/**
 * 房间队列管理IM接口
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
    let model_mgr = server.model_mgr.model_map;
    let mgr_map = server.common_mgr.mgr_map;

    //server 配置
    let serverIO = server.io;

    /**
     * 5.1. 更新队列元素
     */
    serverIO.route('updateQueue', function* (next, ctx, msg, cb) {
        yield F.imCheckParamsNull(msg.req_data,"room_id,key,uid");
        let operator_uid = ctx.uid;
        let room_id = msg.req_data.room_id;
        let key = msg.req_data.key;
        let uid = msg.req_data.uid;
        yield mgr_map.roomQueue.addToRoomQueue(operator_uid,room_id,key,uid,ctx.appCode);
        yield mgr_map.im.sendRes(ctx.uniid, msg, F.packResJson(0));
    });

    /**
     * 5.3 取出/删除队列元素
     */
    serverIO.route('pollQueue', function* (next, ctx, msg, cb) {
        yield F.imCheckParamsNull(msg.req_data,"room_id,key");
        let operator_uid = ctx.uid;
        let room_id = msg.req_data.room_id;
        let key = msg.req_data.key;
        yield mgr_map.roomQueue.delFromRoomQueue(operator_uid,room_id,key);
        yield mgr_map.im.sendRes(ctx.uniid, msg, F.packResJson(0));
    });

    /**
     * 5.4.	获取队列
     */
    serverIO.route('fetchQueue', function* (next, ctx, msg, cb) {
        yield F.imCheckParamsNull(msg.req_data,"room_id");
        let room_id = msg.req_data.room_id;
        let queue_list = yield mgr_map.roomQueue.getRoomQueueList(room_id);
        yield mgr_map.im.sendRes(ctx.uniid, msg, F.packResJson(0, queue_list));
    });

};
