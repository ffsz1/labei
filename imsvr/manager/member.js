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
    let modelMap = app.model_mgr.model_map;
    var model_map = app.model_mgr.model_map;
    var mgr_map = commonManager.mgr_map;

    /**
     * 扩展用户的房间成员信息
     * @param member 用户基本信息
     * @param room_info 房间信息
     */
    this.extendMemberInfo = function* (member, room_info) {
        let room_id = F.isNull(room_info.room_id)?room_info.roomId:room_info.room_id;
        let uid = member.account;

        member.is_online = true;
        member.is_mute = yield mgr_map.redis.isMute(room_id, uid);
        member.is_creator = member.account == room_info.uid;

        let reg_timestamp = F.timestamp(0) - (F.isNull(member.create_time) ? 0 : member.create_time);
        //259200000 = 3*24*60*60*1000
        member.is_new_user = reg_timestamp < 7*24*60*60*1000;
        // 管理员查询
        member.is_manager = yield mgr_map.validator.isRoomManager(room_id, uid);

        let is_black_list = yield mgr_map.redis.isBlacklist(room_id, uid);
        member.is_black_list = is_black_list;
        member.enter_time = F.getTimeStampByDelta();
        return member;
    };

    this.getJavaUserInfo = function* (uid) {
        if (F.isNull(uid)) return null;
        let member = yield mgr_map.redis.getUserInfo(uid); // 先从redis获取 如果存在直接返回
        if (!F.isNull(member)) {
            return member;
        }
        // 否则到java服务器取用户信息
        let user_info = {};
        let get_res = yield mgr_map.curl.httpGet(C.java_host, C.java_port, '/user/v5/get', {'uid': uid}, null, 'utf8', 'http');  //  TODO 替换成java
        if (get_res.result) { // 表示http成功
            let get_user_res = JSON.parse(get_res.data);
            if (get_user_res.code != 200) {  // 表示业务逻辑报错
                F.throwErrCode(100101);
            }
            user_info = get_user_res.data;
        } else { // 表示http超时或返回非200
            F.throwErrCode(100101);
        }
        member = {};
        member.uid = user_info.uid;
        member.account = user_info.uid;
        member.nick = user_info.nick;
        member.avatar = user_info.avatar;
        member.headwear_url = user_info.headwearUrl;
        member.headwear_name = user_info.headwearName;
        member.car_url = user_info.carUrl;
        member.car_name = user_info.carName;
        member.exper_level = user_info.experLevel;
        member.charm_level = user_info.charmLevel;
        member.gender = user_info.gender;
        member.defUser = user_info.defUser;
        member.create_time = user_info.createTime;
        member.defUser = user_info.defUser;

        yield mgr_map.redis.setUserInfo(uid, member);
        return member;
    };

    /**
     * 查询房间管理员列表
     * @param room_id
     */
    this.fetchRoomManagers = function* (room_id) {
        let manager_uid_list = yield mgr_map.redis.getAllRoomManagers(room_id);
        let managers = [];
        if (F.isNull(manager_uid_list)) {
            return managers;
        }

        for (let i = 0; i < manager_uid_list.length; i++) {
            let manager = yield mgr_map.member.getJavaUserInfo(manager_uid_list[i]);
            //member = yield mgr_map.member.extendMemberInfo(member, room_info);
            managers.push(manager);
        }
        return managers;
    };

    /**
     * 查询房间黑名单列表
     * @param room_id
     */
    this.fetchRoomBlackList = function* (room_id) {
        let blacklist_uids = yield mgr_map.redis.getRoomBlacklist(room_id);
        let blacklist = [];
        if (F.isNull(blacklist_uids)) {
            return blacklist;
        }

        for (let i = 0; i < blacklist_uids.length; i++) {
            let member = yield mgr_map.member.getJavaUserInfo(blacklist_uids[i]);
            blacklist.push(member);
        }
        return blacklist;
    };

    // this.kickMember = function* (room_id, account, reason_no, reason_msg) {
    //     let user_info = yield mgr_map.member.getJavaUserInfo(account);
    //     if (F.isNull(user_info)) {
    //         F.throwErrCode(100101);
    //     }
    //
    //     let socket_id = yield mgr_map.redis.getSocketListByUidAndRoomid(account, room_id);
    //     if (F.isNull(socket_id)) {
    //         F.throwErrCode(100201);
    //     }
    //
    //
    //     //1 删除下麦
    //     yield mgr_map.roomQueue.delFromRoomQueueByUid(account, room_id, account);
    //
    //     //2 离开房间删除对应redis
    //     yield mgr_map.redis.delRoomSocketList(room_id, socket_id); // 删除房间对应socket列表记录 socket断开函数也执行
    //     yield mgr_map.redis.delUidRoomSocketList(account, room_id, socket_id); // 删除指定用户+指定房间socket列表记录 socket断开函数也执行
    //     yield mgr_map.redis.delSocketRoomList(socket_id, room_id); // 删除socket对应在房间记录 socket断开函数也执行
    //     yield mgr_map.redis.delRoomUserList(room_id, account); // 删除房间对应用户列表
    //     yield mgr_map.redis.reduceRoomOnlineNum(room_id); // 减去对应房间的人数
    //
    //     // let room_socket = yield mgr_map.redis.getRoomSocketList(room_id);
    //     let room_socket = [ socket_id ];
    //     co(mgr_map.notice.pushMemberKicked(room_socket, room_id, user_info.uid, user_info.nick, user_info.avatar, reason_no, reason_msg));
    // }
};

