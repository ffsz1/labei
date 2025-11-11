
var path = require('path');
var express = require('express');
var fs = require('fs');
var co = require('co');
var logs_obj = require('../libs/logs.js');
var logs = new logs_obj();
var C = require('../config/index');
var F = require('../common/function');

function isNull(obj) {
    if (obj == null || typeof(obj) == "undefined" || obj.length == 0) {
        return true;
    }
    if(typeof(obj) == 'object' && Object.keys(obj).length == 0) {
        return true;
    }
    return false;
};

function rpcWithExpress(opt) {
    this.exp = express();
    if (!isNull(opt) && "ssl" in opt) {
        var opts = {
            key: fs.readFileSync('/etc/nginx/server.key'),
            cert: fs.readFileSync('/etc/nginx/server.crt')
        };
        this.svr = require('https').createServer(opts,this.exp);
    } else {
        this.svr = require('http').createServer(this.exp);
    }
    this.io = require('socket.io')(this.svr);
    this.static_cb_map = {};
    var that = this;

    that.io.route = function (route, fn) {
        that.static_cb_map[route] = fn;
    };

    var desDecode = function (ctx,route,msg) {
        try {
            if ("ed" in msg && F.isNull(msg.is_websocket)) { // 说明是socket.io加密的
                ctx.ed = 1;
                let encode_msg = msg.ed;
                let decode_msg = F.decipher(encode_msg);
                msg = JSON.parse(decode_msg);
                F.addOtherLogs('proxy/proxy',["receive data(sockio):",decode_msg]);
            }
        } catch(e) {
            F.addOtherLogs('proxy/proxy',["socket.io decode err:",msg]);
        }
        return msg;
    }

    var desEncode = function (ctx,route,msg) {
        try {
            if (!F.isNull(ctx.ed)) { // 说明是socket.io加密的
                let encode_msg = F.cipher(JSON.stringify(msg));
                msg = {"ed":encode_msg};
            }
        } catch(e) {
            F.addOtherLogs('proxy/proxy',["socket.io encode err:",msg]);
        }
        return msg;
    }

    that.main = function* (ctx,route,msg,fn) {
        try {
            ctx.up_time = new Date().getTime();
            let start_time = new Date().getTime();
            if (that.beforeCallback) {
                var before_res = yield that.beforeCallback(route,ctx,msg);
                if (!before_res) return;
            }
            msg.start_time = start_time;
            yield fn(null, ctx, msg, null);
            let end_time = new Date().getTime();
            if (end_time > start_time+C.slow_log_delta) {
                logs.addLogs("slow/slow",[`usetime:${end_time-start_time}`,"msg:",msg]);
            }
        } catch (e) {
            if (that.catchException) {
                yield that.catchException(route, ctx, msg, e);
            }
            console.log(e.stack);
        }
    };

    that.io.on('connection', function (socket) {
        //F.addOtherLogs('proxydebug/proxydebug',[" socket connect:",socket.request.url]);
        var ctx = socket;
        ctx.up_time = new Date().getTime();
        ctx.socket = socket; // 为了统一koa接口

        var ping_msg = {
            id:0,
            route:'heartbeat',
            req_data:{}
        }

        var timerid = setInterval(function() {
            var cur_time = new Date().getTime();
            var time_delta = cur_time - ctx.up_time;
            //if (ctx.up_time + 18*1000 <= cur_time) {
            //    F.addDebugLogs(['send ping msg',time_delta]);
            //    ctx.emit('*',ping_msg);
            //}
            if (ctx.up_time + 30*1000 <= cur_time) {
                F.addDebugLogs(['was disconnect by cleaner',time_delta]);
                ctx.disconnect();
            }
        },1000*10);

        if (that.onconnect) co(that.onconnect(null,ctx));

        ctx.on('disconnect', function (reason) {
            clearInterval(timerid);
            if (that.ondisconnect) co(that.ondisconnect(null,ctx,reason));
        });

        for (var key in that.static_cb_map) {
            (function(){
                var k = key;
                var fn = that.static_cb_map[key];
                ctx.on(k, function (msg) {
                    msg = desDecode(ctx,null,msg);
                    ctx.up_time = new Date().getTime();
                    //F.addDebugLogs(['up time for:',ctx.up_time]);
                    co(that.main(ctx,k,msg,fn));
                });
            })();
        }

        ctx.on("all#route", function (msg) {
            msg = desDecode(ctx,null,msg);
            (function(){
	        var key = "unmatchRoute";
	        if (msg.route in that.static_cb_map) key = msg.route;
                var fn = that.static_cb_map[key];
                ctx.up_time = new Date().getTime();
                //F.addDebugLogs(['up time for:',ctx.up_time]);
                co(that.main(ctx,msg.route,msg,fn));
            })();
        });

    });

    that.emit = function(cur_ctx,route,msg) {
        msg = desEncode(cur_ctx,route,msg);
        cur_ctx.emit('*', msg);
    };

}






module.exports = rpcWithExpress;
