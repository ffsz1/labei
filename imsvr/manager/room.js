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
    let mgr_map = commonManager.mgr_map;
    let that = this;

    /**
     * 进入房间逻辑  添加房间对应redis 推送进入房间通知
     * @param uid
     * @param socket_id
     * @param room_id
     */
    this.enterRoom = function* (uid, socket_id, room_id) {
        let old_socket_list = yield mgr_map.redis.getSocketListByUidAndRoomid(uid, room_id); // 查找当前用户在这个房间的旧socket
        for (let i = 0; i < old_socket_list.length; i++) {
            yield mgr_map.redis.delRoomSocketList(room_id, old_socket_list[i]); // 删除房间对应旧socket列表记录 socket断开函数也执行
            yield mgr_map.redis.delUidRoomSocketList(uid, room_id, old_socket_list[i]); // 删除指定用户+指定房间旧socket列表记录 socket断开函数也执行
        }

        yield mgr_map.redis.addSocketToRoom(room_id, socket_id);// 添加房间对应socket列表记录
        yield mgr_map.redis.addSocketToUidRoom(uid, room_id, socket_id);//添加uid+房间id对应socket列表记录
        yield mgr_map.redis.addRoomidToSocket(socket_id, room_id);// 反向记录socket在哪个房间
        yield mgr_map.redis.addUidToRoom(room_id, uid);// 添加房间用户列表记录 修改进入时间
        yield mgr_map.redis.addRoomIdToUid(room_id, uid);//
        let online_num = yield mgr_map.redis.addRoomOnlineNum(room_id); // 增加房间在线人数
        // 推送进入房间通知
        let room_socket = yield mgr_map.redis.getRoomSocketList(room_id);
        let room_info = yield  mgr_map.room.getRoomInfo(room_id);
        co(that.syncOnlineNum(room_id, uid, online_num, 1));// 同步房间在线人数
        yield mgr_map.notice.pushChatRoomMemberIn(room_socket, room_info, uid);
        F.addOtherLogs('stat/stat',["enterRoom", new Date().getTime(), uid, room_id]);
    };

    /**
     * 离开房间删除对应redis 发离开房间通知 删除麦redis 发下麦通知
     * @param uid
     * @param uniid
     * @param room_id
     */
    this.leaveRoom = function* (uid, socketId, room_id) {
        //1 删除下麦
        yield mgr_map.roomQueue.delFromRoomQueueByUid(uid, room_id, uid);

        //2 离开房间删除对应redis
        yield mgr_map.redis.delRoomSocketList(room_id, socketId); // 删除房间对应socket列表记录 socket断开函数也执行
        yield mgr_map.redis.delUidRoomSocketList(uid, room_id, socketId); // 删除指定用户+指定房间socket列表记录 socket断开函数也执行
        yield mgr_map.redis.delSocketRoomList(socketId, room_id); // 删除socket对应在房间记录 socket断开函数也执行
        yield mgr_map.redis.delRoomUserList(room_id, uid); // 删除房间对应用户列表
        yield mgr_map.redis.delUserRoomList(room_id, uid); //
        let online_num = yield mgr_map.redis.addRoomOnlineNum(room_id); // 增加房间在线人数

        //3 发离开房间通知
        let user_info = yield mgr_map.member.getJavaUserInfo(uid);
        if (F.isNull(user_info)) F.throwErrCode(100101);
        let room_socket = yield mgr_map.redis.getRoomSocketList(room_id); // 发送离开通知给房间其他用户
        co(that.syncOnlineNum(room_id, uid, online_num, 2));// 同步房间在线人数
        yield mgr_map.notice.pushLeaveRoomNotice(room_socket, room_id, uid, user_info.nick);

        co(mgr_map.extrn.delZegoStream(room_id, uid));
        F.addOtherLogs('stat/stat',["exitRoom", new Date().getTime(), uid, room_id]); 
    };

    this.kickOffRoom = function* (uid, room_id, reason_no = null, reason_msg = null) {
        if (F.isNull(reason_no)) {
            reason_no = 1;
        }
        if (F.isNull(reason_msg)) {
            reason_msg = F.tipsWithParam(1);
        }

        let user_info = yield mgr_map.member.getJavaUserInfo(uid);
        if (F.isNull(user_info)) {
            F.throwErrCode(100101);
        }
        let socket_list = yield mgr_map.redis.getSocketListByUidAndRoomid(uid, room_id);
        if (F.isNull(socket_list)) {
            F.throwErrCode(100201);
        }
        let is_robot = false;
        for (let i = 0; i < socket_list.length; i++) {
            if (socket_list[i].indexOf("robot_") >= 0) {
                is_robot = true;
                continue;
            } else {
                yield that.leaveRoom(uid, socket_list[i], room_id);
            }
        }
        if (!is_robot) yield mgr_map.notice.pushMemberKicked(socket_list, room_id, user_info.account, user_info.nick, user_info.avatar, reason_no, reason_msg);
    };

    /**
     * 剔除房间中所有的成员
     * @param room_id
     * @param reason_no
     * @param reason_msg
     */
    this.kickOffRoomAllUser = function* (room_id, reason_no = null, reason_msg = null) {
        let room_uid_list = yield mgr_map.redis.getRoomUserList(room_id);
        for (let i = 0; i < room_uid_list.length; i++) {
            co(that.kickOffRoom(room_uid_list[i], room_id, F.isNull(reason_no) ? 3 : reason_no, reason_msg));
        }
    };

    /**
     * 离开该用户之前所在的房间
     * @param uid
     */
    this.leavePreRoom = function* (uid) {
        let socket_id_list = yield mgr_map.redis.getUserSocketList(uid);
        for (let i = 0; i < socket_id_list.length; i++) {
            let room_id_list = yield mgr_map.redis.getSocketRoomList(socket_id_list[i]);
            for (let j = 0; j < room_id_list.length; j++) {
                yield mgr_map.room.leaveRoom(uid, socket_id_list[i], room_id_list[j]);
            }
        }
    };

    this.cleanSockAndLeaveRoom = function* (socketId, ctx = {}, page_name = 0, delay_time = 0) {
        try {
	    // 以下处理普通房间
            F.addDebugLogs(["im断线房间处理函数:socketId:>", socketId," delay_time:",delay_time]);
            yield mgr_map.redis.delUserSocketList(ctx.uid, socketId); // 删除用户socket记录
            let room_list = yield mgr_map.redis.getSocketRoomList(socketId); // 获取之前socket在哪些房间
            F.addDebugLogs(["socketCleaner data:", ctx.uid, socketId, room_list]);
            for (let i = 0; i < room_list.length; i++) {
                let room_id = room_list[i];
                yield mgr_map.redis.delRoomSocketList(room_id, socketId); // 删除房间对应socket列表记录
                yield mgr_map.redis.delUidRoomSocketList(ctx.uid, room_id, socketId); // 删除指定用户+指定房间socket列表记录
                yield mgr_map.redis.delSocketRoomList(socketId, room_id); // 删除socket对应在房间记录
            }

            if (!F.isNull(room_list)) {
                // 放入db定时器8秒后执行manager/dbTimer.js 的 socketCleaner  判断是否有新socket过来 如果没有要退出房间 下麦
                yield mgr_map.dbTimer.addDBTimer((10-delay_time) * 1000, 'socketCleaner', {
                    'uid': ctx.uid,
                    'socketId': socketId,
                    'room_list': room_list
                });
            }

            // 以下处理大厅
            F.addDebugLogs(["err:", "im断线房间处理函数,leavePublicRoom:socketId:>%s", socketId]);
            yield mgr_map.publicRoom.leavePublicRoom(ctx.uid, socketId);
        } catch (e) {
            F.addErrLogs(["abnormalClose error:", e.stack]);
        }
    };

    /**
     * im断线处理函数 业务相关逻辑在此处理
     * @param socketId socketID
     * @returns {*}
     */
    this.abnormalClose = function* (socketId, ctx = {}, page_name = 0) {
        let delay_time = 2;
        yield this.cleanSockAndLeaveRoom(socketId,ctx,page_name,0);
        yield F.sleep(delay_time*1000); // 延迟2秒
        yield this.cleanSockAndLeaveRoom(socketId,ctx,page_name,delay_time); // 再处理一次 以防有正在进房间逻辑
    };

    this.markChatRoomManager = function* (room_id, account) {
        let room_info = yield mgr_map.room.getRoomInfo(room_id);
        if (F.isNull(room_info)) {
            F.throwErrCode(100200);
        }

        let isRoomManager = yield mgr_map.validator.isRoomManager(room_id, account);
        if (isRoomManager) {
            F.throwErrCode(100402);
        }

        yield mgr_map.redis.setRoomManager(room_id, account);
        let member = yield mgr_map.member.getJavaUserInfo(account);
        member = yield mgr_map.member.extendMemberInfo(member, room_info);

        let room_socket = yield mgr_map.redis.getRoomSocketList(room_id);
        F.addDebugLogs(["ChatRoomManager room_socket",room_socket])
        co(mgr_map.notice.pushManagerAdd(room_socket, room_id, member));
    };

    this.removeChatRoomManager = function* (room_id, account) {
        let room_info = yield mgr_map.room.getRoomInfo(room_id);
        if (F.isNull(room_info)) {
            F.throwErrCode(100200);
        }

        let is_manager = yield mgr_map.validator.isRoomManager(room_id, account);
        if (!is_manager) {
            F.throwErrCode(100403);
        }

        yield mgr_map.redis.delRoomManager(room_id, account);
        let member = yield mgr_map.member.getJavaUserInfo(account);
        member = yield mgr_map.member.extendMemberInfo(member, room_info);

        let room_socket = yield mgr_map.redis.getRoomSocketList(room_id);
        co(mgr_map.notice.pushManagerRemove(room_socket, room_id, member));
    };

    /**
     * 聊天室黑名单添加通知
     * @param room_id 房间ID
     * @param account  要加入黑名单uid
     * @returns {IterableIterator<*>}
     */
    this.markChatRoomBlackList = function* (room_id, uid, account) {
        let is_blacklist = yield mgr_map.redis.isBlacklist(room_id, account);
        if (is_blacklist) {
            F.throwErrCode(100400);
        }

        let member = yield mgr_map.member.getJavaUserInfo(account);
        if (F.isNull(member)) {
            F.throwErrCode(100404);
        }

        let black_info = {
            'uid': uid,
            'date': new Date().getTime()
        };

        //TODO feimat 包装manager函数
        yield mgr_map.redis.setRoomBlacklist(room_id, account, black_info);
        let room_socket = yield mgr_map.redis.getRoomSocketList(room_id);
        co(mgr_map.notice.pushMarkChatRoomBlackListNotice(room_socket, room_id, member));
        //TODO feimat 包装manager函数
        yield mgr_map.redis.delRoomManager(room_id, account);
        co(mgr_map.notice.pushManagerRemove(room_socket, room_id, member));

        yield mgr_map.room.kickOffRoom(account, room_id, 2, F.tipsWithParam(2));
        // mgr_map.member.kickMember(room_id, account, 2, "拉黑名单踢出");
    };

    /**
     * 聊天室黑名单删除通知
     * @param room_id 房间ID
     * @param account 要删除黑名单uid
     * @returns {IterableIterator<*>}
     */
    this.removeChatRoomBlackList = function* (room_id, account) {
        yield mgr_map.redis.delRoomBlacklist(room_id, account);
    };

    /**
     * 聊天室禁言添加通知
     */
    this.markChatRoomMute = function* (room_id, uid, account) {
        let is_mute = yield mgr_map.redis.isMute(room_id, account);
        if (is_mute) {
            F.throwErrCode(100406);
        }

        let member = yield mgr_map.member.getJavaUserInfo(account);
        if (F.isNull(member)) {
            F.throwErrCode(100404);
        }

        let mute_info = {
            'uid': uid,
            'date': new Date().getTime()
        };

        //TODO feimat 包装manager函数
        yield mgr_map.redis.setRoomMute(room_id, account, mute_info);
        let room_socket = yield mgr_map.redis.getRoomSocketList(room_id);
        co(mgr_map.notice.pushMarkChatRoomMuteNotice(room_socket, room_id, member));
    };

    /**
     * 聊天室禁言删除通知
     */
    this.removeChatRoomMute = function* (room_id, account) {
        let member = yield mgr_map.member.getJavaUserInfo(account);
        if (F.isNull(member)) {
            F.throwErrCode(100404);
        }
        yield mgr_map.redis.delRoomMute(room_id, account);
        let room_socket = yield mgr_map.redis.getRoomSocketList(room_id);
        co(mgr_map.notice.pushRemoveChatRoomMuteNotice(room_socket, room_id, member));
    };

    this.getRoomInfo = function* (room_id) {
        let room_info = yield mgr_map.redis.getRoomInfo(room_id);
        if (!F.isNull(room_info)) {
            room_info.audioChannel = yield mgr_map.redis.getRoomAudioChannel(room_id);
            return room_info;
        }

        let res = yield mgr_map.curl.httpGet(C.java_host, C.java_port, '/room/v3/get', {'roomId': room_id}, null, 'utf8', 'http');
        if (res.result) {
            let json_res = JSON.parse(res.data);
            if (json_res.code != 200) {  // 表示业务逻辑报错
                F.throwErrCode(100200);
            }
            room_info = json_res.data;
        } else { // 表示http超时或返回非200
            F.throwErrCode(100200);
        }

        room_info.audioChannel = yield mgr_map.redis.getRoomAudioChannel(room_id);
        yield mgr_map.redis.setRoomInfo(room_id, room_info);
        return room_info;
    };

    this.createRoomId = function* () {
        let incr = yield mgr_map.redis.getNextReqId("room_id"); // 生成下一个房间号
        let ranInt = 10 + Math.floor(Math.random() * (99 - 10 + 1));
        let room_id = (100000 + incr) * 100 + ranInt;
        return room_id;
    };

    this.syncOnlineNum = function* (room_id, uid, online_num, type) {
        F.addDebugLogs(["syncOnlineNum", room_id, uid, online_num, type]);
        let room_info = yield mgr_map.room.getRoomInfo(room_id);
        if (F.isNull(room_info)) {
            F.addDebugLogs(["syncOnlineNum", "/room/online/sync", "空"]);
            return;
        }
        let res = yield mgr_map.curl.httpPost(C.java_host, C.java_port, '/inner/im/v1/receiveRoomOnlineMsg', {
            'roomUid': room_info.uid,
            'uid': uid,
            'onlineNum': online_num,
            'time': new Date().getTime(),
            'type':type
        }, null, 'utf8', 'http');
        F.addDebugLogs(["syncOnlineNum", "/room/online/sync", res]);
        if (!res.result) {
            F.addErrLogs("[ 同步房间人数报错 ]", res);
        }
    };

    /**
     * 更改声音渠道
     * @param audio_channel
     * @param room_id
     */
    this.changeAudioChannel = function* (audio_channel, room_id) {
        yield mgr_map.redis.setRoomAudioChannel(audio_channel, room_id);
        if (!F.isNull(room_id)) {
            yield that.kickOffRoomAllUser(room_id, 3);
            return;
        }

        let roomid_list = yield mgr_map.redis.getRoomIdsByOnlineNum(1);
        if (F.isNull(roomid_list)) {
            return;
        }

        for (let i = 0; i < roomid_list.length; i++) {
            if (F.isNull(roomid_list[i])) {
                continue;
            }

            co(that.kickOffRoomAllUser(roomid_list[i], 3));
        }
    };

    this.getRoomMemberRole = function* (room_id, uid) {
        let room_info = yield mgr_map.room.getRoomInfo(room_id);
        if (F.isNull(room_info)) {
            F.throwErrCode(100203);
        }

        let is_creator = room_info.uid == uid;
        let is_manager = !F.isNull(yield mgr_map.redis.getRoomManager(room_id, uid));
        let is_member = yield mgr_map.validator.isRoomUser(room_id, uid);
        let is_blacklist = yield mgr_map.redis.isBlacklist(room_id, uid);
        return {
            "is_creator": is_creator,
            "is_manager": is_manager,
            "is_member" : is_member,
            "is_blacklist": is_blacklist
        }
    };

    this.kickoffZego = function* (roomid, uid) {

    }
};

