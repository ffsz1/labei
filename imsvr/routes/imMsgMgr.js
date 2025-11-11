/**
 * 房间消息管理IM接口
 */

'use strict';
const F = require('../common/function');
const C = require('../config/index');
const _ = require('underscore');
_.str = require('underscore.string');
_.v = require('validator');
const co = require('co');

module.exports = function (server) {
    //model、manager Map对象
    let managerMap = server.common_mgr.mgr_map;
    let mgr_map = managerMap;
    //server 配置
    let imapp = server;
    imapp.giftList = [];

    /**
     * 6.1 发送非文本消息（Client -> Server）
     */
    imapp.io.route('sendMessage', function* (next, ctx, msg, cb) {
        yield F.imCheckParamsNull(msg.req_data, "room_id");
        try {
            if (imapp.giftList.length == 0) {
                imapp.giftList = yield mgr_map.redis.getGiftList();
                imapp.giftList = JSON.parse(imapp.giftList);
            }
        } catch (e) {
        F.addOtherLogs('lili/lili',["exception", e.stack]);
        }

        let isTiYan = false;
        let bigGift = false;

        let custom = {};
        try {
            custom = JSON.parse(msg.req_data.custom);
        } catch(e) {
            custom = msg.req_data.custom;
        }

        if (custom.first == 3) {
        F.addOtherLogs('lili/lili',["giftid", custom.data.giftId]);
for(let j = 0; j < imapp.giftList.length; j++) {
    let giftinfo = imapp.giftList[j];
    if (custom.data.giftId == giftinfo.giftId && giftinfo.giftName.indexOf('体验') >= 0) {isTiYan = true; break;}
}
        }

        if (custom.first == 16) {
            if (custom.data.params.giftName.indexOf('体验') >= 0) {
                F.addOtherLogs('lili/lili',["draw", custom.data.params.giftName]);
                isTiYan = true;
            }
            if (custom.data.params.giftName.indexOf('月2222') >= 0 || 
                custom.data.params.giftName.indexOf('恋恋爱神') >= 0 ||
                custom.data.params.giftName.indexOf('冰雪城堡') >= 0) {
                bigGift = true;
            }
        }






        let room_id = msg.req_data.room_id;
        let socket_id = ctx.uniid;
        let is_in_room = mgr_map.validator.isRoomSocket(room_id, socket_id);
        if (!is_in_room) {
            F.throwErrCode(100201);
        }

        let room_socket = yield mgr_map.redis.getRoomSocketList(room_id);
        let new_room_socket = [];
        if (isTiYan == true) {
            if (bigGift) {
                for (let j = 0; j < room_socket.length; j++) {
                    new_room_socket.push(room_socket[j]);
                }
            } else {
                for (let j = 0; j < room_socket.length; j++) {
                    if (room_socket[j].indexOf("_"+ctx.uid) >= 0) new_room_socket.push(room_socket[j]);
                }
            }
            msg.req_data.isTiyan = 1;
            msg.req_data.svrUid = ctx.uid;
            co(mgr_map.notice.push(new_room_socket, 'sendMessageReport', msg.req_data));
        } else {
            msg.req_data.isTiyan = 0;
            msg.req_data.svrUid = ctx.uid;
            co(mgr_map.notice.push(room_socket, 'sendMessageReport', msg.req_data));
        }
        yield mgr_map.im.sendRes(ctx.uniid, msg, F.packResJson(0));
    });

    /**
     * 6.3 发送文本消息（Client -> Server）
     */
    imapp.io.route('sendText', function* (next, ctx, msg, cb) {
        yield F.imCheckParamsNull(msg.req_data, "room_id");
        let room_id = msg.req_data.room_id;
        let socket_id = ctx.uniid;
        let is_in_room = mgr_map.validator.isRoomSocket(room_id, socket_id);
        if (!is_in_room) {
            F.throwErrCode(100201);
        }

        //校验是否服务器禁言
        let abr = yield mgr_map.redis.getAccountBannedRecord(ctx.uid);
        if(abr){
            if(abr.bannedType == 0 || abr.bannedType == 1){
                let now = new Date().getTime();
                if(now < abr.limitTime){
                    F.throwErrCodeWithParam(100407,[abr.bannedMinute+'分钟']);
                }
            }
        }
        msg.req_data.sensitive_words = 1;
        let res = yield mgr_map.curl.checkWord(ctx.uid,msg.req_data.content);
        msg.req_data.sensitive_words = res;

        let member = yield mgr_map.member.getJavaUserInfo(ctx.uid);
        msg.req_data.member = member;

        //检测是否开启实名认证
        let ret = yield mgr_map.validator.checkRealName(ctx.uid, 'sendtext');
        let result;
        if (ret.code == 200 && msg.req_data.sensitive_words == 1) {
            let room_socket = yield mgr_map.redis.getRoomSocketList(room_id);
            co(mgr_map.notice.push(room_socket, 'sendTextReport', msg.req_data));
            result = F.packResJson(0);
        } else if(msg.req_data.sensitive_words === 1) {
            result = F.packResErrMsg(ret.code, ret.message);
        }

        if (msg.req_data.sensitive_words === 1) {
            yield mgr_map.im.sendRes(ctx.uniid, msg, result);
        }
        if(msg.req_data.sensitive_words == 2){
            // F.throwErrCode(100408);
            //msg.req_data.content = "***";
            let room_socket = yield mgr_map.redis.getRoomSocketList(room_id);
            co(mgr_map.notice.push(room_socket, 'sendTextReport', msg.req_data));
            result = F.packResJson(0);
        }
    });

};
