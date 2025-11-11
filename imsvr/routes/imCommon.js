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

  imapp.onconnect = function* (next, ctx) {
      // on connect
      var cur_time = new Date();
      var socket_id = ctx.socket.id;
      ctx.connect_time = cur_time.getTime();
      F.addLogs("im-svr " + socket_id + " receive connect suc");
      setTimeout(function () {
          if (F.isNull(ctx.uid)) ctx.disconnect();
      },10000); // 10秒后不登录则强制断开socket
  };

  imapp.ondisconnect = function* (next, ctx, reason) {
      // on disconnect
      var userid = ctx.userid;
      var uniid = ctx.uniid;
      var socket_id = ctx.socket.id;
      F.addDebugLogs(["socket disconnect:",uniid,socket_id,reason]);
      if (!F.isNull(ctx.uniid)) {
          yield mgr_map.im.delSvrMap(ctx.uniid,false); //注销im消息管理
          yield mgr_map.room.abnormalClose(ctx.uniid,ctx,ctx.page_name); //异常关闭处理
      }
  };

  imapp.beforeCallback = function* (route, ctx, message) {
      message.req_data = F.rmHtmlAndJsByJson(message.req_data);//进行html编码
      if(message.route != 'heartbeat') {
          F.addOtherLogs("imrw/imrw",['##im receive: uniid: ', ctx.uniid, 'userid: ', ctx.userid, message]);
      }
      if (!F.isNull(ctx.has_del)) { // 已经被删除
          if (route == "kickoff") { // 收到回复才真正删除
              ctx.disconnect();
          }
          return false;
      }

      var allow_route_list = ["login","heartbeat","test","test2"]; // 允许不登录访问的路由
      if (_.v.isIn(message.route,allow_route_list)) return true;
      if (F.isNull(ctx.uid)) F.throwErrCode(10002);
      return true;
  };

  imapp.catchException = function* (route,ctx,message,err) {
      var msg = JSON.stringify(message);
      var err_str = F.log("err:", "#####%s; req:%s; catch a err:%s,code:%s", [route,msg,err.message,err.code]);
      F.addErrLogs([err_str,err.stack]);
      var errno = F.isNull(err.code) || !F.isInt(err.code) ? 10003 : err.code;
      var res = {
          "errno": errno,
          "errmsg": F.isNull(err.use_msg) ? C.err_msg[errno.toString()] : err.message/*, 
          "err_stack": err.stack*/
      }
      if(!F.isNull(err.data)) {
          res.data = err.data;
          res.errdata = err.data;
      }
      yield sendRes(ctx, message, res);
  };

  var sendRes = function* (ctx, message, res) {
    if(!F.isNull(ctx.uniid)) {
      yield mgr_map.im.sendRes(ctx.uniid, message, res);
    }else {
      let send_data = {
        'id':message.id,
        'route':message.route,
        'res_data':res
      }
      imapp.emit(ctx, message.route, send_data);
    }
  }

};

