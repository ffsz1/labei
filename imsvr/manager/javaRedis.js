'use strict';
var redisLib = require('../libs/redis');


/**
 * 连接旧javaredis库使用的
 * @type {javaRedisCo}
 */
var redisCo = redisLib.javaRedisCo;

var C = require('../config');
var F = require('../common/function');
var _ = require('underscore');
_.str = require('underscore.string');
_.v = require('validator');

function redisMgr(app, common_mgr) {

    var model_map = app.model_mgr.model_map;
    var mgr_map = common_mgr.mgr_map;

    var that = this;


    /****
     * 注意该manager只能操作旧java的redis库
     */

    /**
     * 检查ticket
     * @param uid
     * @returns {*}
     */
    this.getTicketByUid = function* (uid) {
        var key = C.redisPre.uid_ticket;
        var res = yield redisCo.hmget(key, [uid]);
        return res;
    }

    /**
     * 获取房间坑位信息
     */
    this.getRoomMicList = function* (room_id) {
        let room_info = yield mgr_map.room.getRoomInfo(room_id);
        if (F.isNull(room_info)) {
            F.throwErrCode(100200);
        }

        let room_mic_list = C.redisPre.room_mic_list;
        room_mic_list = _.str.vsprintf(room_mic_list, [room_info.uid]);
        let mic_key_list = [-1, 0, 1, 2, 3, 4, 5, 6, 7];
        let mic_info_list = yield redisCo.hmget(room_mic_list, mic_key_list);

        for (let i = 0; i < mic_info_list.length; i++) {
            if (!F.isNull(mic_info_list[i])) {
                mic_info_list[i] = JSON.parse(mic_info_list[i]);
                continue;
            }

            mic_info_list[i] = {"position": mic_key_list[i], "posState": 0, "micState": 0}
            yield redisCo.hmset(room_mic_list, mic_key_list[i], JSON.stringify(mic_info_list[i]));
        }
        return mic_info_list;
    }

}

module.exports = redisMgr;
