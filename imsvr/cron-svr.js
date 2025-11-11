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
var gzip = require('koa-gzip');//压缩页面
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


httpapp.proxy = true;
httpapp.use(cors({credentials: true}));
httpapp.use(gzip());
httpapp.keys = ['keys', 'keykeys'];
httpapp.use(session({
    // 默认采用 memory 方式
    store: redisStore({
        host:C.redis.host,
        db:C.redis.db,
        pass: C.redis.options.auth_pass
    }),
    key: C.session.key,
    prefix: C.session.prefix,
    rolling:true,//always reset the cookie and sessions, default false
    ttl: C.session.ttl*1000//此处单位为毫秒，所以得乘于1000
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

var port = C.cronsvr_port;
app.port = port;
app.listen(port, function () {
    console.log('Server listening at port %d', port);
});

app.model_mgr = new modelMgr();
app.common_mgr = new commonMgr(app);

try{
    app.common_mgr.mgr_map.cronJobLogic.batchAddJob();
    app.common_mgr.mgr_map.cronJob.start();
    co(app.common_mgr.mgr_map.im.clearLock());
}catch(e) {
    F.addErrLogs(["cron err:", e.stack]);
}

