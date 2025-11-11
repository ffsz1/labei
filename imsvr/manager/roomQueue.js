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
    let managerMap = commonManager.mgr_map;
    let that = this;
    var model_map = app.model_mgr.model_map;
    var mgr_map = commonManager.mgr_map;


    /**
     * 加入队列函数
     * @param operator_uid  操作人id
     * @param room_id   房间id
     * @param key   坑位key
     * @param uid   被操作id
     */
    this.addToRoomQueue = function* (operator_uid, room_id, key, uid, appCode) {
        // TODO 添加用户是否在房间判断
        let user_info = yield mgr_map.member.getJavaUserInfo(uid);
        if (F.isNull(user_info)) F.throwErrCode(100101);
        let room_info = yield mgr_map.room.getRoomInfo(room_id);
        if (F.isNull(room_info)) F.throwErrCode(100200);
        let operator_role;// 操作人角色
        if (room_info.uid == operator_uid) {
            operator_role = 9;// 房主
        } else {
            operator_role = yield mgr_map.redis.getRole(room_id, operator_uid);
        }
        let uid_role;// 被操作人角色
        if (room_info.uid == uid) {
            uid_role = 9;// 房主
        } else {
            uid_role = yield mgr_map.redis.getRole(room_id, uid);
        }
        if (uid_role == 9) {
            if (key != -1) {// 房主只能在第一个位置
                F.throwErrCode(100300);
            }
            if (operator_role != 9) {
                F.throwErrCode(100301);
            }
        } else if (uid_role == 1) {
            if (key == -1) {// 其他人不能在第一个位置
                F.throwErrCode(100300);
            }
            if (operator_role != 9 && operator_role != 1) {
                F.throwErrCode(100301);
            }
        } else {
            if (key == -1) {// 其他人不能在第一个位置
                F.throwErrCode(100300);
            }
        }
        if (operator_role != 9 && operator_role != 1) {
            if (operator_uid != uid) { // 普通成员只能操作自己
                F.throwErrCode(100301);
            }
        }
        let lock = {};
        try {
            lock = yield mgr_map.redis.getLock(C.lock_key.room_queue + room_id, 20);
            if (!lock.suc) F.throwErr("get lock for addToRoomQueue timeout!");

            let queue_mem_list = yield mgr_map.redis.getAllQueueMember(room_id);
            if (!F.isNull(queue_mem_list[parseInt(key) + 1]) && queue_mem_list[parseInt(key) + 1] != uid) F.throwErrCode(100303);
            for (let i = 0; i < queue_mem_list.length; i++) {
                let mem_id = queue_mem_list[i];
                if (mem_id == uid) yield mgr_map.redis.delQueueMember(room_id, i - 1); // 把uid 原来所在位置删除
            }
            yield mgr_map.redis.addMemberToQueue(room_id, key, uid);
            co(mgr_map.extrn.addZegoStream(room_id, uid, appCode));
            let room_socket = yield mgr_map.redis.getRoomSocketList(room_id);
            yield mgr_map.notice.pushQueueMemberUpdateNotice(room_socket, room_id, 1, key, uid);
            co(that.pushRoomMicMsg(room_info.uid, uid, 1));
        }
        catch (e) {
            F.addErrLogs(['addToRoomQueue err:', e.stack]);
            throw  e;
        }
        finally {
            yield mgr_map.redis.releaseLock(lock);
        }
        F.addOtherLogs('stat/stat',["updateQueue", new Date().getTime(), uid, room_id, key]); 
    }

    /**
     * 把key上的人从roomid下麦
     * @param operator_uid  操作人id
     * @param room_id   房间id
     * @param key   坑位key
     */
    this.delFromRoomQueue = function* (operator_uid, room_id, key) {
        // TODO 添加用户是否在房间判断
        let queue_mem_list = yield mgr_map.redis.getAllQueueMember(room_id);
        let i = parseInt(key) + 1;
        let uid = queue_mem_list[i];
        if (F.isNull(uid)) F.throwErrCode(100101);
        let room_info = yield mgr_map.room.getRoomInfo(room_id);
        if (F.isNull(room_info)) F.throwErrCode(100200);
        let operator_role;// 操作人角色
        if (room_info.uid == operator_uid) {
            operator_role = 9;// 房主
        } else {
            operator_role = yield mgr_map.redis.getRole(room_id, operator_uid);
        }
        let uid_role;// 被操作人角色
        if (room_info.uid == uid) {
            uid_role = 9;// 房主
        } else {
            uid_role = yield mgr_map.redis.getRole(room_id, uid);
        }
        if (uid_role == 9) {
            if (operator_role != 9) {
                F.throwErrCode(100301);
            }
        } else if (uid_role == 1) {
            if (operator_role != 9 && operator_role != 1) {
                F.throwErrCode(100301);
            }
        }
        if (operator_role != 9 && operator_role != 1) {
            if (operator_uid != uid) { // 普通成员只能操作自己
                F.throwErrCode(100301);
            }
        }
        let lock = 0;
        try {
            lock = yield mgr_map.redis.getLock(C.lock_key.room_queue + room_id, 20);
            if (!lock.suc) F.throwErr("get lock for delFromRoomQueue timeout!");

            yield mgr_map.redis.delQueueMember(room_id, key); // 注意 queue_list 0 对应-1麦位 1对应0麦位。。。
            // TODO:请求即构后台流删除接口

            co(mgr_map.extrn.delZegoStream(room_id, uid));
            let room_socket = yield mgr_map.redis.getRoomSocketList(room_id);
            //4 推送下麦通知
            // 异步执行不阻塞锁
            co(mgr_map.notice.pushQueueMemberUpdateNotice(room_socket, room_id, 2, key, uid));
            co(mgr_map.roomQueue.pushRoomMicMsg(room_info.uid, uid, 2));
        } catch (e) {
            F.addErrLogs(['delFromRoomQueue err:', e.stack]);
            throw  e;
        } finally {
            yield mgr_map.redis.releaseLock(lock);
        }
        F.addOtherLogs('stat/stat',["pollQueue", new Date().getTime(), operator_uid, room_id, key]); 
    }

    /**
     * 把uid从roomid下麦
     * @param operator_uid  操作人id
     * @param room_id   房间id
     * @param uid   下麦uid
     */
    this.delFromRoomQueueByUid = function* (operator_uid, room_id, uid) {
        let queue_list = yield mgr_map.redis.getAllQueueMember(room_id); // 注意 queue_list 0 对应-1麦位 1对应0麦位。。。
        for (let i = 0; i < queue_list.length; i++) {
            let value = queue_list[i];
            if (value == uid) {// 如果在麦上就调用下麦接口
                yield mgr_map.roomQueue.delFromRoomQueue(operator_uid, room_id, i - 1);
            }
        }
    }

    /**
     * 获取房间麦序(麦上成员信息一起返回)
     * @param room_id
     */
    this.getRoomQueueList = function* (room_id) {
        // TODO 添加用户是否在房间判断
        let room_info = yield mgr_map.room.getRoomInfo(room_id);
        if (F.isNull(room_info)) F.throwErrCode(100200);
        let queue_list = [];
        let mic_list = yield mgr_map.javaRedis.getRoomMicList(room_id);
        let member_list = yield mgr_map.redis.getAllQueueMember(room_id);
        if (mic_list.length != member_list.length) F.throwErr("room member and mic info not match!");
        for (let i = 0; i < mic_list.length; i++) {
            let key = i - 1;
            let mic_info = mic_list[i];
            let memberid = member_list[i];
            let member = yield mgr_map.member.getJavaUserInfo(memberid);
            queue_list.push({
                'key': key,
                'value': {
                    'mic_info': mic_info,
                    'member': member
                }
            });
        }
        return queue_list;
    };

    this.pushRoomMicMsg = function* (room_uid, uid, type) {
        F.addDebugLogs(["receiveRoomMicMsg", room_uid, uid, type]);
        let res = yield mgr_map.curl.httpGet(C.java_host, C.java_port, '/inner/im/v1/receiveRoomMicMsg', {
            'roomUid': room_uid,
            'uid': uid,
            'type': type
        }, null, 'utf8', 'http');
        F.addDebugLogs(["receiveRoomMicMsg", "/inner/im/v1/receiveRoomMicMsg", res]);
    };
};

