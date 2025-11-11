/**
 * 房间管理http接口
 */

'use strict';
var F = require('../common/function');
var _ = require('underscore');
_.str = require('underscore.string');
_.v = require('validator');
var co = require('co');

module.exports = function (app) {
    var mgr_map = app.common_mgr.mgr_map;
    var http_app = app.app;

    /**
     * 7.4.获取公聊大厅标题(Client→Server) 军圣
     */
    http_app.regPost('/imroom/:apiVer/publicTitle', function* (ctx) {
        yield F.checkParamsNull(ctx, "room_id");
        let room_id = ctx.I.room_id;
        let res = yield mgr_map.publicRoom.getPublicRoomTitle(room_id);
        F.setResJson(ctx, 0, res);
    });
};

