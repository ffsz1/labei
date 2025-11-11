'use strict';
var F = require('../common/function');
var C = require('../config/index');
var _ = require('underscore');
_.str = require('underscore.string');
_.v = require('validator');
var co = require('co');
var exec = require('child_process').exec;

var redisLib = require('../libs/redis');
const redisClient = redisLib.redisClient;
const redisCo = redisLib.redisCo;
const koa_send = require('koa-send');
const fs = require('fs');

module.exports = function (app) {
    var mgr_map = app.common_mgr.mgr_map;
    var http_app = app.app;

    http_app.regRequest('/test/:apiVer/test', function* (ctx) {
        //ctx.set('Location', 'itms-services://?action=download-manifest&url=https%3A%2F%2Fcode.aliyun.com%2Ffeimat2020%2Fstatic%2Fraw%2Fmaster%2Fhaijiaoxingqiu.plist');
        //ctx.status = 302;
        ctx.body = "1609442743808";
    });

    http_app.regRequest('/test/:apiVer/haijiao', function* (ctx) {
        //ctx.set('Location', 'itms-services://?action=download-manifest&url=https%3A%2F%2Fcode.aliyun.com%2Ffeimat2020%2Fstatic%2Fraw%2Fmaster%2Fhaijiaoxingqiu.plist');
        //ctx.status = 302;
        ctx.body = "1901278429000";
    });

    http_app.regAll('/test/:apiVer/stat', function* (ctx) {                                                                     
        yield F.checkParamsNull(ctx, "type,uid");                                                                               
        F.setResJson(ctx, 0);
    }); 


    http_app.regAll('/user/:apiVer/get', function* (ctx) {
        ctx.body = "aaaa";
    });

    http_app.regRequest('/test/:apiVer/test2', function* (ctx) {
        //mgr_map.ActivemqMgr.client.publish("/queue/test_delay_queue",'aaa');

        ctx.body = "1601278429000";
        //let res = yield mgr_map.curl.httpGet('172.17.64.210', 3007, '/test/v1/test', {'uid': 100715}, null, 'utf8', 'http');  //  TODO 替换成java
	//ctx.body = res.data;
        //let res = yield mgr_map.curl.httpGet('39.105.187.28', 80, '/user/v5/get', {'uid': 100715}, null, 'utf8', 'http');  //  TODO 替换成java
        //let res = yield mgr_map.redis.getSocketRoomList(ctx.I.socketId);
	
        //yield F.checkParamsNull(ctx, "t");
	//let lock = yield mgr_map.redis.getLock("aabbcc");
	//if (lock.suc == 0) F.throwErr("get lock fail");
	//yield F.sleep(ctx.I.t);
	//yield mgr_map.redis.releaseLock(lock);


        // yield mgr_map.redis.addSocketToUser('1111','socketid1');
        // yield F.sleep(1);
        // yield mgr_map.redis.addSocketToUser('1111','socketid2');
        // yield F.sleep(1);
        // yield mgr_map.redis.addSocketToUser('1111','socketid3');
        // let res = yield mgr_map.redis.getUserSocketList('1111');


        // yield mgr_map.redis.addMemberToQueue('roomid1',-1,"---1");
        // yield mgr_map.redis.addMemberToQueue('roomid1',0,"00000");
        // yield mgr_map.redis.addMemberToQueue('roomid1',1,"11111");
        // let res = yield mgr_map.redis.getAllQueueMember('roomid1');
	//let res = yield mgr_map.redis.setnx("gghh",5);
        //F.setResJson(ctx, 0,res.data);
    });




};

