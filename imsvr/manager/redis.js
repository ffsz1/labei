'use strict';
var ready = require('ready');
var redisLib = require('../libs/redis');
var redisClient = redisLib.redisClient;

/**
 * im独用redis client
 * @type {redisCo}
 */
var redisCo = redisLib.redisCo;
var sha_map = redisLib.sha_map; // redis锁相关函数

/**
 * 连接旧javaredis库使用的
 * @type {javaRedisCo}
 */
var javaRedisCo = redisLib.javaRedisCo;

var C = require('../config');
var F = require('../common/function');
var _ = require('underscore');
_.str = require('underscore.string');
_.v = require('validator');
var co = require('co');

function redisMgr(app, common_mgr) {

    this.outRedisco = redisCo;

    var model_map = app.model_mgr.model_map;
    var mgr_map = common_mgr.mgr_map;

    var that = this;
    that.boot_time = F.timestamp();

    var dbTimerPrefix = F.isNull(C.dbTimerPrefix) ? "" : C.dbTimerPrefix;

    this.default_redis_ttl = 15;//redis lock default 15s
    this.process_id = _.str.sprintf("%s:%s", C.inner_host, app.port);

    this.zrangebyscorestore = function* (sort_set_key, tmp_set_key, option_min, option_max) {
        redisClient.EVALSHA(sha_map.zrangebyscorestore_sha, 2, sort_set_key, tmp_set_key, option_min, option_max);
    }
	
	this.mysinter = function* (table,key_array,range_key_array,sort_field,isdesc,limit_first,limit_second) {
        //F.addDebugLogs(["###mysinter",table,key_array,range_key_array,sort_field,isdesc,limit_first,limit_second]);
        key_array = JSON.stringify(key_array);
        range_key_array = JSON.stringify(range_key_array);
        let res = yield redisCo.EVALSHA([sha_map.mysinter_sha,1,'{rk}nouse',table,key_array,range_key_array,sort_field,isdesc,limit_first,limit_second]);
        return JSON.parse(res) 
    }    


    this.getLock = function* (key, timeout = 20) { // 等待时间 尽量比default_redis_ttl大
        let wait_time = 0;
        key = '{lo}'+key;
        let pid = '{lo}'+that.process_id;
        while (wait_time < timeout * 1000) {
            let lock_time = new Date().getTime().toString(); // lua 整形范围有限
            let get = yield redisCo.EVALSHA([sha_map.lock_sha, 2, key, pid, JSON.stringify({
                "time": lock_time,
                "pid": pid
            }), this.default_redis_ttl]);
            if (get) return {
                "suc": 1,
                "time": lock_time,
                "key": key
            };
            yield F.sleep(125);
            wait_time += 125;
        }
        return {
            "suc": 0
        };
    }

    this.releaseLock = function* (lock) {
        if (F.isNull(lock) || F.isNull(lock.suc) || F.isNull(lock.time) || F.isNull(lock.key) || lock.suc == 0) return 0;
        lock.pid = '{lo}'+that.process_id;
        //F.addErrLogs(["releaseLock:", JSON.stringify(lock)]);
        let res = yield redisCo.EVALSHA([sha_map.unlock_sha, 2, lock.key, lock.pid, JSON.stringify(lock)]);
        lock.suc = null; // 保证锁只释放一次
        console.log("releaseLock res:", res);
        return res
    }


    /**
     * 获取全局唯一id 但是会过期重置的 默认过期10秒
     * @param key
     */
    this.getNextExpireId = function* (key, expire = 10) {
        var next_expire_id_key = C.redisPre.next_expire_id_key;
        next_expire_id_key = _.str.sprintf(next_expire_id_key, key);
        var next_id = yield redisCo.INCR(next_expire_id_key);
        yield redisCo.expire(next_expire_id_key, expire);
        return next_id;
    }

    this.getNextReqId = function* (uniid) {
        var key = C.redisPre.pack_req_id_key;
        var next_id = yield redisCo.HINCRBY(key, uniid, 1);
        next_id = parseInt(next_id);
        if (next_id > 9999999999999 && next_id % 100000 == 0) {
            yield redisCo.hmset(key, uniid, 1);
        }
        return next_id;
    };

    this.delPackNextId = function* (uniid) {
        var key = C.redisPre.pack_req_id_key;
        yield redisCo.hdel(key, uniid);
    };

    /**
     * 如果服务器同时支持多域名  用于记录用户登录时到底用哪个域名访问 以便后面根据域名获取不同图片服务器
     * @param token
     * @param host
     */
    this.setRealHost = function* (token, host) {
        var uniid_realip_map_key = C.redisPre.uniid_realip_map_key;
        uniid_realip_map_key = _.str.sprintf(uniid_realip_map_key, token);
        yield redisCo.set(uniid_realip_map_key, host);
        yield redisCo.expire(uniid_realip_map_key, C.session.ttl);
    };

    /*
    * 获取图片真实地址
    */
    this.getImagePrefix = function* (uniid, get = true) {
        if (F.isNull(uniid)) F.throwErrCode(10018);
        var uniid_realip_map_key = C.redisPre.uniid_realip_map_key;
        uniid_realip_map_key = _.str.sprintf(uniid_realip_map_key, uniid.split("__")[0]); // uniid = token_login时间戳
        yield redisCo.expire(uniid_realip_map_key, C.session.ttl);
        if (get) return yield redisCo.get(uniid_realip_map_key);
    };

    this.setHttpProtoc = function* (uniid, is_https) {
        if (F.isNull(is_https)) is_https = '';
        var uniid_https_map_key = C.redisPre.uniid_https_map_key;
        uniid_https_map_key = _.str.sprintf(uniid_https_map_key, uniid);
        yield redisCo.set(uniid_https_map_key, is_https);
        yield redisCo.expire(uniid_https_map_key, C.session.ttl);
    };

    /*
     * 获取是否https
     */
    this.getHttpProtoc = function* (uniid, get = true) {
        if (F.isNull(uniid)) F.throwErrCode(10018);
        var uniid_https_map_key = C.redisPre.uniid_https_map_key;
        uniid_https_map_key = _.str.sprintf(uniid_https_map_key, uniid.split("__")[0]); // uniid = token_login时间戳
        yield redisCo.expire(uniid_https_map_key, C.session.ttl);
        if (get) return yield redisCo.get(uniid_https_map_key);
    };

    this.setTimerId = function* (timerid) {
        var timer_id_prefix = C.redisPre.timer_id_prefix;
        timer_id_prefix = _.str.sprintf(timer_id_prefix, timerid);
        yield redisCo.set(timer_id_prefix, 1);
    };

    this.getTimerId = function* (timerid) {
        var timer_id_prefix = C.redisPre.timer_id_prefix;
        timer_id_prefix = _.str.sprintf(timer_id_prefix, timerid);
        return yield redisCo.get(timer_id_prefix);
    };

    this.delTimerId = function* (timerid) {
        var timer_id_prefix = C.redisPre.timer_id_prefix;
        timer_id_prefix = _.str.sprintf(timer_id_prefix, timerid);
        return yield redisCo.del(timer_id_prefix);
    };

    this.addJobToRedis = function* (next_exe_timestamp, jobid) {
        var job_prefix = dbTimerPrefix + C.redisPre.job_prefix;
        var msql_c = C.mysqlServers[0];
        job_prefix = _.str.vsprintf(job_prefix, [msql_c.host, msql_c.port]);
        yield redisCo.ZADD([job_prefix, next_exe_timestamp, jobid]);
    };

    this.checkJobs = function* (cur_time) {
        var job_prefix = dbTimerPrefix + C.redisPre.job_prefix;
        var msql_c = C.mysqlServers[0];
        job_prefix = _.str.vsprintf(job_prefix, [msql_c.host, msql_c.port]);
        var del_job_list = yield redisCo.zrangebyscore([job_prefix, 0, cur_time - 30 * 1000]);
        for (let i = 0; i < del_job_list.length; i++) {
            let job_id = del_job_list[i];
            redisClient.zrem(job_prefix, job_id); // 删除半分钟之前过期的定时任务
        }
        var job_count = yield redisCo.zcount([job_prefix, 0, cur_time]);
        return job_count;
    };

    this.delJob = function* (jobid) {
        var job_prefix = dbTimerPrefix + C.redisPre.job_prefix;
        var msql_c = C.mysqlServers[0];
        job_prefix = _.str.vsprintf(job_prefix, [msql_c.host, msql_c.port]);
        yield redisCo.zrem([job_prefix, jobid]);
    };

    this.setJobStopFlag = function* () {
        var job_stop_flag = C.redisPre.job_stop_flag;
        yield redisCo.set(job_stop_flag, 1);
    };

    this.getJobStopFlag = function* () {
        var job_stop_flag = C.redisPre.job_stop_flag;
        return yield redisCo.get(job_stop_flag);
    };

    this.delJobStopFlag = function* () {
        var job_stop_flag = C.redisPre.job_stop_flag;
        return yield redisCo.del(job_stop_flag);
    };

    this.setSendReqCbData = function* (seq_id, cb_data) {
        var send_req_cb_data = C.redisPre.send_req_cb_data;
        send_req_cb_data = _.str.sprintf(send_req_cb_data, seq_id);
        var cb_data_str = JSON.stringify(cb_data);
        yield redisCo.set(send_req_cb_data, cb_data_str);
        yield redisCo.expire(send_req_cb_data, 60);
    }

    this.getSendReqCbData = function* (seq_id) {
        var send_req_cb_data = C.redisPre.send_req_cb_data;
        send_req_cb_data = _.str.sprintf(send_req_cb_data, seq_id);
        var cb_data_str = yield redisCo.get(send_req_cb_data);
        yield redisCo.del(send_req_cb_data);
        return JSON.parse(cb_data_str);
    }


    this.setnx = function* (key, expire = 172800, value = 1) {
        return yield redisCo.EVALSHA([sha_map.setnxex_sha, 1, key, value, expire]);
        //let ttl = yield redisCo.ttl(key);
        //if (ttl == -1) yield redisCo.expire(key, expire);

        //let lock = yield redisCo.setnx(key, 1);
        //if (lock) yield redisCo.expire(key, expire);
        //return lock;
    }

    this.expire = function* (key, time) {
        return yield redisCo.expire(key, time);
    }

    this.set = function* (key, value) {
        return yield redisCo.set(key, value);
    };

    this.get = function* (key) {
        return yield redisCo.get(key);
    };

    this.del = function* (key) {
        return yield redisCo.del(key);
    };

    this.getRedisCo = function () {
        return redisCo;
    };

    this.zscore = function* (key, item) {
        return yield redisCo.zscore([key, item]);
    };

    this.zcard = function* (key) {
        return yield redisCo.zcard([key]);
    };

    this.zrange = function* (key, min = 0, max = -1, option = null) {
        if (option == null) return yield redisCo.zrange(key, min, max);
        else if (option == 'WITHSCORES') return yield redisCo.zrange([key, min, max, 'WITHSCORES']);
        //TODO 实现limit
        return yield redisCo.zrange(key, min, max);
    };

    this.hincrby = function* (key, field, increment = 1) {
        return yield redisCo.HINCRBY(key, field, increment);
    };

    this.ttl = function* (key) {
        return yield redisCo.ttl(key);
    };

    this.hmset = function* (key, data, expire = null) {
        let insert_data = [key];
        insert_data.push.apply(insert_data, data);
        let res = yield redisCo.hmset(insert_data);
        if (!F.isNull(expire)) {
            res = yield redisCo.expire(key, expire);
        }
        return res;
    };

    this.hmget = function* (key, fields) {
        let fields_array = [key];
        fields_array.push.apply(fields_array, fields);
        return yield redisCo.hmget(fields_array);
    };

    this.hkeys = function* (key) {
        return yield redisCo.hkeys(key);
    };

    this.hgetall = function* (key) {
        return yield redisCo.hgetall(key);
    };

    this.sadd = function* (key, value, expire = null) {
        let fields_array = [key];
        if (Array.isArray(value)) {
            fields_array.push.apply(fields_array, value);
        } else {
            fields_array.push(value);
        }
        let res = yield redisCo.sadd(fields_array);
        if (!F.isNull(expire)) {
            yield redisCo.expire(key, expire);
        }
        return res;
    };

    /**
     * 返回集合中包含的所有元素
     * @param key
     * @returns {*}
     */
    this.smembers = function* (key) {
        return yield redisCo.smembers(key);
    }

    /**
     * 返回那些存在于第一个集合，但不存在于其他集合的元素(差集)
     * @param key
     * @returns {*}
     */
    this.sdiff = function* (key) {
        return yield redisCo.sdiff(key);
    }

    this.sinter = function* (keys) {
        return yield redisCo.sinter(keys);
    }

    this.exists = function* (key) {
        return yield redisCo.exists(key);
    }

    this.zadd = function* (key, value, score, expire = null) {
        let res = yield redisCo.ZADD([key, score, value]);
        if (!F.isNull(expire)) {
            res = yield redisCo.expire(key, expire);
        }
        return res;
    }

    this.zrangebyscore = function* (key, min, max) {
        return yield redisCo.zrangebyscore([key, min, max]);
    }

    this.zrem = function* (key, value) {
        let fields_array = [key];
        if (Array.isArray(value)) {
            fields_array.push.apply(fields_array, value);
        } else {
            fields_array.push(value);
        }
        return yield redisCo.zrem(fields_array);
    };

    this.zrank = function* (key, member) {
        return yield redisCo.ZRANK([key, member]);
    }

    this.lpush = function* (key, value, expire = null) {
        let fields_array = [key];
        if (Array.isArray(value)) {
            fields_array.push.apply(fields_array, value);
        } else {
            fields_array.push(value);
        }
        let res = yield redisCo.lpush(fields_array);
        if (!F.isNull(expire)) {
            yield redisCo.expire(key, expire);
        }
        return res;
    }
    /* 查询用户登录记录IP信息 */
    this.getUserLoginIpInfo = function* (userid) {
        let key = _.str.sprintf(C.redisPre.userid_login_ip_info_key, userid);
        let value = yield redisCo.get(key);
        return F.isNull(value) ? '' : JSON.parse(value);
    }

    this.lrem = function* (key, value, count = 1) {
        return yield redisCo.lrem(key, count, value);
    }

    this.llen = function* (key) {
        return yield redisCo.llen(key);
    }

    /* 设置用户登录记录IP信息 */
    this.setUserLoginIpInfo = function* (userid, ip, user_agent, expire = C.session.ttl) {
        let key = _.str.sprintf(C.redisPre.userid_login_ip_info_key, userid);
        let value = {'ip': ip, 'user_agent': user_agent};
        yield redisCo.set(key, JSON.stringify(value));
        yield redisCo.expire(key, expire);
    }


    /****
     * 以下为聚效业务逻辑  以上业务逻辑不再使用 但保留先
     */
    // this.addUniidToBigRoom = function* (uniid) {
    //     var userid_uni_map_key = C.redisPre.userid_uni_map_key;
    //     userid_uni_map_key = _.str.sprintf(userid_uni_map_key, "BigRoom");
    //     var insert_time = F.timestamp();
    //     yield redisCo.ZADD([userid_uni_map_key, insert_time, uniid]);
    // };
    //
    // this.delUniidFromBigRoom = function* (uniid) {
    //     var userid_uni_map_key = C.redisPre.userid_uni_map_key;
    //     userid_uni_map_key = _.str.sprintf(userid_uni_map_key, "BigRoom");
    //     yield redisCo.zrem([userid_uni_map_key, uniid]);
    // };

    // this.getAllSockets = function* () {
    //     var userid_uni_map_key = C.redisPre.userid_uni_map_key;
    //     userid_uni_map_key = _.str.sprintf(userid_uni_map_key, "BigRoom");
    //     return yield redisCo.zrange(userid_uni_map_key, 0, -1);
    // };

    this.setRoomInfo = function* (room_id, room_info) {
        let key = _.str.sprintf(C.redisPre.room_info_key, room_id);
        return yield redisCo.set(key, JSON.stringify(room_info));
    };

    this.getRoomInfo = function* (room_id) {
        let room_uid = yield redisCo.hget(C.redisPre.room_room_id, room_id);
        if (F.isNull(room_uid)) {
            return null;
        }

        // let room_id_key = JSON.parse(roomUid);
        // let key = _.str.sprintf(C.redisPre.uid_type_map_room_info, room_id_key.type);
        let res = yield redisCo.hget(C.redisPre.uid_type_map_room_info,room_uid);
        return F.isNull(res) ? res : JSON.parse(res);
    };

    this.getRoomAudioChannel = function* (room_id) {
        let audioChannel = yield redisCo.hget(C.redisPre.room_audio_channel_key, room_id);
        if (!F.isNull(audioChannel)) {
            return Number(audioChannel);
        }

        audioChannel = yield redisCo.get(C.redisPre.default_audio_channel_key);
        return F.isNull(audioChannel) ? 1 : Number(audioChannel);
    };

    this.setRoomAudioChannel = function* (audio_channel, room_id) {
        if (F.isNull(room_id)) {
            yield redisCo.set(C.redisPre.default_audio_channel_key, audio_channel);
        } else {
            yield redisCo.hset(C.redisPre.room_audio_channel_key, room_id, audio_channel);
        }
    };

    // 设置房间黑名单
    this.setRoomBlacklist = function* (room_id, uid, black_info) {
        let key = _.str.sprintf(C.redisPre.room_blacklist_key, room_id);
        return yield redisCo.hset(key, uid, JSON.stringify(black_info));
    };

    // 删除房间黑名单
    this.delRoomBlacklist = function* (room_id, uid) {
        let key = _.str.sprintf(C.redisPre.room_blacklist_key, room_id);
        return yield redisCo.hdel(key, uid);
    };

    // 获取房间黑名单
    this.getRoomBlacklist = function* (room_id) {
        let key = _.str.sprintf(C.redisPre.room_blacklist_key, room_id);
        let res = yield redisCo.hkeys(key);
        return res;
    };

    // 获取用户是否在房间黑名单
    this.isBlacklist = function* (room_id, uid) {
        let key = _.str.sprintf(C.redisPre.room_blacklist_key, room_id);
        let res = yield redisCo.hget(key, uid);
        return !F.isNull(res);
    };

    // 设置房间禁言
    this.setRoomMute = function* (room_id, uid, mute_info) {
        let key = _.str.sprintf(C.redisPre.room_mute_key, room_id);
        return yield redisCo.hset(key, uid, JSON.stringify(mute_info));
    };

    // 删除房间禁言
    this.delRoomMute = function* (room_id, uid) {
        let key = _.str.sprintf(C.redisPre.room_mute_key, room_id);
        return yield redisCo.hdel(key, uid);
    };

    // 获取房间禁言
    this.getRoomMute = function* (room_id) {
        let key = _.str.sprintf(C.redisPre.room_mute_key, room_id);
        let res = yield redisCo.hkeys(key);
        return res;
    };

    // 获取用户是否在房间禁言
    this.isMute = function* (room_id, uid) {
        let key = _.str.sprintf(C.redisPre.room_mute_key, room_id);
        let res = yield redisCo.hget(key, uid);
        return !F.isNull(res);
    };

    // ==================================管理员==================================//
    // 设置房间管理员
    this.setRoomManager = function* (room_id, uid) {
        let key = _.str.sprintf(C.redisPre.room_manager_key, room_id);
        var insert_time = F.timestamp();
        yield redisCo.ZADD([key, insert_time, uid]);
    };

    // 删除房间管理员
    this.delRoomManager = function* (room_id, uid) {
        let key = _.str.sprintf(C.redisPre.room_manager_key, room_id);
        yield redisCo.zrem([key, uid]);
    };

    // 获取房间管理员
    this.getRoomManager = function* (room_id, uid) {
        let key = _.str.sprintf(C.redisPre.room_manager_key, room_id);
        let score = yield redisCo.ZSCORE([key, uid]);
        return score;
    };

    this.getAllRoomManagers = function* (room_id) {
        let key = _.str.sprintf(C.redisPre.room_manager_key, room_id);
        return yield this.zrange(key, 0, -1);
    };

    // 是否房间管理员
    this.getRole = function* (room_id, uid) {
        let key = _.str.sprintf(C.redisPre.room_manager_key, room_id);
        let score = yield redisCo.ZSCORE([key, uid]);
        return F.isNull(score) ? 0 : 1;
    };
    // ==================================管理员==================================//

    // 记录用户基础信息
    this.setUserInfo = function* (uid, user_info) {
        let key = _.str.sprintf(C.redisPre.user_info_key, uid);
        return yield redisCo.set(key, JSON.stringify(user_info));
    };

    this.getUserInfo = function* (uid) {
        let key = _.str.sprintf(C.redisPre.user_info_key, uid);
        let res = yield redisCo.get(key);
        return F.isNull(res) ? res : JSON.parse(res);
    };
    /******* END ******/

    // 记录用户socket列表
    this.addSocketToUser = function* (uid, socketid) {
        var user_map_socket_key = C.redisPre.user_map_socket_key;
        user_map_socket_key = _.str.sprintf(user_map_socket_key, uid);
        var insert_time = F.timestamp();
        yield redisCo.ZADD([user_map_socket_key, insert_time, socketid]);
    };

    this.getUserSocketList = function* (uid) {
        var user_map_socket_key = C.redisPre.user_map_socket_key;
        user_map_socket_key = _.str.sprintf(user_map_socket_key, uid);
        return yield this.zrange(user_map_socket_key, 0, -1);
    };

    this.delUserSocketList = function* (uid, socketid) {
        var user_map_socket_key = C.redisPre.user_map_socket_key;
        user_map_socket_key = _.str.sprintf(user_map_socket_key, uid);
        yield redisCo.zrem([user_map_socket_key, socketid]);
    };
    /******* END ******/

    // 存某个房间用户列表
    this.addUidToRoom = function* (room_id, uid) {
        var roomid_map_uid_key = C.redisPre.roomid_map_uid_key;
        roomid_map_uid_key = _.str.sprintf(roomid_map_uid_key, room_id);
        var insert_time = F.timestamp();
        //如果是管理员时间戳乘以2
        let is_admin = yield this.getRole(room_id, uid);
        if (is_admin) {
            insert_time = insert_time * 2;
        }
        yield redisCo.ZADD([roomid_map_uid_key, insert_time, uid]);
    };

    this.getRoomUserList = function* (room_id) {
        var roomid_map_uid_key = _.str.sprintf(C.redisPre.roomid_map_uid_key, room_id);
        return yield redisCo.zrange(roomid_map_uid_key, 0, -1)
    };

    /**根据房间id获取成员分页数据*/
    this.getRoomUserPage = function* (room_id, start, limit) {
        let roomid_map_uid_key = _.str.sprintf(C.redisPre.roomid_map_uid_key, room_id);
        return yield redisCo.zrange(roomid_map_uid_key, start, limit);
    };

    this.getRoomUserCount = function* (room_id) {
        let roomid_map_uid_key = _.str.sprintf(C.redisPre.roomid_map_uid_key, room_id);
        let room_count = yield redisCo.zcount([roomid_map_uid_key, '-inf', '+inf']);
        return F.isNull(room_count) ? 0 : room_count;
    };

    this.delRoomUserList = function* (room_id, uid) {
        let roomid_map_uid_key = _.str.sprintf(C.redisPre.roomid_map_uid_key, room_id);
        F.log("err:", "key=%s", [roomid_map_uid_key]);
        yield redisCo.zrem([roomid_map_uid_key, uid]);
    };

    /******* END ******/



    /****** start uid map room ******/
    this.addRoomIdToUid = function* (room_id, uid) {
        var uid_map_roomid_key = C.redisPre.uid_map_roomid_key;
        uid_map_roomid_key = _.str.sprintf(uid_map_roomid_key, uid);
        var insert_time = F.timestamp();
        yield redisCo.ZADD([uid_map_roomid_key, insert_time, room_id]);
    };



    this.delUserRoomList = function* (room_id, uid) {
        let uid_map_roomid_key = _.str.sprintf(C.redisPre.uid_map_roomid_key, uid);
        yield redisCo.zrem([uid_map_roomid_key, room_id]);
    };


    /****** end uid map room ******/

    // 存某个房间socket列表
    this.addSocketToRoom = function* (room_id, socketId) {
        let roomid_map_socket_key = _.str.sprintf(C.redisPre.roomid_map_socket_key, room_id);
        let insert_time = F.timestamp();
        co(redisCo.zremrangebyscore([roomid_map_socket_key, 0, that.boot_time- 10*60]));
        yield redisCo.ZADD([roomid_map_socket_key, insert_time, socketId]);
    };

    this.getRoomSocketList = function* (room_id) {
        var roomid_map_socket_key = C.redisPre.roomid_map_socket_key;
        roomid_map_socket_key = _.str.sprintf(roomid_map_socket_key, room_id);
        return yield this.zrange(roomid_map_socket_key, 0, -1);
    };

    this.getRoomSocketId = function* (room_id, socket_id) {
        let key = _.str.sprintf(C.redisPre.roomid_map_socket_key, room_id);
        let score = yield redisCo.ZSCORE([key, socket_id]);
        return score;
    };

    this.delRoomSocketList = function* (room_id, socketId) {
        var roomid_map_socket_key = C.redisPre.roomid_map_socket_key;
        roomid_map_socket_key = _.str.sprintf(roomid_map_socket_key, room_id);
        yield redisCo.zrem([roomid_map_socket_key, socketId]);
    };
    /******* END ******/

    // 反向记录socket在哪些房间
    this.addRoomidToSocket = function* (socketId, room_id) {
        var socket_map_room_key = C.redisPre.socket_map_room_key;
        socket_map_room_key = _.str.sprintf(socket_map_room_key, socketId);
        var insert_time = F.timestamp();
        yield redisCo.ZADD([socket_map_room_key, insert_time, room_id]);
    };

    this.getSocketRoomList = function* (socketId) {
        let socket_map_room_key = _.str.sprintf(C.redisPre.socket_map_room_key, socketId);
        return yield this.zrange(socket_map_room_key, 0, -1);
    };

    this.delSocketRoomList = function* (socketId, room_id) {
        F.addDebugLogs(["delSocketRoomList:", socketId, room_id]);
        var socket_map_room_key = C.redisPre.socket_map_room_key;
        socket_map_room_key = _.str.sprintf(socket_map_room_key, socketId);
        yield redisCo.zrem([socket_map_room_key, room_id]);
    };
    /******* END ******/

    // 存某个用户+某个房间的socket列表
    this.addSocketToUidRoom = function* (uid, room_id, socketId) {
        var uid_room_map_socket_key = C.redisPre.uid_room_map_socket_key;
        uid_room_map_socket_key = _.str.vsprintf(uid_room_map_socket_key, [uid, room_id]);
        co(redisCo.zremrangebyscore([uid_room_map_socket_key, 0, that.boot_time- 10*60]));
        var insert_time = F.timestamp();
        yield redisCo.ZADD([uid_room_map_socket_key, insert_time, socketId]);
    };

    this.getSocketListByUidAndRoomid = function* (uid, room_id) {
        var uid_room_map_socket_key = C.redisPre.uid_room_map_socket_key;
        uid_room_map_socket_key = _.str.vsprintf(uid_room_map_socket_key, [uid, room_id]);
        return yield this.zrange(uid_room_map_socket_key, 0, -1);
    }

    this.delUidRoomSocketList = function* (uid, room_id, socketId) {
        var uid_room_map_socket_key = C.redisPre.uid_room_map_socket_key;
        uid_room_map_socket_key = _.str.vsprintf(uid_room_map_socket_key, [uid, room_id]);
        yield redisCo.zrem([uid_room_map_socket_key, socketId]);
    };
    /******* END ******/

    //房间队列成员映射
    this.addMemberToQueue = function* (room_id, key, memberid) {
        if (parseInt(key) < -1 || parseInt(key) > 7) F.throwErr("queue key is wrong!");
        var room_map_queue_mem_key = C.redisPre.room_map_queue_mem_key;
        room_map_queue_mem_key = _.str.vsprintf(room_map_queue_mem_key, [room_id]);
        yield redisCo.hmset(room_map_queue_mem_key, key, memberid);
    };

    this.getAllQueueMember = function* (room_id) {
        var room_map_queue_mem_key = C.redisPre.room_map_queue_mem_key;
        room_map_queue_mem_key = _.str.vsprintf(room_map_queue_mem_key, [room_id]);
        let res = yield redisCo.hmget(room_map_queue_mem_key, [-1, 0, 1, 2, 3, 4, 5, 6, 7]);
        if (F.isNull(res)) {
            return [null, null, null, null, null, null, null, null, null];
        }
        return res;
    };

    this.checkInQueue = function* (room_id, uid) {
        let que_list = yield this.getAllQueueMember(room_id);
        F.addDebugLogs(["###getAllQueueMember:",que_list]);
        for (let i = 0; i < que_list.length; i++) {
            let mem_id = que_list[i];
            if (uid == mem_id) return true;
        }
        return false;
    }

    this.delQueueMember = function* (room_id, key) {
        var room_map_queue_mem_key = C.redisPre.room_map_queue_mem_key;
        room_map_queue_mem_key = _.str.vsprintf(room_map_queue_mem_key, [room_id]);
        yield redisCo.hdel(room_map_queue_mem_key, key);
    };
    /******* END ******/

    // 统计房间在线人数
    this.addRoomOnlineNum = function* (room_id) {
        let online_num = yield that.getRoomUserCount(room_id);
        let key = C.redisPre.room_live_sortedset_key;
        F.addDebugLogs(["###addRoomOnlineNum#####:", key, online_num, room_id]);
        
        // zincrby key increment member
        yield redisCo.ZADD([key, online_num, room_id]);
        return online_num;
    };

    this.getRoomOnlineNum = function* (room_id) {
        let key = C.redisPre.room_live_sortedset_key;
        let online_num = yield redisCo.zscore([key, room_id]);
        return Number(online_num);
    };

    this.getRoomIdsByOnlineNum = function* (online_num = 3) {
        let key = C.redisPre.room_live_sortedset_key;
        return yield redisCo.zrangebyscore([key, online_num, '+inf']);
    };

    /******* END ******/
    // 往公聊大厅添加一个socket
    this.addSocketToPublicRoom = function* (room_id, socket_id, uid) {
        let insert_time = F.timestamp();

        let public_roomid_map_socketid_zset_key = _.str.sprintf(C.redisPre.public_roomid_map_socketid_zset_key, room_id);
        co(redisCo.zremrangebyscore([public_roomid_map_socketid_zset_key, 0, that.boot_time- 10*60]));
        yield redisCo.ZADD([public_roomid_map_socketid_zset_key, insert_time, socket_id]);

        let public_roomid_map_uid_zset_key = _.str.sprintf(C.redisPre.public_roomid_map_uid_zset_key, room_id);
        yield redisCo.ZADD([public_roomid_map_uid_zset_key, insert_time, uid]);

        let public_socketid_map_roomid_zset_key = _.str.sprintf(C.redisPre.public_socketid_map_roomid_zset_key, socket_id);
        yield redisCo.ZADD([public_socketid_map_roomid_zset_key, insert_time, room_id]);
    };

    /**
     * 获取公聊大厅的socket列表
     * @param room_id
     * @returns {*}
     */
    this.getPublicRoomSocketList = function* (room_id) {
        let key = _.str.sprintf(C.redisPre.public_roomid_map_socketid_zset_key, room_id);
        return yield this.zrange(key, 0, -1);
    };

    this.getRecentPublicRoomUidList = function* (room_id) {
        let key = _.str.sprintf(C.redisPre.public_roomid_map_uid_zset_key, room_id);
        return yield redisCo.ZREVRANGE([key, 0, 5]);
    };

    this.getPublicRoomUserCount = function* (room_id) {
        let insert_time = F.timestamp();
        let key = _.str.sprintf(C.redisPre.public_roomid_map_uid_zset_key, room_id);
        return yield redisCo.ZCOUNT([key, 0, insert_time]);
    };

    /**
     * 根据当前的socket_id获取所在房间
     * @param socket_id
     * @returns {*}
     */
    this.getPublicRoomIdBySocketId = function* (socket_id) {
        let key = _.str.sprintf(C.redisPre.public_socketid_map_roomid_zset_key, socket_id);
        return yield this.zrange(key, 0, -1);
    };

    /**
     * 用户退出公聊大厅删除缓存
     * @param room_id
     * @param socket_id
     */
    this.delPublicRoomSocket = function* (uid, socket_id) {
        let key = _.str.sprintf(C.redisPre.public_socketid_map_roomid_zset_key, socket_id);
        let room_id_list = yield this.zrange(key, 0, -1);
        let public_socketid_map_roomid_zset_key = _.str.sprintf(C.redisPre.public_socketid_map_roomid_zset_key, socket_id);

        for (let i = 0; i < room_id_list.length; i++) {
            let public_roomid_map_socketid_zset_key = _.str.sprintf(C.redisPre.public_roomid_map_socketid_zset_key, room_id_list[i]);
            yield redisCo.zrem([public_roomid_map_socketid_zset_key, socket_id]);

            let public_roomid_map_uid_zset_key = _.str.sprintf(C.redisPre.public_roomid_map_uid_zset_key, room_id_list[i]);
            yield redisCo.zrem([public_roomid_map_uid_zset_key, uid]);

            yield redisCo.zrem([public_socketid_map_roomid_zset_key, room_id_list[i]]);

        }
    };

    /**
     * 保存公聊大厅的聊天历史记录
     * @param room_id
     * @param custom
     */
    this.addPublicRoomHistory = function* (room_id, custom) {
        let key = _.str.sprintf(C.redisPre.public_room_history_zset_key, room_id);
        let insert_time = F.timestamp();
        custom.insert_time = insert_time;
        yield redisCo.ZADD([key, insert_time, JSON.stringify(custom)]);
        // 保存50条
        let his_count = yield redisCo.ZCOUNT([key, 0, insert_time]);
        if (Number(his_count) > 50) {
            yield redisCo.ZREMRANGEBYRANK([key, 0, Number(his_count) - 50 - 1]);
        }
    };

    this.getPublicRoomHisLis = function* (room_id) {
        let key = _.str.sprintf(C.redisPre.public_room_history_zset_key, room_id);
        return yield redisCo.ZRANGE([key, 0, -1]);
    };

    this.setStreamId = function* (uid,streamid) {
        let key = _.str.sprintf(C.redisPre.stream_uid_map_streamid_key, uid);
        yield redisCo.set(key, streamid);
    }

    this.getStreamId = function* (uid) {
        let key = _.str.sprintf(C.redisPre.stream_uid_map_streamid_key, uid);
        return yield redisCo.get(key);
    }

    this.setStreamSvrId = function* (uid,streamsvrid) {
        let key = _.str.sprintf(C.redisPre.stream_uid_map_streamsvrid_key, uid);
        yield redisCo.set(key, streamsvrid);
    }

    this.getStreamSvrId = function* (uid) {
        let key = _.str.sprintf(C.redisPre.stream_uid_map_streamsvrid_key, uid);
        return yield redisCo.get(key);
    }
 
    this.getGiftList = function* (giftId) {
        return yield redisCo.get("yingtao_gift_all");
    }


    /**
     * 获取管理后台禁言记录
     * @param uid
     */
    this.getAccountBannedRecord = function* (uid) {
        let key = _.str.sprintf(C.redisPre.account_banned_record_hash);
        let cb_data_str = yield redisCo.hget(key,uid);
        return cb_data_str?JSON.parse(cb_data_str):null;
    }
    /******* END ******/

    /***start sms ***/
    this.setSmsCode = function* (phone, smsCode, expire) {
        let key = _.str.sprintf(C.redisPre.sms_mobile_code_string, phone);
        return yield that.setnx(key, expire, smsCode);
    }

    this.getSmsCode = function* (phone) {
        let key = _.str.sprintf(C.redisPre.sms_mobile_code_string, phone);
        return yield redisCo.get(key);
    }
    /***end sms **/

    /***start token uid ***/
    this.setTokenUid = function* (token, uid) {
        let key = _.str.sprintf(C.redisPre.token_uid, token);
        return yield that.setnx(key, 60*60*24*7, uid);
    }

    this.getTokenUidAndExpire = function* (token) {
        let key = _.str.sprintf(C.redisPre.token_uid, token);
        yield redisCo.expire(key, 60*60*24*7);
        return yield redisCo.get(key);
    }

    this.delTokenUid = function* (token) {
        let key = _.str.sprintf(C.redisPre.token_uid, token);
        return yield redisCo.del(key);
    }

    /***end token uid ***/

    /****start award *****/
    this.setAward = function* (missionId, awardList) {
        let key = _.str.sprintf(C.redisPre.award_key, missionId);
        yield redisCo.set(key, JSON.stringify(awardList));
    }

    this.getAward = function* (missionId) {
        let key = _.str.sprintf(C.redisPre.award_key, missionId);
        let res = yield redisCo.get(key);
        if (F.isNull(res)) res = "{}";
        return JSON.parse(res);
    }
    /****end award ****/

    this.addPerHasAward = function* (missionId,uid,item) {
        let key = _.str.vsprintf(C.redisPre.has_award_list_per, [missionId, uid]);
        yield redisCo.RPUSH([key, JSON.stringify(item)]);
    }

    this.getPerHasDrawCount = function* (missionId,uid) {
        let key = _.str.vsprintf(C.redisPre.has_award_list_per, [missionId, uid]);
        return yield redisCo.LLEN([key]);
    }

    this.getPerHasDrawHis = function* (missionId,uid) {
        let key = _.str.vsprintf(C.redisPre.has_award_list_per, [missionId, uid]);
        let res = yield redisCo.LRANGE([key,0,-1]);
        if (F.isNull(res)) res = [];
        for (let i = 0; i < res.length; i++) {
            res[i] = JSON.parse(res[i])
        }
        return res;
    }

    this.addTotalHasAward = function* (missionId,item) {
        let key = _.str.vsprintf(C.redisPre.has_award_list, [missionId]);
        yield redisCo.RPUSH([key, JSON.stringify(item)]);
    }

    this.getTotalHasAward = function* (missionId) {
        let key = _.str.vsprintf(C.redisPre.has_award_list, [missionId]);
        let res = yield redisCo.LRANGE([key,0,-1]);
        if (F.isNull(res)) res = [];
        for (let i = 0; i < res.length; i++) {
            res[i] = JSON.parse(res[i])
        }
        return res;
    }

    this.setTotalHasAward = function* (missionId,index,item) {
        let key = _.str.vsprintf(C.redisPre.has_award_list, [missionId]);
        item = JSON.stringify(item);
        yield redisCo.LSET([key, index , item]);
    }
}

module.exports = redisMgr;
