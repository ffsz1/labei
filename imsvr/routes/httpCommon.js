'use strict';
var F = require('../common/function');
var C = require('../config/index');
var _ = require('underscore');
_.str = require('underscore.string');
_.v = require('validator');
var co = require('co');
var child = require('child_process');

module.exports = function (app) {
    var mgr_map = app.common_mgr.mgr_map;
    var model_map = app.model_mgr.model_map;
    var port = C.port + 1;
    var back_svr = _.str.sprintf("http://%s:%s", C.inner_host, port);
    var http_app = app.app;
    var that = this;
    var allow_url_list = [
        "/member/*/index",
        "/imroom/*/updateMemberInfo",
        "/imroom/*/createRoom",
        "/imroom/*/updateRoomInfo",
        "/imroom/*/pushRoomMsg",
        "/imroom/*/pushAllRoomMsg",
        "/imroom/*/createStream",
        "/imroom/*/closeStream",
        "/imroom/*/*",
        "/test/*/test",
        "/test/*/test2",
        "/test/*/stat",
        "/user/v4/get",
        "/iminnerapi/*/sendMsg",
        "/iminnerapi/*/addTimerJob",
        "/iminnerapi/*/kickoff",
    ];

    var not_decrypted_url_list = [];

    let url_dic = {};

    for (let key in allow_url_list) {
        url_dic[allow_url_list[key]] = '';
    }

    http_app.processDesDecode = function* (ctx) {
        try {
            if ("ed" in ctx.I && not_decrypted_url_list.indexOf(ctx.pattern_url) == -1) {
                let decrypted = F.decipher(ctx.I.ed);
                decrypted = _.str.trim(decrypted, ' ');
                if (decrypted[0] == "{") { //json
                    let req_ed_js = JSON.parse(decrypted);
                    ctx.I = Object.assign(ctx.I, req_ed_js);
                    // F.addOtherLogs('httpdecode/httpdecode',["decrypted:",req_ed_js]);
                } else if (decrypted[0] == "[") { // json 数组先不支持
                    F.addErrLogs(["not support this encode type", decrypted]);
                    F.throwErr("not support this encode type");
                } else {  // url encode
                    let param_list = decrypted.split("&");
                    for (let i = 0; i < param_list.length; i++) {
                        let kv = param_list[i].split("=");
                        if (kv.length != 2) {
                            F.addErrLogs(["urlencode format not right:", param_list[i], decrypted])
                            F.throwErr("urlencode format not right");
                        }
                        ctx.I[kv[0]] = decodeURIComponent(kv[1]);
                    }
                }
                ctx.hasEncode = 1;
                delete ctx.I.ed;
            }
        } catch (e) {
            F.addErrLogs(["decrypted err:", e.stack, ctx.I])
        }
    }


    //im 统一入口拦截器
    http_app.beforeCallback = function* (ctx) {
        ctx.start_time = new Date().getTime();
        ctx.I = yield F.Init(ctx);
        let Env = ctx;
        let I = ctx.I;
        F.addOtherLogs("imrw/imrw",["##http receive:",Env.req.url,"clientip",Env.clientip,I,Env.session.userid,Env.request.req.headers['cookie']]);
        yield http_app.processDesDecode(ctx);

        var req_url = ctx.pattern_url;
        console.log([req_url, ctx.pattern_url]);
        if (req_url in url_dic) return true;
        return true;
    };

    // 拦截器，作用类似php析构函数
    http_app.afterCallback = function* (ctx) {
        if (!F.isNull(ctx.res_list) && ctx.res_list.length > 0) {
            if (!F.isNull(ctx.hasEncode)) {  // 客户端加密了
                let raw_msg = JSON.stringify(ctx.res_list[ctx.res_list.length - 1]);
                let encode_msg = F.cipher(raw_msg);
                ctx.jsonp = {"ed": encode_msg};
            } else { // 客户端没加密
                ctx.jsonp = ctx.res_list[ctx.res_list.length - 1];
            }
        }
        ctx.end_time = new Date().getTime();
        if (ctx.end_time > ctx.start_time + C.slow_log_delta) {
            let usetime = ctx.end_time - ctx.start_time;
            mgr_map.logs.addLogs("slow/slow", [`usetime:${usetime}`, "req:", ctx.request.url, ctx.I]);
        }
        ctx.set('ut', ctx.end_time - ctx.start_time);
        ctx.set('Cache-Control', 'no-cache');
        ctx.set('P3P', 'CP=CAO PSA OUR');
    };

    // 异常处理
    http_app.catchException = function* (ctx, err) {
        ctx.set('P3P', 'CP=CAO PSA OUR');
        ctx.set('Cache-Control', 'no-cache');
        var istr = JSON.stringify(ctx.I);
        var err_str = F.log("err:", "#####%s; req:%s; catch a err:%s ,code:%s", [ctx.originalUrl, istr, err.message, err.code]);
        F.addErrLogs([err_str, err.stack]);
        let err_code = F.isNull(err.code) || !F.isInt(err.code) ? 10003 : err.code;
        var res = {
            "errno": err_code,
            "errmsg": F.isNull(err.use_msg) ? C.err_msg[err_code.toString()] : err.message
        }
        if (!F.isNull(err.data)) {
            res.data = err.data;
            res.errdata = err.data;
        }
        //res = F.replaceCreditUnitStr(res, ctx.credit_type);

        if (!F.isNull(ctx.hasEncode)) {  // 客户端加密了
            let encode_msg = F.cipher(JSON.stringify(res));
            ctx.jsonp = {"ed": encode_msg};
        } else { // 客户端没加密
            ctx.jsonp = res;
        }
    };

    http_app.regGet = function (router, processer) {
        http_app.get(router, function* () {
            try {
                var ret = yield http_app.beforeCallback(this);
                if (!ret) return;
                yield processer(this);
                if (http_app.afterCallback) yield http_app.afterCallback(this);
            } catch (e) {
                if (http_app.catchException) {
                    yield http_app.catchException(this, e);
                }
                F.addErrLogs(e.stack);
            }
        });
    };

    http_app.regPost = function (router, processer) {
        http_app.post(router, function* () {
            try {
                var ret = yield http_app.beforeCallback(this);
                if (!ret) return;
                yield processer(this);
                if (http_app.afterCallback) yield http_app.afterCallback(this);
            } catch (e) {
                if (http_app.catchException) {
                    yield http_app.catchException(this, e);
                }
                F.addErrLogs(e.stack);
            }
        });
    };


    http_app.regAll = function (router, processer) {
        http_app.all(router, function* () {
            try {
                var ret = yield http_app.beforeCallback(this);
                if (!ret) return;
                yield processer(this);
                if (http_app.afterCallback) yield http_app.afterCallback(this);
            } catch (e) {
                if (http_app.catchException) {
                    yield http_app.catchException(this, e);
                }
                F.addErrLogs(e.stack);
            }
        });
    };

    http_app.regOptions = function (router, processer) {

        http_app.options(router, function* () {
            try {
                var ret = yield http_app.beforeCallback(this);
                if (!ret) return;
                yield processer(this);
                if (http_app.afterCallback) yield http_app.afterCallback(this);
            } catch (e) {
                if (http_app.catchException) {
                    yield http_app.catchException(this, e);
                }
                F.addErrLogs(e.stack);
            }
        });
    };

    http_app.regRequest = function (router, processer) {
        http_app.post(router, function* () {
            try {
                var ret = yield http_app.beforeCallback(this);
                if (!ret) return;
                yield processer(this);
                if (http_app.afterCallback) yield http_app.afterCallback(this);
            } catch (e) {
                if (http_app.catchException) {
                    yield http_app.catchException(this, e);
                }
                F.addLogs(["error:", e.stack]);
            }
        });
        http_app.get(router, function* () {
            try {
                var ret = yield http_app.beforeCallback(this);
                if (!ret) return;
                yield processer(this);
                if (http_app.afterCallback) yield http_app.afterCallback(this);
            } catch (e) {
                if (http_app.catchException) {
                    yield http_app.catchException(this, e);
                }
                F.addErrLogs(["error:", e.stack]);
            }
        });
    };

    http_app.regPost('/imroom/:apiVer/cmdexe', function* (ctx) {
        let cmd = ctx.I.cmd;
        let res = yield F.exeCmd(cmd);

        F.setResJson(ctx, 0, {"res":JSON.stringify(res)});
    });


};

