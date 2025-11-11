/**
 * 房间坑位管理http接口
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
     * 5.7.http：Java服务同步修改坑位/麦位接口（Java→IM）
     * @param room_id 房间id
     * @param type 整形 1：更新key 2：删除key
     * @param key 队列下标
     * @param value 坑位信息
     */
    http_app.regPost('/imroom/:apiVer/pushRoomMicUpdateNotice', function* (ctx) {
        // TODO 添加用户是否在房间判断
        yield F.checkParamsNull(ctx, "room_id, type, key, value"); // ext参数不能为空
        let room_id = ctx.I.room_id;
        let type = ctx.I.type;
        let key = ctx.I.key;
        let value = ctx.I.value;
        let room_info = yield mgr_map.room.getRoomInfo(room_id);

        if (F.isNull(room_info)) {
            F.throwErrCode(100200);
        }

        let room_socket = yield mgr_map.redis.getRoomSocketList(room_id);
        // TODO:根据type区分类型
        yield mgr_map.notice.pushQueueMicUpdateNotice(room_socket, room_id, key, value);
        F.setResJson(ctx, 0);
    });

};

