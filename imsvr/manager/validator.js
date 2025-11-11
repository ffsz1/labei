/**
 * 直播间男女端共用Manager
 */
'use strict';
const F = require('../common/function');
const C = require('../config');
const _ = require('underscore');
_.str = require('underscore.string');
_.v = require('validator');
const co = require('co');

module.exports = function (app, commonManager) {
    var mgr_map = commonManager.mgr_map;

    /**
     * 判断用户是否在房间，true，则在房间，false，则不在
     * @param room_id
     * @param uid
     * @returns {boolean}
     */
    this.isRoomUser = function* (room_id, uid) {
        let roomid_map_uid_key = _.str.sprintf(C.redisPre.roomid_map_uid_key, room_id);
        let rank = yield mgr_map.redis.zrank(roomid_map_uid_key, uid);
        return !F.isNull(rank);
    };

    /**
     * 判断该socket是否在房间中
     * @param room_id
     * @param socket_id
     * @returns {boolean}
     */
    this.isRoomSocket = function* (room_id, socket_id) {
        let score = yield mgr_map.redis.getRoomSocketId(room_id, socket_id);
        return !F.isNull(score);
    };

    /**
     * 判断用户是否房主
     * @param room_id
     * @param uid
     * @returns {boolean}
     */
    this.isRoomCreator = function* (room_id, uid) {
        let room_info = yield mgr_map.room.getRoomInfo(room_id);
        if (F.isNull(room_info)) {
            F.throwErrCode(100203);
        }

        return room_info.uid == uid;
    };

    /**
     * 判断用户是否管理员
     * @param room_id
     * @param uid
     * @returns {boolean}
     */
    this.isRoomManager = function* (room_id, uid) {
        let room_manager = yield mgr_map.redis.getRoomManager(room_id, uid);
        return !F.isNull(room_manager);
    };

    /**
     *
     * @param room_id
     * @param uid
     * @returns {boolean}
     */
    this.isRoomBlacklist = function* (room_id, uid) {
        return yield mgr_map.redis.isBlacklist(room_id, uid);
    };

    this.isInPublicRoom = function* (room_id, socket_id) {
        let socket_room_id = yield mgr_map.redis.getPublicRoomIdBySocketId(socket_id);
        return room_id == socket_room_id;
    };

    /**
     * 校验实名认证
     */
    this.checkRealName = function* (uid, type) {
        let get_res = yield mgr_map.curl.httpGet(C.java_host, C.java_port, '/user/realname/v1/getUserRealNameStatus', {
            'uid': uid,
            'type': type
        }, null, 'utf8', 'http');
        if (!get_res.result) {
            F.throwErrCode(100101);
        }
        return JSON.parse(get_res.data);
    };
};

