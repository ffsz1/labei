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
     * 7.1.进入公屏聊天室（Client→Server）军圣
     */
    imapp.io.route('enterPublicRoom', function* (next, ctx, msg, cb) {
        yield F.imCheckParamsNull(msg.req_data, "room_id");
        let room_id = msg.req_data.room_id;
        let uid = ctx.uid;
        let socket_id = ctx.uniid;
        let his_list = yield mgr_map.publicRoom.enterPublicRoom(room_id, socket_id, uid);
        yield mgr_map.im.sendRes(ctx.uniid, msg, F.packResJson(0, {
            "his_list": his_list
        }));
    });


    /**
     * 7.2.发送公屏聊天室消息（Client→Server）军圣
     */
    imapp.io.route('sendPublicMsg', function* (next, ctx, msg, cb) {
        // 1、校验参数
        yield F.imCheckParamsNull(msg.req_data, "room_id");
        let room_id = msg.req_data.room_id;
        // 2、判断用户是否在房间
        let is_in_public = yield mgr_map.validator.isInPublicRoom(room_id, ctx.uniid);
        if (!is_in_public) {
            yield mgr_map.im.sendRes(ctx.uniid, msg, F.packResJson(100601));
            return;
        }

        //校验是否服务器禁言
        let abr = yield mgr_map.redis.getAccountBannedRecord(ctx.uid);
        if(abr){
            if(abr.bannedType == 0 || abr.bannedType == 3){
                let now = new Date().getTime();
                if(now < abr.limitTime){
                    F.throwErrCodeWithParam(100407,[abr.bannedMinute+'分钟']);
                }
            }
        }

        msg.req_data.sensitive_words = 1;
        let res = yield mgr_map.curl.checkWord(ctx.uid,msg.req_data.custom.data.msg);
        msg.req_data.sensitive_words = res;

        // 4、往房间发送公屏消息
        let ret = yield mgr_map.validator.checkRealName(ctx.uid, 'sendpublic');
        if (ret.code == 200 && msg.req_data.sensitive_words == 1) {
            yield mgr_map.publicRoom.sendPublicMsgNotice(msg.req_data);
            yield mgr_map.im.sendRes(ctx.uniid, msg, F.packResJson(0));
        }
        if (msg.req_data.sensitive_words === 1) {
            yield mgr_map.im.sendRes(ctx.uniid, msg, F.packResErrMsg(ret.code, ret.message));
        }

        if(msg.req_data.sensitive_words == 2){
            // F.throwErrCode(100408);
            //msg.req_data.custom.data.msg = "***";
            yield mgr_map.publicRoom.sendPublicMsgNotice(msg.req_data);
            yield mgr_map.im.sendRes(ctx.uniid, msg, F.packResJson(0));
        }
    });

    /**
     * 7.6. 退出公聊大厅(Client→Server) 军圣
     */
    imapp.io.route('exitPublicRoom', function* (next, ctx, msg, cb) {
        yield F.imCheckParamsNull(msg.req_data, "room_id");
        let room_id = msg.req_data.room_id;
        yield mgr_map.publicRoom.leavePublicRoom(ctx.uid, ctx.uniid, room_id);
        yield mgr_map.im.sendRes(ctx.uniid, msg, F.packResJson(0));
    });
};

