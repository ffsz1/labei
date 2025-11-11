'use strict';

const F = require('../common/function');
const _ = require('underscore');
_.str = require('underscore.string');
_.v = require('validator');
const co = require('co');
const C = require('../config');

module.exports = function (app, commonManager) {

    let modelMap = app.model_mgr.model_map;
    let managerMap = commonManager.mgr_map;
    var model_map = app.model_mgr.model_map;
    var mgr_map = commonManager.mgr_map;

    let that = this;


    this.sendDingTalk = function* (errMsg) {
        if (!F.isNull(C.isDebug) && true == C.isDebug) return;
        let route = "/robot/send?access_token=cd508442df5317b257c2ab232e602e0d60d10c8629a2588a4763efa2387583cb";
        yield mgr_map.curl.httpPostJson("oapi.dingtalk.com", 443, route, {
            "text": {
                "content": C.inner_host  + "机子, im预警:"+errMsg
            },
            "msgtype": "text",
            "at": {
                "atMobiles": ["13682306032", "18520124838"]
            }
        }, {"Content-Type": "application/json; charset=utf-8"}, 'utf8', 'https');
    }

    /**
     * 向客户端发送通知
     * @param socketList socket列表
     * @param route 路由名
     * @param data 发送的数据
     */
    this.push = function* (socketList, route, data, cb_data = null) {
        if (_.isString(socketList)) {
            socketList = [socketList];
        }
        F.addOtherLogs("imrw/imrw", ["push data:", route, data, " to:", socketList]);
        for (let i in socketList) {
            let socketId = _.isString(socketList[i]) ? socketList[i] : socketList[i].socket_id;
            if (socketId.indexOf("robot_") >= 0) continue;
            yield managerMap.im.sendReq(socketId, route, data, cb_data);
        }
    };

    ///**
    // * 主播即时邀请通知(拒绝+同意)
    // * @param inviteId 邀请IDs
    // * @param anchorId 主播ID
    // * @param roomId 场次ID
    // * @param roomType 场次类型
    // */
    //this.anchorInstanceInviteReply = function* (inviteId, anchorId, roomId = 0, roomType = 0, msg = '') {
    //    //获取用户在邀请页的在线socket(只有一条)
    //    let manSocketList = yield modelMap.live_invitation_socket_log.getOnlineListByInviteId(inviteId);
    //    if (!F.isNull(manSocketList)) {
    //        let anchorBasicInfo = yield modelMap.user.getUserInfoById(anchorId); //主播信息
    //        anchorBasicInfo.avatar_img = F.getFileUrl(anchorBasicInfo.avatar_img);
    //        //没有roomId则为拒绝
    //        let replyType = roomId == 0 ? 0 : 1;
    //        for (let i = 0; i < manSocketList.length; i++) {
    //            //执行推送
    //            co(this.push(manSocketList[i].socket_id, 'imLady/instantInviteReplyNotice', {
    //                reply_type: replyType,
    //                invite_id: manSocketList[i].invitation_id.toString(),
    //                room_id: roomId.toString(),
    //                room_type: roomType,
    //                anchor_id: anchorId,
    //                nick_name: anchorBasicInfo.nick_name,
    //                avatar_img: anchorBasicInfo.avatar_img,
    //                msg: msg
    //            }));
    //        }
    //    }
    //};

    ///**
    // * 发送礼物推送通知
    // * @param data 请求的参数
    // */
    //this.sendGiftNotice = function* (data) {
    //    //推送发礼物通知 - 获取直播间的所有在线socket
    //    if (!F.isNull(data.is_hangout)) { // hangout
    //        if (!F.isNull(data.is_anchor)) { // 主播发送
    //            yield this.anchorSendHangoutGiftNotice(data);
    //        } else { // 观众发送
    //            yield this.manSendHangoutGiftNotice(data);
    //        }
    //    } else { // 非hangout
    //        yield this.sendNormalGiftNotice(data);
    //    }
    //};

    ///**
    // * 通知房间所有人
    // * @param roomid 房间ID
    // * @param route
    // * @param data
    // * @param except_user_arr 不通知的用户ID数组
    // */
    //this.noticeMsgToRoom = function* (roomid, route, data, except_user_arr = new Array()) {
    //    let room_socket_list = yield modelMap.live_room_socket_log.getOnlineListByRoomId(roomid, except_user_arr);
    //    co(that.push(room_socket_list, route, data));
    //};

    ///**
    // * 进入房间通知
    // * @param userid
    // * @param roomid
    // */
    //this.noticeMsgToEnterRoom = function* (userid, roomid, has_ticket = 0) {
    //    // 通知房间所有人
    //    let user_info = yield modelMap.user.getUserInfoById(userid);
    //    if(F.isNull(user_info)) return ;

    //    let rider_info = yield modelMap.backpack_equipment.getCarDrive(userid);
    //    let honor_info = yield modelMap.equipment.getUserHonor(userid);
    //    let data = {
    //        'userid':userid,
    //        'nickname':user_info.nick_name,
    //        'photourl':user_info.avatar_img,
    //        'riderid':F.isNull(rider_info[userid]) ? '' : rider_info[userid].id.toString(),
    //        'ridername':F.isNull(rider_info[userid]) ? '' : rider_info[userid].name,
    //        'riderurl':F.isNull(rider_info[userid]) ? '' : F.getFileUrl(rider_info[userid].icon_img),
    //        'roomid':roomid.toString(),
    //        'fansnum':yield modelMap.live_room.getCurrentUserNum(roomid),
    //        'honor_img': F.isNull(honor_info.icon_img) ? '' : F.getFileUrl(honor_info.icon_img),
    //        'has_ticket':has_ticket
    //    };
    //    let honor = yield modelMap.equipment.getUserHonor(userid);
    //    if (!F.isNull(honor)) data.honor_url = F.getFileUrl(honor.icon_img);
    //    yield that.noticeMsgToRoom(roomid, 'imShare/enterRoomNotice', data);
    //};

    ///**
    // * 离开房间通知
    // * @param userid
    // * @param roomid
    // */
    //this.noticeMsgToLeaveRoom = function* (userid, roomid) {
    //    let user_info = yield modelMap.user.getUserInfoById(userid);
    //    let data = {
    //        'userid':userid,
    //        'nickname':user_info.nick_name,
    //        'photourl':user_info.avatar_img,
    //        'roomid':roomid.toString(),
    //        'fansnum':yield modelMap.live_room.getCurrentUserNum(roomid)
    //    };
    //    yield that.noticeMsgToRoom(roomid, 'imShare/leaveRoomNotice', data);
    //};


    ///**
    // * 推送男士节目状态变更
    // * @param socket_list
    // * @param data  通过manager.roomShow.combineValueThroughShowList()方法获取
    // */
    //this.pushManShowStatusChange = function* (socket_list,data){
    //    co(that.push(socket_list, 'imMan/statusChangeNotice', data));
    //}

    this.pushChatRoomMemberIn = function* (socket_list, room_info, enter_uid) {
        let member = yield mgr_map.member.getJavaUserInfo(enter_uid);

        let room_id = F.isNull(room_info.room_id) ? room_info.roomId : room_info.room_id;
        let online_num = yield mgr_map.redis.getRoomOnlineNum(room_id);
        member = yield mgr_map.member.extendMemberInfo(member, room_info);

        co(that.push(socket_list, 'chatRoomMemberIn', {
            'member': member,
            'online_num': online_num,
            'timestamp': F.timestamp()
        }));
    };

    /**
     * 推送离开房间通知
     * @param socket_list
     * @param room_id
     * @param uid
     * @param nickname
     */
    this.pushLeaveRoomNotice = function* (socket_list, room_id, uid, nickname) {
        let online_num = yield mgr_map.redis.getRoomOnlineNum(room_id);
        co(that.push(socket_list, 'chatRoomMemberExit', {
            'room_id,': room_id,
            'uid': uid,
            'nickname': nickname,
            'online_num': online_num,
            'timestamp': F.timestamp()
        }));
    };

    /**
     * 4.11通知：聊天室信息更新通知(Server->Client)
     * @param socket_list
     * @param room_info
     */
    this.pushChatRoomUpdatedNotice = function* (socket_list, room_info) {
        co(that.push(socket_list, 'ChatRoomInfoUpdated', {
            'room_info': room_info
        }));
    };

    /**
     * 4.15.通知：聊天室黑名单添加通知
     * @param room_id 房间ID
     * @param uid 添加用户ID
     * @param nickname 昵称
     * @param avatar 头像
     * @returns {IterableIterator<*>}
     */
    this.pushMarkChatRoomBlackListNotice = function* (socket_list, room_id, member) {
        co(that.push(socket_list, 'ChatRoomMemberBlackAdd', {
            'room_id': room_id,
            'member': member
        }));
    };

    /**
     * 4.16.通知：聊天室黑名单删除通知
     * @param room_id 房间ID
     * @param uid  删除用户ID
     * @param nickname 昵称
     * @param avatar 头像
     * @returns {IterableIterator<*>}
     */
    this.pushRemoveChatRoomBlackListNotice = function* (socket_list, room_id, member) {
        co(that.push(socket_list, 'ChatRoomMemberBlackRemove', {
            'room_id': room_id,
            'member': member
        }));
    };

    /**
     * 4.18.通知：管理员添加通知
     * @param room_id 房间ID
     * @param uid 添加用户ID
     * @param nickname 昵称
     * @param avatar 头像
     * @returns {IterableIterator<*>}
     */
    this.pushManagerAdd = function* (socket_list, room_id, member) {
        co(that.push(socket_list, 'ChatRoomManagerAdd', {
            'room_id': room_id,
            'member': member
        }));
    }

    /**
     * 4.19.通知：管理员删除通知
     * @param room_id 房间ID
     * @param uid  删除用户ID
     * @param nickname 昵称
     * @param avatar 头像
     * @returns {IterableIterator<*>}
     */
    this.pushManagerRemove = function* (socket_list, room_id, member) {
        co(that.push(socket_list, 'ChatRoomManagerRemove', {
            'room_id': room_id,
            'member': member
        }));
    }

    /**
     * 4.21.通知：踢除特定成员通知
     * @param room_id 房间ID
     * @param uid  删除用户ID
     * @param nickname 昵称
     * @param avatar 头像
     * @returns {IterableIterator<*>}
     */
    this.pushMemberKicked = function* (socket_list, room_id, uid, nickname, avatar, reason_no, reason_msg) {
        co(that.push(socket_list, 'ChatRoomMemberKicked', {
            'room_id': room_id,
            'uid': uid,
            'nickname': nickname,
            'avatar': avatar,
            'reason_no': reason_no,
            'reason_msg': reason_msg
        }));
    };

    /**
     * 4.29.通知：禁言用户添加通知
     */
    this.pushMarkChatRoomMuteNotice = function* (socket_list, room_id, member) {
        co(that.push(socket_list, 'ChatRoomMemberMute', {
            'room_id': room_id,
            'member': member
        }));
    };

    /**
     * 4.30.通知：禁言用户移除通知
     */
    this.pushRemoveChatRoomMuteNotice = function* (socket_list, room_id, member) {
        co(that.push(socket_list, 'ChatRoomMemberMuteCancel', {
            'room_id': room_id,
            'member': member
        }));
    };

    /**
     * 5.5 通知：更新坑位信息
     * @param room_id 房间id
     * @param key 队列下标
     * @param mic_info 坑位信息
     */
    this.pushQueueMicUpdateNotice = function* (socket_list, room_id, key, mic_info) {
        co(that.push(socket_list, 'QueueMicUpdateNotice', {
            'room_id': room_id,
            'key': key,
            'mic_info': mic_info
        }));
    }

    /**
     * 5.6 通知：队列成员被更新通知
     * @param room_id 房间id
     * @param type 整形 1：更新key 2：删除key
     * @param key 队列下标
     * @param uid 下麦的用户id
     */
    this.pushQueueMemberUpdateNotice = function* (socket_list, room_id, type, key, uid) {
        let value = yield mgr_map.member.getJavaUserInfo(uid);
        F.addOtherLogs("imrw/imrw", ["push data to:", socket_list, uid]);
        co(that.push(socket_list, 'QueueMemberUpdateNotice', {
            'room_id': room_id,
            'type': type,
            'key': key,
            'value': value
        }));
    };

};
