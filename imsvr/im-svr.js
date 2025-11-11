'use strict';

var koa = require('koa.io');
var rpcWithExpress = require('./libs/rpcWithExpress');
var wordFilter = require('./libs/wordfilter');
var jsonp = require('koa-safe-jsonp');
var router = require('koa-router');
var bodyParser = require('koa-better-body');
var _ = require('underscore');
_.str = require('underscore.string');
_.v = require('validator');
var gzip = require('./libs/mygzip');//压缩页面
var session = require('koa-generic-session');
var redisStore = require('koa-redis');
var cors = require('koa-cors');

var F = require('./common/function');
var C = require('./config');
var apiPre = C.apiPre;
var co = require('co');

var modelMgr = require('./manager/model');
var commonMgr = require('./manager/common');

var app = new rpcWithExpress();
var port = C.socketio_port;
app.port = port;

app.model_mgr = new modelMgr();
app.common_mgr = new commonMgr(app);

co(app.common_mgr.mgr_map.im.clearLock());
co(app.common_mgr.mgr_map.im.clearSocket());

var im_common = require('./routes/imCommon');
im_common(app);

// 登录相关IM接口
require('./routes/imLogin')(app);

// 房间消息相关IM接口
require('./routes/imMsgMgr')(app);

// 公聊大厅相关接口
require('./routes/imPublicRoomMgr')(app);

//房间队列相关IM接口
require('./routes/imQueueMgr')(app);

// 房间管理IM接口
require('./routes/imRoomMgr')(app);

app.svr.listen(port, function () {
    console.log('Server listening at port %d', port);
});

//app.setBeatTime(5000,15000);
/**
 * @desc 监听客户端的连接和断开
 */



