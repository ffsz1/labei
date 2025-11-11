'use strict';
var redis = require('../libs/redis');
var redisClient = redis.redisClient;
var redisCo = redis.redisCo;
var C = require('../config');
var F = require('../common/function');

var co = require('co');
var _ = require('underscore');
_.str = require('underscore.string');
_.v = require('validator');
var request = require('koa-request');

// 清楚之前的定时器
var clear_timer = function* (clear_prefix,timer_prefix) {
    clear_prefix = _.str.sprintf(C.redisPre.timer_id_prefix, clear_prefix);
    timer_prefix = _.str.sprintf(C.redisPre.timer_id_prefix, timer_prefix);
    console.log("timerid his:",clear_prefix);
    var timerid_his = yield redisCo.keys(clear_prefix);
    if (F.isNull(timerid_his)) return;
    console.log("clear timer his:",timerid_his);
    for (var i = 0; i < timerid_his.length; i++) {
        var timerid = timerid_his[i];
        if (timerid.indexOf(timer_prefix) == -1) {
            yield redisCo.del(timerid);
            console.log("del timerid:",timerid);
        }
    }
};

function timerMgr(app, common_mgr) {

    var model_map = app.model_mgr.model_map;
    var mgr_map = common_mgr.mgr_map;

    var that = this;
    this.app = app;
    this.timeout_id_map = {};
    this.interval_id_map = {};
    this.start_time = new Date().getTime();
    this.timer_prefix = _.str.sprintf("timer#%s:%s#%s_", C.inner_host, app.port, that.start_time);
    this.clear_prefix = _.str.sprintf("timer#%s:%s#*", C.inner_host, app.port);
    co(clear_timer(this.clear_prefix,this.timer_prefix));


    this.recordTimerid = function* (key, timerid) {

    }

    this.getTimerid = function* (key) {

    }


    this.innerRunTimeout = function* (fn,timeout,redis_timer_id,delete_redis = true) {
        that.timeout_id_map[redis_timer_id] = setTimeout(function() {
            co(function*(){
                var get_timerid_res = yield mgr_map.redis.getTimerId(redis_timer_id);
                if (F.isNull(get_timerid_res)) { // 说明定时器被删除
                    var real_timerid = that.timeout_id_map[redis_timer_id];
                    if (F.isNull(real_timerid)) {
                        F.log("err","redis timeris is null,but can not find real timerid to clear");
                        return;
                    }
                    clearTimeout(real_timerid);
                    delete that.timeout_id_map[redis_timer_id];
                    return;
                }
                try {
                    yield fn(); // 执行回调
                } catch(e) {
                    console.log(e);
                }
                try {
                    if(delete_redis == true) {
                        yield mgr_map.redis.delTimerId(redis_timer_id);
                    }
                    var real_timerid = that.timeout_id_map[redis_timer_id];
                    if (F.isNull(real_timerid)) {
                        F.log("err","redis timeris is null,but can not find real timerid to clear when exe close");
                    }
                    clearTimeout(real_timerid);
                    delete that.timeout_id_map[redis_timer_id];
                } catch(e) {
                    console.log(e);
                }
            }());
        },timeout);
        return redis_timer_id;
    };

    this.innerRunInterval = function* (fn,timeout,redis_timer_id) {
        try {
            yield fn(); // 立马执行
        } catch(e) {
            console.log(e);
        }
        that.interval_id_map[redis_timer_id] = setInterval(function() {
            co(function*(){
                var get_timerid_res = yield mgr_map.redis.getTimerId(redis_timer_id);
                console.log("########:"+redis_timer_id);
                if (F.isNull(get_timerid_res)) { // 说明定时器被删除
                    mgr_map.logs.roomManInLogs('定时器被删除', {'timer_id':redis_timer_id});
                    var real_timerid = that.interval_id_map[redis_timer_id];
                    if (F.isNull(real_timerid)) {
                        F.log("err","redis timeris is null,but can not find real timerid to clear");
                        return;
                    }
                    clearInterval(real_timerid);
                    delete that.interval_id_map[redis_timer_id];
                    return;
                }
                try {
                    yield fn(); // 执行回调
                } catch(e) {
                    console.log(e);
                }
            }());
        },timeout);
        return redis_timer_id;
    };

    // 判断定时器是否存在 interval timeout 定时器都适用
    this.isTimerExist = function* (timerid) {
        var get_timerid_res = yield mgr_map.redis.getTimerId(timerid);
        if (F.isNull(get_timerid_res)) { // 说明定时器被删除
            return 0;
        }
        return 1;
    };

    // fn 必须是异步函数
    this.innerSetTimeout = function* (fn,timeout) {
        if (timeout > 2147483647) F.throwErrCode(10012);
        var redis_timer_id = yield mgr_map.redis.getNextReqId("timer");
        redis_timer_id = this.timer_prefix + redis_timer_id;
        yield mgr_map.redis.setTimerId(redis_timer_id);
        return yield this.innerRunTimeout(fn,timeout,redis_timer_id);
    };

    this.innerClearTimeout = function* (timerid) {
        yield mgr_map.redis.delTimerId(timerid);
    };


    // fn 必须是异步函数
    this.innerSetInterval = function* (fn,timeout, starttime) {
        if (timeout > 2147483647 || starttime > 2147483647) F.throwErrCode(10014);
        mgr_map.logs.roomManInLogs('定时器内部参数:', {'timeout':timeout, 'starttime':starttime});
        var redis_timer_id = yield mgr_map.redis.getNextReqId("timer");
        redis_timer_id = this.timer_prefix + redis_timer_id;
        yield mgr_map.redis.setTimerId(redis_timer_id);
        if (!F.isNull(starttime)) {
            var timer = this;
            yield this.innerRunTimeout(function* () {
                                        yield timer.innerRunInterval(fn,timeout,redis_timer_id);
                                        },starttime,redis_timer_id,false);
        } else {
            yield this.innerRunInterval(fn,timeout,redis_timer_id);
        }
        return redis_timer_id;
    };

    this.innerClearInterval = function* (timerid) {
        yield mgr_map.redis.delTimerId(timerid);
    };

};

module.exports = timerMgr;
