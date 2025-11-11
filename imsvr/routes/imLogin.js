/**
 * 登录相关im接口
 */

'use strict';
var F = require('../common/function');
var C = require('../config/index');
var _ = require('underscore');
_.str = require('underscore.string');
_.v = require('validator');
var co = require('co');

module.exports = function (app){

    var model_map = app.model_mgr.model_map;
    var mgr_map = app.common_mgr.mgr_map;
    
    var port = C.port + 1;
    var back_svr = _.str.sprintf("http://%s:%s", C.inner_host, port);
    var imapp = app;

    imapp.io.route('unmatchRoute', function* (next,ctx,msg,cb) {
        F.throwErr("route:"+msg.route+" not found");
    });

    /* IM登录--用户 
    * uid
    * ticket
    * page_name socket所在页面 1:ios 2:android 3:ios小程序 4:android小程序
    */
    imapp.io.route('login', function* (next,ctx,msg,cb) {
        yield F.imCheckParamsNull(msg.req_data,"uid,ticket,page_name");
        let uid = msg.req_data.uid;
        let ticket = msg.req_data.ticket;
        let page_name = msg.req_data.page_name;
        let cache_ticket = yield mgr_map.javaRedis.getTicketByUid(uid);
        if (ticket != cache_ticket) F.throwErrCode(100100);
        if (!F.isNull(ctx.uniid)) {
            yield mgr_map.im.sendRes(ctx.uniid, msg, F.packResJson(0,{'sockeid':ctx.uniid}));
            return;
        }

        let member = yield mgr_map.member.getJavaUserInfo(uid); // TODO 从javahttp服务器获取userinfo并转化为member保存到redis

        // 把该用户的旧socket踢出，目前业务逻辑是 一个用户同一时刻只有一个socket
        let old_socket_list = yield mgr_map.redis.getUserSocketList(uid);
        F.addDebugLogs(["old_socket_list:",old_socket_list]);
        for (let i = 0; i < old_socket_list.length; i++) {
            yield mgr_map.im.delSvrMap(old_socket_list[i]); // 服务器主动断开socket
            yield mgr_map.redis.delUserSocketList(uid,old_socket_list[i]); // 删除用户socket记录，其实socket断开处理abnormalClose函数也会删除
        }

        let cur_time_num = yield mgr_map.redis.getNextExpireId(ctx.connect_time);
        ctx.uniid = _.str.vsprintf("%s_%s_%s_%s_%s", [ctx.socket.id,cur_time_num,ctx.connect_time,page_name,uid]); // 就是socketid保证跨服务器唯一
        yield mgr_map.redis.addSocketToUser(uid,ctx.uniid); // 记录该用户有那些socket

        ctx.uid = uid;
        ctx.page_name = page_name;
        ctx.appCode = msg.req_data.appCode;
        yield mgr_map.im.setSvrMap(ctx.uniid,back_svr,ctx); // 注册到redis消息订阅
        yield mgr_map.im.sendRes(ctx.uniid, msg, F.packResJson(0,{'sockeid':ctx.uniid}));
        F.addOtherLogs('stat/stat',["imLogin", new Date().getTime(), uid, page_name]);                                                                      
    });

    /* IM登录--主播
    * token
    * page_name socket所在页面 1主播个人首页 2列表页 3主播后台 4预约等待页 5公开直播间页 6私密直播间页 7移动端
    */
    imapp.io.route('heartbeat', function* (next,ctx,msg,cb) {
        var send_data = {};
        send_data["id"] = parseInt(msg.id);
        send_data["route"] = msg.route;
        send_data["res_data"] = {"errno":0};
        imapp.emit(ctx,msg.route,send_data);
    });

    imapp.io.route('*', function* (next,ctx,msg,cb) {
        F.throwErrCode(10004);
        var res = F.packResJson(0,{"msg":"aaa"});
        yield mgr_map.im.sendRes(ctx.uniid,msg,res);
    });

    imapp.io.route('imLogin/testechoreq', function* (next,ctx,msg,cb) {
        var client_data = "";
	if (!F.isNull(msg.req_data)) client_data = JSON.stringify(msg.req_data); 
        var res = F.packResJson(0,{"msg":"服务器收到"+client_data});
        yield mgr_map.im.sendRes(ctx.uniid,msg,res);
        //yield mgr_map.im.sendReq(ctx.uniid,'clientNotice',{"msg":"这个是服务器主动推消息"});
    });
    imapp.io.route('test', function* (next,ctx,msg,cb) {
        var client_data = "";
	if (!F.isNull(msg.req_data)) client_data = JSON.stringify(msg.req_data); 
        var res = F.packResJson(0,{"msg":"服务器收到"+client_data+" 时间:"+F.fDatetime()});
        yield mgr_map.im.sendRes(ctx.uniid,msg,res);
        //yield mgr_map.im.sendReq(ctx.uniid,'clientNotice',{"msg":"这个是服务器主动推消息"});
    });

};

