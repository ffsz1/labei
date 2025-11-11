
var staticCache = require('koa-static-cache');
var koa = require('koa.io');
var path = require('path');
var fs = require('fs');
var co = require('co');

function rpcServer() {
    this.app = koa();
    this.cid = 0;
    this.response_cb_map = {};
    this.static_cb_map = {};
    this.err_cb_map = {};
    this.all_ctx_dic = {};
    this.BeatTimerId = -1;
    var that = this;
    var rpc_svr = this;

    this.nextId = function() {
        var new_id = ++rpc_svr.cid;
        if (rpc_svr.cid > 999999999) {
            rpc_svr.cid = 0;
        }
        return new_id;
    };

    this.setBeatTime = function(beat_time, timeout) {
        if (that.BeatTimerId > 0) {
            clearInterval(that.BeatTimerId);
            that.BeatTimerId = -1;
        }
        if (beat_time <= 0) return;
        var timeout = timeout || 20000;
        that.BeatTimerId = setInterval(function(){
            for (var key in that.all_ctx_dic) {
                (function(){
                    var cur_ctx = that.all_ctx_dic[key];
                    that.emit(cur_ctx,'rpc_beat',"",{
                      "success": function*(){
                        //console.log("rpc server beat suc");
                      },
                      "timeout_time": timeout,
                      "timeout_cb": function*(){
                        console.log('server beat timeout');
                        cur_ctx.timeout = true;
                        cur_ctx.disconnect();
                      },
                      "error": function* (){console.log('server beat err');}
                    });
                })();
            }
        }, beat_time);
    };

    this.app.io.route("rpc_response", function* (next,data){
        if ("rpc_cid" in data && data.rpc_cid in that.response_cb_map) {
            var suc_cb = that.response_cb_map[data.rpc_cid];
            var suc_data = data.rpc_data || {};
            yield suc_cb(suc_data);
        }
    }); 

    this.app.io.route("rpc_request", function* (next,data){
        if (!("rpc_route" in data)) {
            return;
        }
        var rpc_route = data.rpc_route;
        if (!(rpc_route in that.static_cb_map)) {
            rpc_route = '*';
        }
        if (rpc_route in that.static_cb_map) {
            var route_cb = that.static_cb_map[rpc_route];
            var rpc_data = data.rpc_data || {};
            var ctx = this;
            try {
                if (that.beforeCallback) {
                    var before_res = yield that.beforeCallback(rpc_route,this,rpc_data);
                    if (!before_res) return;
                }
                yield route_cb(next, this, rpc_data, function(response) {
                    var response_json = {};
                    response_json["rpc_cid"] = data.rpc_cid;
                    response_json["rpc_data"] = response;
                    ctx.emit('rpc_response', response_json);
                });
            } catch (e) {
                if (that.catchException) {
                    yield that.catchException(rpc_route, this, rpc_data, e);
                }
                console.log(e.stack);
            }
        }
    });

    function internalio() {
        this.use = function(cb) {
            that.app.io.use(function* (next) {
                var sockid = this.socket.id;
                that.all_ctx_dic[sockid] = this;
                yield cb(next, this);
                delete that.all_ctx_dic[sockid];
                if (sockid in that.err_cb_map) {
                    var sub_map = that.err_cb_map[sockid];
                    for (var key in sub_map) {
                        var err_cb = sub_map[key];
                        yield err_cb();
                    }
                    delete that.err_cb_map[sockid];
                }
            });
        };
        this.route = function(route, cb) {	
            that.static_cb_map[route] = cb;
            that.app.io.route(route, function* (next,msg) {
                try {
                    if (that.beforeCallback) {
                        var before_res = yield that.beforeCallback(route,this,msg);
                        if (!before_res) return;
                    }
                    yield cb(next,this,msg);
                } catch (e) {
                    if (that.catchException) {
                        yield that.catchException(route,this,msg,e);
                    }
                    console.log(e.stack);
                }
            });
        };
        // for beat
        this.route("rpc_beat", function* (next,ctx,msg,cb){
            if (cb) cb("");
        });
    }
    this.io = new internalio(); 

    this.use = function(options) {
        that.app.use(options);
    }

    this.listen = function(port, options) {
        that.app.listen(port, options);
    }

    this.sleep = function(ms) {
        return function(done) {
            setTimeout(done,ms);
        }
    }

    //options:
    //  success: callback for success response
    //  error: callback for error
    //  if has success callback:
    //    timeout_time: time for timeout ms
    //    timeout_cb: callback for timeout
    this.emit = function(ctx,route,msg,options) {
        options = options || {};
        //if (!options.success && !options.error) {// no callback
        if (false) {// no callback
            if (msg) {
                ctx.emit(route,msg);
            } else {
                ctx.emit(route);
            }
        } else {
            var sid = ctx.socket.id;
            var new_id =String(that.nextId());
            var timeout_id;
            if (options.success) {
                // timeout callback
                if (options.timeout_cb) {
                    var timeout = options.timeout_time || 10000;
                    timeout_id = setTimeout(function() {
                            var sub_err_map = that.err_cb_map[sid] || {};
                            if (new_id in sub_err_map) {delete sub_err_map[new_id];} // rm err cb
                            if (new_id in that.response_cb_map) {
                                delete that.response_cb_map[new_id]; // rm success cb
                                if(typeof(options.timeout_cb)=="function"){
                                    co(options.timeout_cb()); // exe timeout cb
                                    //options.timeout_cb();
                                }else{
                                    console.log(options.timeout_cb+'is not a function');
                                }
                            }
                    }, timeout);
                }

                // success callback
                var suc_cb_cb = function*(suc_data) {
                    if (new_id in that.response_cb_map) {delete that.response_cb_map[new_id];}
                    var sub_err_map = that.err_cb_map[sid] || {};
                    if (new_id in sub_err_map) {delete sub_err_map[new_id];} // rm err cb
                    if (timeout_id) {clearTimeout(timeout_id);} // rm timeout cb
                    yield options.success(suc_data); // exe success cb
                };
                that.response_cb_map[new_id] = suc_cb_cb;
            }

            // err callback
            if (options.error) {
                var e_cb_cb = function*() {
                    if (new_id in that.response_cb_map) {delete that.response_cb_map[new_id];} // rm success cb
                    if (timeout_id) {clearTimeout(timeout_id);} // rm timeout cb
                    yield options.error(); // exe error cb
                }
                var sub_cb_map = that.err_cb_map[sid] || {};
                sub_cb_map[new_id] = e_cb_cb;
                that.err_cb_map[sid] = sub_cb_map;
            }

            var new_msg = {};
            new_msg["rpc_cid"] = new_id;
            new_msg["rpc_route"] = route;
            if (msg) {
                new_msg["rpc_data"] = msg;
                ctx.emit("rpc_request",new_msg);
            } else {
                ctx.emit("rpc_request",new_msg);
            }
        }
    };
}






module.exports = rpcServer;
