/*********************** 对koa.io进行了rpc封装，支持通讯里面用yield ******************/
'use strict';

var co = require('co');
var koa = require('koa.io');
var rpcServer = require('./libs/rpcserver');
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

var modelMgr = require('./manager/model');
var commonMgr = require('./manager/common');

var app = new rpcServer();
var httpapp = app.app;
httpapp.use(bodyParser());
jsonp(httpapp, {
    callback: '_callback', // default is 'callback'
    limit: 50 // max callback name string length, default is 512
});

httpapp.root_dir = __dirname;

httpapp.proxy = true;
httpapp.use(cors({credentials: true}));
httpapp.use(gzip());
httpapp.keys = ['keys', 'keykeys'];
httpapp.use(session({
    // 默认采用 memory 方式
    store: redisStore({
        host: C.redis.host,
        db: C.redis.db,
        pass: C.redis.options.auth_pass
    }),
    key: C.session.key,
    prefix: C.session.prefix,
    rolling: true,//always reset the cookie and sessions, default false
    ttl: C.session.ttl * 1000,//此处单位为毫秒，所以得乘于1000
    cookie: {
        signed: false,
        httpOnly: false,
        //domain: C.cookie.domain,
        //maxage: C.cookie.maxage
    }
}));
httpapp.use(router(httpapp));

///////渲染模板
var render = require('koa-ejs');
render(httpapp, {
    root: './view',
    layout: 'template',
    viewExt: 'html',
    cache: false,
    debug: true
});

var port = C.http_port;
app.port = port;
app.listen(port, function () {
    console.log('Server listening at port %d', port);
});

app.model_mgr = new modelMgr();
app.common_mgr = new commonMgr(app);
co(app.common_mgr.mgr_map.im.clearLock());

var http_common = require('./routes/httpCommon');
http_common(app);

// 处理服务器内部接口
require('./routes/httpInnerApi')(app);

// 登录相关http接口
require('./routes/httpLogin')(app);

// 成员相关http接口
require('./routes/httpMemberMgr')(app);

//
require('./routes/httpMsgMgr')(app);

//
require('./routes/httpPublicRoomMgr')(app);

// 房间坑位管理http接口
require('./routes/httpQueueMgr')(app);

// 房间管理http接口
require('./routes/httpRoomMgr')(app);

require('./routes/httpBanZhu')(app);

require('./routes/httpBanZhuAction')(app);

require('./routes/httpBanZhuMission')(app);

//app.setBeatTime(5000,15000);
/**
 * @desc 监听客户端的连接和断开
 */



