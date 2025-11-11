/**
 * 房间管理im接口
 */

'use strict';
var F = require('../common/function');
var C = require('../config/index');
var _ = require('underscore');
_.str = require('underscore.string');
_.v = require('validator');

module.exports = function (app) {

    var model_map = app.model_mgr.model_map;
    var mgr_map = app.common_mgr.mgr_map;

    var imapp = app;

    /**
     * 4.7.进入聊天室
     */
    imapp.io.route('enterChatRoom', function* (next, ctx, msg, cb) {
        yield F.imCheckParamsNull(msg.req_data, "room_id");
        let room_id = msg.req_data.room_id;

        let room_info = yield mgr_map.room.getRoomInfo(room_id);
        if (F.isNull(room_info)) {
            F.throwErrCode(100200);
        }

        // 鉴权
        let is_in_black_list = yield mgr_map.redis.isBlacklist(room_id, ctx.uid);
        if (is_in_black_list) F.throwErrCode(100202);

        // 在这里把离开用户之前进的房间
        yield mgr_map.room.leavePreRoom(ctx.uid);

        let member = yield mgr_map.member.getJavaUserInfo(ctx.uid);
        member = yield mgr_map.member.extendMemberInfo(member, room_info);

        yield mgr_map.room.enterRoom(ctx.uid, ctx.uniid, room_id); // 执行进入房间函数
        let online_user_count = yield mgr_map.redis.getRoomUserCount(room_id);
        room_info.onlineNum = online_user_count;  // 实时取im在线人数

        if (room_info.uid == ctx.uid) { // 说明是房主自己进房间
            yield mgr_map.roomQueue.addToRoomQueue(ctx.uid, room_id, -1, ctx.uid, ctx.appCode);
        }

        let queue_list = yield mgr_map.roomQueue.getRoomQueueList(room_id);
        let res = {
            'room_info': room_info,
            'member': member,
            'queue_list': queue_list,
            'mic_info': ''
        };
        yield mgr_map.im.sendRes(ctx.uniid, msg, F.packResJson(0, res));
    });


    /**
     * 4.9.退出聊天室
     */
    imapp.io.route('exitChatRoom', function* (next, ctx, msg, cb) {
        yield F.imCheckParamsNull(msg.req_data, "room_id");
        let room_id = msg.req_data.room_id;

        yield mgr_map.room.leaveRoom(ctx.uid, ctx.uniid, room_id);
        yield mgr_map.im.sendRes(ctx.uniid, msg, F.packResJson(0));
    });

};

