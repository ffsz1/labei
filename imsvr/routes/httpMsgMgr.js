/**
 * 房间管理http接口
 */

'use strict';
var F = require('../common/function');
var C = require('../config/index');
var _ = require('underscore');
_.str = require('underscore.string');
_.v = require('validator');
var co = require('co');

module.exports = function (app) {
    var mgr_map = app.common_mgr.mgr_map;
    var http_app = app.app;

    /**
     *
     */
    http_app.regPost('/imroom/:apiVer/pushUserMsg', function* (ctx) {
        yield F.checkParamsNull(ctx, "msg_info");
        let msg_info = ctx.I.msg_info;
        yield F.assertParamNull(msg_info, "room_id,uid,custom");
        let uid = msg_info.uid;
        let user_socket_list = yield mgr_map.redis.getUserSocketList(uid);
        co(mgr_map.notice.push(user_socket_list, 'sendMessageReport', msg_info));
        F.setResJson(ctx, 0);
    });

    /**
     * 4.25.http：往单个房间推送自定义消息（Java→IM）军圣
     */
    http_app.regPost('/imroom/:apiVer/pushRoomMsg', function* (ctx) {
        yield F.checkParamsNull(ctx, "msg_info"); // room_info参数不能为空
        let msg_info = ctx.I.msg_info;
        yield F.assertParamNull(msg_info, "room_id,custom"); // room_info里的uid 不能为空
        let room_id = msg_info.room_id;
        if (room_id >= 1 && room_id <= 4) {
            yield mgr_map.publicRoom.sendPublicMsgNotice(msg_info);
        } else {
            let room_socket = yield mgr_map.redis.getRoomSocketList(room_id);
            co(mgr_map.notice.push(room_socket, 'sendMessageReport', msg_info));
        }

        F.setResJson(ctx, 0);
    });

    /**
     * 4.26.http：往所有房间推送自定义消息（Java→IM）军圣
     */
    http_app.regPost('/imroom/:apiVer/pushAllRoomMsg', function* (ctx) {
        yield F.checkParamsNull(ctx, "msg_info"); // room_info参数不能为空
        let msg_info = ctx.I.msg_info;
        let intercept_room_list = ctx.I.intercept_room_list;
        if (F.isNull(intercept_room_list)) {
            intercept_room_list = [];
        }

        let roomid_list = yield mgr_map.redis.getRoomIdsByOnlineNum(1);
        if (F.isNull(roomid_list)) {
            F.addWebErrLogs("[ 发送全服消息 ] 无超过3人以上的房间。")
            F.setResJson(ctx, 0);
        }

        F.log("err", "%s", [roomid_list]);

        for (let i = 0; i < roomid_list.length; i++) {
            if (F.isNull(roomid_list[i])) {
                continue;
            }

            if (F.in_array(roomid_list[i], intercept_room_list)) {
                F.addDebugLogs(["[ 发送全服消息 ]该房间在过滤列表中", intercept_room_list, roomid_list[i]]);
                continue;
            }

            let room_socket = yield mgr_map.redis.getRoomSocketList(roomid_list[i]);
            co(mgr_map.notice.push(room_socket, 'sendMessageReport', msg_info));
        }

        F.setResJson(ctx, 0);
    });
};

