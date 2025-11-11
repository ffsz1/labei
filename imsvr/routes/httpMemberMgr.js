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
     * 4.3.http：同步用户信息接口（Java→IM）
     */
    http_app.regPost('/imroom/:apiVer/updateMemberInfo', function* (ctx) {
        yield F.checkParamsNull(ctx, "user_info");
        let user_info = ctx.I.user_info;
        yield F.assertParamNull(user_info, "uid");
        let member = {};
        member.account = user_info.uid;
        member.nick = user_info.nick;
        member.avatar = user_info.avatar;
        member.gender = user_info.gender;
        member.headwear_url = user_info.headwearUrl;
        member.headwear_name = user_info.headwearName;
        member.car_url = user_info.carUrl;
        member.car_name = user_info.carName;
        member.exper_level = user_info.experLevel;
        member.charm_level = user_info.charmLevel;
        member.create_time = user_info.createTime;
        member.defUser = user_info.defUser;

        yield mgr_map.redis.setUserInfo(user_info.uid, member);
        F.setResJson(ctx, 0);
    });

    /**
     * 4.21.http：踢除特定成员（Client→Server）
     * room_id：房间id string
     * account：要踢除的uid string
     * uid: 发请求的用户
     * ticket：发请求的用户ticket
     */
    http_app.regPost('/imroom/:apiVer/kickMember', function* (ctx) {
        yield F.checkParamsNull(ctx, "room_id, account, uid, ticket"); // ext参数不能为空
        let room_id = ctx.I.room_id;
        let uid = ctx.I.uid;
        let account = ctx.I.account;

        let is_creator = yield mgr_map.validator.isRoomCreator(room_id, uid);
        let is_manager = yield mgr_map.validator.isRoomManager(room_id, uid);
        if (!is_creator && !is_manager) {
            F.throwErrCode(100405);
        }

        yield mgr_map.room.kickOffRoom(account,room_id);
        F.setResJson(ctx, 0);
    });

    /**
     * 4.23.http：查询房间管理员列表（Client→Server）军圣
     */
    http_app.regPost('/imroom/:apiVer/fetchRoomManagers', function* (ctx) {
        yield F.checkParamsNull(ctx, "room_id,uid,ticket");
        let room_id = ctx.I.room_id;
        let managers = yield mgr_map.member.fetchRoomManagers(room_id);
        F.setResJson(ctx, 0, managers);
    });

    /**
     * 4.24.http：查询房间黑名单列表（Client→Server）军圣
     */
    http_app.regPost('/imroom/:apiVer/fetchRoomBlackList', function* (ctx) {
        yield F.checkParamsNull(ctx, "room_id,uid,ticket");
        let room_id = ctx.I.room_id;
        let managers = yield mgr_map.member.fetchRoomBlackList(room_id);
        F.setResJson(ctx, 0, managers);
    });
};

