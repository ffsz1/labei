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

    http_app.regRequest('/iminnerapi/:apiVer/sendMsg', function* (ctx) {
        yield F.checkParamsNull(ctx, "socket_list,content,route");
        let socket_list = ctx.I.socket_list;
        let content = JSON.parse(ctx.I.content); // 不捕获异常了 返回什么错误就什么错误
        let route = ctx.I.route;
    });

    http_app.regRequest('/imroom/:apiVer/delZegoStream', function* (ctx) {
        yield F.checkParamsNull(ctx, "room_id,uid");
        let room_id = ctx.I.room_id;
        let uid = ctx.I.uid;
        yield mgr_map.extrn.delZegoStream(room_id, uid);
        F.setResJson(ctx, 0);
    });
};

