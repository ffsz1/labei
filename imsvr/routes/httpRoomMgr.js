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
var exec = require('child_process').exec;

module.exports = function (app) {
    var mgr_map = app.common_mgr.mgr_map;
    var http_app = app.app;

    /**
     * 4.4.http：创建聊天室接口（Java→IM）
     */
    http_app.regRequest('/imroom/:apiVer/createRoom', function* (ctx) {
        yield F.checkParamsNull(ctx, "room_info"); // room_info参数不能为空
        let room_info = ctx.I.room_info;
        yield F.assertParamNull(room_info, "uid"); // room_info里的uid 不能为空
        let new_room_id = yield mgr_map.room.createRoomId(); // 生成下一个房间号
        room_info.roomId = new_room_id;
        room_info.mute = 0; //TODO 禁言功能
        room_info.audioChannel = yield mgr_map.redis.getRoomAudioChannel(room_info.roomId);
        yield mgr_map.redis.setRoomInfo(room_info.roomId, room_info);
        yield mgr_map.room.createRoomId();
        F.setResJson(ctx, 0, new_room_id);
    });

    /**
     * 4.5.http：修改聊天室接口（Java→IM）
     */
    http_app.regRequest('/imroom/:apiVer/updateRoomInfo', function* (ctx) {
        yield F.checkParamsNull(ctx, "room_info"); // room_info参数不能为空
        let room_info = ctx.I.room_info;
        yield F.assertParamNull(room_info, "uid"); // room_info里的uid 不能为空

        let new_room_id = 0;
        if (!"roomId" in room_info || F.isNull(room_info.roomId)) {
            new_room_id = yield mgr_map.redis.getNextReqId("room_id"); // 生成下一个房间号
            room_info.roomId = new_room_id;
        }

        room_info.mute = 0; //TODO 禁言功能
        room_info.audioChannel = yield mgr_map.redis.getRoomAudioChannel(room_info.roomId);
        yield mgr_map.redis.setRoomInfo(room_info.roomId, room_info);
        let room_socket = yield mgr_map.redis.getRoomSocketList(room_info.roomId);
        yield mgr_map.notice.pushChatRoomUpdatedNotice(room_socket, room_info);
        F.setResJson(ctx, 0, new_room_id);
    });

    /**
     * 4.11.http：获取聊天室基本信息（Client→Server）
     * room_id：房间id string
     * uid: 发请求的用户
     * ticket：发请求的用户ticket
     */
    http_app.regPost('/imroom/:apiVer/fetchRoomInfo', function* (ctx) {
        yield F.checkParamsNull(ctx, "room_id,uid,ticket");
        let room_id = ctx.I.room_id;
        let room_info = yield mgr_map.room.getRoomInfo(room_id);
        if (!F.isNull(room_info)) {
            room_info.onlineNum = yield mgr_map.redis.getRoomOnlineNum(room_id);
        }
        F.setResJson(ctx, 0, room_info);
    });

    /**
     * 4.12.http：获取聊天室成员信息（Client→Server）
     * room_id：房间id string
     * start: 整形 分页用 从哪个位置开始 0表示第一个
     * limit: 整数 分页用 每页个数 默认10
     * uid: 发请求的用户
     * ticket：发请求的用户ticket
     */
    http_app.regPost('/imroom/:apiVer/fetchRoomMembers', function* (ctx) {
        yield F.checkParamsNull(ctx, "room_id, uid, ticket, start,limit");
        let room_id = ctx.I.room_id;
        let start = parseInt(ctx.I.start);
        let limit = parseInt(ctx.I.limit);
        if (F.isNull(limit)) {
            limit = 10;
        }
        let uid_list = yield mgr_map.redis.getRoomUserPage(room_id, start, start + limit);
        let room_info = yield mgr_map.room.getRoomInfo(room_id);
        if (F.isNull(room_info)) {
            F.throwErrCode(100200);
        }

        let member_list = [];
        for (let i = 0; i < uid_list.length; i++) {
            let member = yield mgr_map.member.getJavaUserInfo(uid_list[i]);
            member = yield mgr_map.member.extendMemberInfo(member, room_info);
            member_list.push(member);
        }
        //F.addDebugLogs(["fetchRoomMembers:", member_list]);
        // F.addErrLogs(member_list)
        F.setResJson(ctx, 0, member_list);
    });

    /**
     * 4.14.http：根据ID获取聊天室成员信息（Client→Server）
     * room_id：房间id string
     * accounts：用户id string 示例: '123,345'
     * uid: 发请求的用户
     * ticket：发请求的用户ticket
     */
    http_app.regPost('/imroom/:apiVer/fetchRoomMembersByIds', function* (ctx) {
        yield F.checkParamsNull(ctx, "room_id, uid, ticket, accounts"); // ext参数不能为空
        let room_id = ctx.I.room_id;
        let accounts = ctx.I.accounts;

        let room_uids = yield mgr_map.redis.getRoomUserList(room_id);
        let room_info = yield mgr_map.room.getRoomInfo(room_id);
        if (F.isNull(room_info)) {
            F.throwErrCode(100200);
        }

        let member_list = [];
        if (F.isNull(room_uids)) {
            F.setResJson(ctx, 0, null);
            return;
        }

        for (let i = 0; i < room_uids.length; i++) {
            if (accounts.indexOf(room_uids[i]) == -1) {
                continue;
            }
            let user_info = yield mgr_map.member.getJavaUserInfo(room_uids[i]);
            user_info = yield mgr_map.member.extendMemberInfo(user_info, room_info);
            member_list.push(user_info);
        }

        F.addDebugLogs(["fetchRoomMembersByIds:", member_list]);
        F.setResJson(ctx, 0, member_list);
    });

    /**
     * 4.15.http：添加移除聊天室黑名单（Client→Server）
     * is_add: 整形 1：添加 0：移除
     * room_id：房间id string
     * account：要添加或移除的uid string
     * uid: 发请求的用户
     * ticket：发请求的用户ticket
     */
    http_app.regPost('/imroom/:apiVer/markChatRoomBlackList', function* (ctx) {
        yield F.checkParamsNull(ctx, "room_id, uid, ticket, account, is_add"); // ext参数不能为空
        let room_id = ctx.I.room_id;
        let uid = ctx.I.uid;
        let is_add = ctx.I.is_add;
        let account = ctx.I.account;


        let room_info = yield mgr_map.room.getRoomInfo(room_id);
        if (F.isNull(room_info)) {
            F.throwErrCode(100200);
        }

        let user_info = yield mgr_map.member.getJavaUserInfo(account);
        if (F.isNull(user_info)) {
            F.setResJson(ctx, 0);
        }

        let is_creator = yield mgr_map.validator.isRoomCreator(room_id, uid);
        let is_manager = yield mgr_map.validator.isRoomManager(room_id, uid);
        if (is_creator && is_manager) {
            F.throwErrCode(100405);
        }

        if (Number(is_add) == 0) {
            yield mgr_map.room.removeChatRoomBlackList(room_id, account);
        } else {
            yield mgr_map.room.markChatRoomBlackList(room_id, uid, account);
        }

        F.setResJson(ctx, 0);
    });

    /**
     * 4.18.http：设置/取消聊天室管理员（Client→Server）
     * is_add: 整形 1：添加 0：移除
     * room_id：房间id string
     * account：要添加或移除的uid string
     * uid: 发请求的用户
     * ticket：发请求的用户ticket
     */
    http_app.regPost('/imroom/:apiVer/markChatRoomManager', function* (ctx) {
        yield F.checkParamsNull(ctx, "room_id, uid, ticket, account, is_add"); // ext参数不能为空
        let is_add = ctx.I.is_add;
        let room_id = ctx.I.room_id;
        let account = ctx.I.account;
        let uid = ctx.I.uid;

        //判断房间是否存在
        let room_info = yield mgr_map.room.getRoomInfo(room_id);
        if (F.isNull(room_info)) {
            F.throwErrCode(100200);
        }

        // 判断是否房主
        let is_creator = yield mgr_map.validator.isRoomCreator(room_id, uid);
        if (!is_creator) {
            F.throwErrCode(100405);
        }

        // 判断用户是否存在
        let user_info = yield mgr_map.member.getJavaUserInfo(account);
        if (F.isNull(user_info)) {
            F.throwErrCode(100101);
        }

        if (is_add == 1) {
            yield mgr_map.room.markChatRoomManager(room_id, account);
        } else {
            yield mgr_map.room.removeChatRoomManager(room_id, account);
        }
        F.setResJson(ctx, 0);
    });

    http_app.regGet('/imroom/:apiVer/control', function* (ctx) {
        yield F.checkParamsNull(ctx, "ctl"); // ext参数不能为空
        let ctl = ctx.I.ctl;
        if ("restart" == ctl) {
            var cmdStr = 'pm2 restart all';
            exec(cmdStr, function(err,stdout,stderr){
                if(err) {
                    console.log(ctl+' error:'+stderr);
                } else {
                    console.log(ctl+' suc');
                }
            });
        }
        F.setResJson(ctx, 0);
    });

    /**
     * 4.27.http：增加房间用户禁言接口（Client→Server）
     * is_mute: 是否禁言用户 1：是 0：否
     * room_id：房间id string
     * account：要添加或移除的uid string
     * uid: 发请求的用户
     * ticket：发请求的用户ticket
     */
    http_app.regPost('/imroom/:apiVer/markChatRoomMute', function* (ctx) {
        yield F.checkParamsNull(ctx, "room_id, uid, ticket, account, is_mute"); // ext参数不能为空
        let room_id = ctx.I.room_id;
        let uid = ctx.I.uid;
        let is_mute = ctx.I.is_mute;
        let account = ctx.I.account;

        let room_info = yield mgr_map.room.getRoomInfo(room_id);
        if (F.isNull(room_info)) {
            F.throwErrCode(100200);
        }

        let user_info = yield mgr_map.member.getJavaUserInfo(account);
        if (F.isNull(user_info)) {
            F.setResJson(ctx, 0);
        }

        let is_creator = yield mgr_map.validator.isRoomCreator(room_id, uid);
        let is_manager = yield mgr_map.validator.isRoomManager(room_id, uid);
        if (is_creator && is_manager) {
            F.throwErrCode(100405);
        }

        if (Number(is_mute) == 0) {
            yield mgr_map.room.removeChatRoomMute(room_id, account);
        } else {
            yield mgr_map.room.markChatRoomMute(room_id, uid, account);
        }

        F.setResJson(ctx, 0);
    });

    /**
     * 4.28.http：查询房间用户禁言接口
     * room_id：房间id string
     * uid: 发请求的用户
     * ticket：发请求的用户ticket
     */
    http_app.regPost('/imroom/:apiVer/fetchChatRoomMuteList', function* (ctx) {
        yield F.checkParamsNull(ctx, "room_id, uid, ticket");
        let uid = ctx.I.uid;
        let room_id = ctx.I.room_id;
        let room_info = yield mgr_map.room.getRoomInfo(room_id);
        if (F.isNull(room_info)) {
            F.throwErrCode(100200);
        }

        let is_creator = yield mgr_map.validator.isRoomCreator(room_id, uid);
        let is_manager = yield mgr_map.validator.isRoomManager(room_id, uid);
        if (is_creator && is_manager) {
            F.throwErrCode(100405);
        }

        let uid_list = yield mgr_map.redis.getRoomMute(room_id);
        let member_list = [];
        for (let i = 0; i < uid_list.length; i++) {
            let member = yield mgr_map.member.getJavaUserInfo(uid_list[i]);
            member = yield mgr_map.member.extendMemberInfo(member, room_info);
            member_list.push(member);
        }
        F.setResJson(ctx, 0, member_list);
    });

    /**
     * http：添加机器人
     * room_id：房间id
     * accounts：机器人uid string 示例: '123,345'
     */
    http_app.regRequest('/imroom/:apiVer/addRobot', function* (ctx) {
        yield F.checkParamsNull(ctx, "room_id, accounts"); // ext参数不能为空
        let room_id = ctx.I.room_id;
        let accounts = ctx.I.accounts;
        let room_info = yield mgr_map.room.getRoomInfo(room_id);
        if (F.isNull(room_info)) {
            F.throwErrCode(100200);
        }
        let list = accounts.split(',');
        let suc = [];
        let err = [];
        for (let i = 0; i < list.length; i++) {
            let member = yield mgr_map.member.getJavaUserInfo(list[i]);
            if (F.isNull(member)) {
                err.push(list[i]);
                continue;
            } else {
                suc.push(list[i]);
            }
            // 把该用户的旧socket踢出，目前业务逻辑是 一个用户同一时刻只有一个socket
            let old_socket_list = yield mgr_map.redis.getUserSocketList(list[i]);
            for (let j = 0; j < old_socket_list.length; j++) {
                yield mgr_map.im.delSvrMap(old_socket_list[j]); // 服务器主动断开socket
                yield mgr_map.redis.delUserSocketList(list[i], old_socket_list[j]); // 删除用户socket记录，其实socket断开处理abnormalClose函数也会删除
            }
            let connect_time = new Date().getTime();
            let cur_time_num = yield mgr_map.redis.getNextExpireId(connect_time);
            let uniid = _.str.vsprintf("%s_%s_%s_%s_%s", ['robot', cur_time_num, connect_time, list[i]]); // 就是socketid保证跨服务器唯一
            F.addDebugLogs(["add robot uid:",uniid," old_socket_list:",old_socket_list]);
            yield mgr_map.redis.addSocketToUser(list[i], uniid); // 记录该用户有那些socket
            // 在这里把离开用户之前进的房间
            yield mgr_map.room.leavePreRoom(list[i]);
            yield mgr_map.member.extendMemberInfo(member, room_info);
            yield mgr_map.room.enterRoom(list[i], uniid, room_id); // 执行进入房间函数
        }
        let result = {
            suc: suc,
            err: err
        };
        F.setResJson(ctx, 0, result);
    });

    /**
     * http：删除机器人
     * room_id：房间id
     * accounts：机器人uid string 示例: '123,345'
     */
    http_app.regRequest('/imroom/:apiVer/delRobot', function* (ctx) {
        yield F.checkParamsNull(ctx, "room_id, accounts"); // ext参数不能为空
        let room_id = ctx.I.room_id;
        let accounts = ctx.I.accounts;
        let list = accounts.split(',');
        let suc = [];
        let err = [];
        for (let i = 0; i < list.length; i++) {
            let uniid = yield mgr_map.redis.getUserSocketList(list[i]); // 获取该用户有那些socket
            if (F.isNull(uniid)) {
                err.push(list[i]);
                continue;
            } else {
                suc.push(list[i]);
            }
            yield mgr_map.room.leaveRoom(list[i], uniid, room_id);
        }
        let result = {
            suc: suc,
            err: err
        };
        F.setResJson(ctx, 0, result);
    });

    /**
     * 切换声网即构
     */
    http_app.regGet('/imroom/:apiVer/changeAudioChannel', function* (ctx) {
        yield F.checkParamsNull(ctx, "audio_channel"); // audio_channel参数不能为空
        let audio_channel = ctx.I.audio_channel;
        if (audio_channel != 1 && audio_channel != 2 && audio_channel != 3) {
            F.throwErrCode(100204);
        }

        let room_id = ctx.I.room_id;
        yield mgr_map.room.changeAudioChannel(audio_channel, room_id);
        F.setResJson(ctx, 0);
    });

    /**
     * 获取用户房间内角色
     */
    http_app.regGet('/imroom/:apiVer/getRoomMemberRole', function* (ctx) {
        yield F.checkParamsNull(ctx, "room_id,uid"); // audio_channel参数不能为空
        let room_id = ctx.I.room_id;
        let uid = ctx.I.uid;
        let res = yield mgr_map.room.getRoomMemberRole(room_id, uid);
        F.setResJson(ctx, 0, res);
    });

    /**
     * 检查是否可以推流接口
     */
    http_app.regRequest('/imroom/:apiVer/checkPushAuth',function* (ctx) {
        yield F.checkParamsNull(ctx, "room_id,uid,ticket"); // audio_channel参数不能为空
        let room_id = ctx.I.room_id;
        let uid = ctx.I.uid;
        let ticket = ctx.I.ticket;
        let cache_ticket = yield mgr_map.javaRedis.getTicketByUid(uid);
        if (ticket != cache_ticket) F.throwErrCode(100100);
        let in_que = yield mgr_map.redis.checkInQueue(room_id,uid);
        if (!in_que) {
            //yield mgr_map.roomQueue.delFromRoomQueueByUid(uid,room_id,uid);
            F.throwErrCode(100302);
        }
        F.setResJson(ctx, 0);
    });

    http_app.regRequest('/imroom/:apiVer/createStream',function* (ctx) {
        yield F.checkParamsNull(ctx, "channel_id,publish_id,stream_alias,stream_sid"); // audio_channel参数不能为空
        let room_id = ctx.I.channel_id;
        let uid = ctx.I.publish_id;
        let streamid = ctx.I.stream_alias;
        let streamsvrid = ctx.I.stream_sid;
        yield mgr_map.redis.setStreamId(uid,streamid);
        yield mgr_map.redis.setStreamSvrId(uid,streamsvrid);
        let in_que = yield mgr_map.redis.checkInQueue(room_id,uid);
        F.addDebugLogs(["##########inque",in_que, uid]);
        //if (!in_que && "10045146" != room_id) {
        if (!in_que) {
            yield mgr_map.roomQueue.delFromRoomQueueByUid(uid,room_id,uid);
            co(mgr_map.extrn.delZegoStream(room_id,uid));
            // F.throwErrCode(100302);
        }
        ctx.body = 1;
    });

    http_app.regRequest('/imroom/:apiVer/closeStream',function* (ctx) {
        ctx.body = 1;
    });

};

