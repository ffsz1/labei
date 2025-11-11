'use strict';
var C = require('../config');
var F = require('../common/function');

var _ = require('underscore');
_.str = require('underscore.string');
_.v = require('validator');
var co = require('co');
var redis = require('../libs/redis');
var redisClient = redis.redisClient;
var redisCo = redis.redisCo;

var dbcacheCli = redis.dbcacheCli;
var dbcacheCo = redis.dbcacheCo;

function imMgr(app, common_mgr) {

    var model_map = app.model_mgr.model_map;
    var mgr_map = common_mgr.mgr_map;

    var that = this;
    this.app = app;
    this.socket_dic = {};
    this.start_time = new Date().getTime();
    this.socket_prefix = _.str.sprintf("socket#%s:%s#%s_", C.inner_host, app.port, that.start_time);
    this.clear_prefix = _.str.sprintf("socket#%s:%s#*", C.inner_host, app.port);
    this.process_id = _.str.sprintf("%s:%s", C.inner_host, app.port); // 锁用进程标识

    // 清除之前没关闭的锁
    this.clearLock = function* () {
        let cur_time = new Date().getTime();
        let lock_prefix = "locker_" + this.process_id;
        let lock_list = yield mgr_map.redis.zrange(lock_prefix, 0, cur_time, "WITHSCORES");
        console.log("############lock_list", lock_list);
        if (lock_list.length % 2 == 1) {
            F.addErrLogs(["get old socket list err:", lock_list]);
        }
        for (let i = 0; i + 1 < lock_list.length; i += 2) {
            let key = lock_list[i];
            let time = lock_list[i + 1].toString(); // 字符串格式
            let lock = {
                "suc": 1,
                "time": time,
                "key": key,
                "pid": that.process_id,
                "svrRestartTime": cur_time
            }
            co(mgr_map.redis.releaseLock(lock));
        }
    }


    // 清除启动之前socket
    this.clearSocket = function* () {
        // dbcacheCli.flushdb(); // dbcache 模块应该立马清楚
        yield F.sleep(5000); // 3秒后 不要挤到一起
        var socket_his_list = yield redisCo.keys(that.clear_prefix);
        F.addLogs(["socket_his_list:", socket_his_list]); // 清除旧socket
        for (var i = 0; i < socket_his_list.length; i++) {
            var socket_id = socket_his_list[i];
            if (socket_id.indexOf(that.socket_prefix) == -1) {
                var id_list = socket_id.split('_');
                id_list.splice(0, 1);
                var uniid = id_list.join('_');
                try {
                    var ctx = {};
                    ctx.uid = id_list[id_list.length - 1];
                    let page_name = id_list[id_list.length - 2];
                    yield mgr_map.im.delSvrMap(uniid, false);
                    yield mgr_map.room.abnormalClose(uniid, ctx, page_name);
                } catch (e) {
                    F.addErrLogs(["clear socket err:", e.stack]);
                }
                yield redisCo.del(socket_id);
                F.addLogs(["del socket:", uniid]);
            }
        }
    };

    this.setSocket = function* (uniid, ctx) {
        yield redisCo.set(that.socket_prefix + uniid, uniid);
        that.socket_dic[uniid] = ctx;
    };

    this.delSocket = function* (uniid) {
        delete that.socket_dic[uniid];
        yield redisCo.del(that.socket_prefix + uniid);
    };

    this.setSvrMap = function* (uniid, inner_host, ctx, page_name) {
        var socket_id = ctx.socket.id;
        delete ctx.has_del;
        yield that.setSocket(uniid, ctx);
        // yield mgr_map.redis.addUniidToBigRoom(uniid);
        mgr_map.redisSub.subscribe(uniid);
        // 添加redis登录状态
        // yield mgr_map.socketPage.insertOnlineSocket(ctx.userid, uniid, page_name);
    };

    this.delSvrMap = function* (uniid, kickoffold = true, db, kickoffold_info = null) {
        try {
            if (kickoffold) { // 向旧连接发送kickoff消息
                if (F.isNull(kickoffold_info)) {
                    kickoffold_info = {'errno': 10037, 'errmsg': C.err_msg['10037']};
                }
                F.addLogs(['kickoffold', {uniid: uniid, kickoffold_info}]);
                yield that.sendReq(uniid, "kickoff", kickoffold_info);
            }

            // yield mgr_map.redis.delUniidFromBigRoom(uniid);
            yield mgr_map.redis.delPackNextId(uniid);
            mgr_map.redisSub.unsubscribe(uniid);

            yield that.delSocket(uniid);
        } catch (e) {
            F.addErrLogs(["delSvrMap:", e.stack]);
        }
    };

    // pack req data for client
    this.packReq = function* (uniid, route, data) {
        var send_data = {};
        send_data["id"] = yield mgr_map.redis.getNextReqId(uniid);
        send_data["route"] = route;
        send_data["req_data"] = data;
        return send_data;
    };

    // pack res data for client
    this.packRes = function* (id, route, data) {
        var send_data = {};
        send_data["id"] = parseInt(id);
        send_data["route"] = route;
        send_data["res_data"] = data;
        return send_data;
    };

    this.sendReq = function* (uniid, route, data, cb_data, err_cb) {
        var send_data = yield that.packReq(uniid, route, data);
        err_cb = F.isNull(err_cb) ? that.sendReqErrorCallBack : err_cb;
        var cbid = F.vsprintf("%s_%s", [uniid, send_data.id]);
        if (!F.isNull(cb_data)) yield mgr_map.redis.setSendReqCbData(cbid, cb_data);
        var res = yield that.emit(uniid, route, send_data, err_cb);
        return res;
    };

    this.getSendReqCbData = function* (uniid, msg) {
        var cbid = F.vsprintf("%s_%s", [uniid, msg.id]);
        return yield mgr_map.redis.getSendReqCbData(cbid);
    }

    this.sendRes = function* (uniid, message, data) {
        var lastId = message.id;
        var route = message.route;
        var send_data = yield that.packRes(lastId, route, data);
        send_data.ut = new Date().getTime() - message.start_time;
        var res = yield that.emit(uniid, route, send_data);
        return res;
    }

    this.emit = function* (uniid, route, msg, err_cb) {
        var res_emit = yield this.emitLocal(uniid, route, msg);
        if (res_emit == false) {
            mgr_map.redisSub.publish(uniid, route, msg, err_cb);
        }
    };

    this.emitLocal = function* (uniid, route, msg, from = '') {
        if (!F.isNull(uniid) && uniid in that.socket_dic) {
            var cur_ctx = that.socket_dic[uniid];
            msg = yield that.app.common_mgr.pregReplaceResJson(msg, uniid, cur_ctx);
            if (!F.isNull(msg.ut)) F.addOtherLogs("imrw/imrw", ['##im send: uniid:', uniid, " route:", route]);
            that.app.emit(cur_ctx, route, msg);
            if (route == "kickoff") { // 如果是剔除，要打上剔除标识 从user_dic里删除
                cur_ctx.has_del = true;
                setTimeout(function () {
                    cur_ctx.disconnect();
                }, 1000);
                yield that.delSocket(uniid);
                F.log("debug", "local remove old socket,uniid:%s", [uniid]);
            }
            return true;
        } else {
            if (!F.isNull(from)) F.addErrLogs(["##im send fail,sock not found:", uniid, route, msg]);
            return false;
        }
    }

    this.sendReqErrorCallBack = function (uniid, route, msg) {
        if ("res_data" in msg) {
            mgr_map.logs.addLogs("err/err", ["send res error:", route]);
        }
        if (F.isNull(uniid)) F.throwErr("send msg faile. uniid is null.");
        var id_list = uniid.split('_');
        var ctx = {};
        ctx.uid = id_list[id_list.length - 1];
        let page_name = id_list[id_list.length - 2];
        co(mgr_map.im.delSvrMap(uniid, false));
        //co(mgr_map.room.abnormalClose(uniid, ctx, page_name));
        mgr_map.logs.addLogs("err/err", "send msg to uniid:" + uniid + " faile. route:" + route + '. msg:' + JSON.stringify(msg));
    }


};

module.exports = imMgr;
