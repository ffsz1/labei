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
     * 进入公屏聊天室
     * @param room_id
     * @param socket_id
     */
    this.enterPublicRoom = function* (room_id, socket_id, uid) {
        yield mgr_map.redis.addSocketToPublicRoom(room_id, socket_id, uid);
        let his_list = yield mgr_map.redis.getPublicRoomHisLis(room_id);
        return his_list;
    };

    this.leavePublicRoom = function* (uid, socket_id) {
        F.addDebugLogs(["err:", "退出公聊大厅:socketId:>{}", uid, socket_id]);
        yield mgr_map.redis.delPublicRoomSocket(uid, socket_id);
    };

    /**
     * 发送公屏信息
     * @param room_id
     */
    this.sendPublicMsgNotice = function* (data) {
        let room_socket = yield mgr_map.redis.getPublicRoomSocketList(data.room_id);
        let server_msg_id = yield mgr_map.redis.getNextReqId("server_msg_id");

        data.server_msg_id = server_msg_id;
        if (!F.isNull(data.custom.data)) {
            data.custom.data.server_msg_id = server_msg_id;
        }
        yield mgr_map.redis.addPublicRoomHistory(data.room_id, data.custom);


        co(mgr_map.notice.push(room_socket, 'sendPublicMsgNotice', data));
        // 抄送Java端
        co(mgr_map.curl.httpPostJson(C.java_host, C.java_port, '/publicRoom/v1/pushMsg', data, null, 'utf8', 'http'))
    };

    /**
     * 7.4.获取公聊大厅标题(Client→Server) 军圣
     * @param room_id
     */
    this.getPublicRoomTitle = function* (room_id) {
        let uid_count = yield mgr_map.redis.getPublicRoomUserCount(room_id);
        let res = {
            "count": uid_count + 1000,
            "ulist": []
        };
        let uid_list = yield mgr_map.redis.getRecentPublicRoomUidList(room_id);
        if (F.isNull(uid_list)) {
            return res;
        }

        for (let i = 0; i < uid_list; i++) {
            if (F.isNull(uid_list[i])) {
                continue;
            }
            let member = yield mgr_map.member.getJavaUserInfo(uid_list[i]);
            if (F.isNull(member)) {
                continue;
            }
            res.ulist.push({"avatar": member.avatar});
        }
        return res;
    };
};

