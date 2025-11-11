'use strict';
var C = require('../config');
var F = require('../common/function');
var _ = require('underscore');
_.str = require('underscore.string');
var redis = require('redis');
var co = require('co');

var wrapper = require('co-redis');

function redisSubMgr(app, common_mgr) {

	var model_map = app.model_mgr.model_map;
	var mgr_map = common_mgr.mgr_map;

	this.redisSub;
	this.redisPush;
	var that = this;

	this.init = function (){
		try {
			that.redisSub = redis.createClient(C.redis.port,C.redis.host,C.redis.options);
			that.redisSub.select(C.redis.db_sub,function(){
			    console.log('redis subscribe select db is '+C.redis.db_sub);
			});

			that.redisPush = redis.createClient(C.redis.port,C.redis.host,C.redis.options);
			that.redisPush.select(C.redis.db_sub,function(){
			    
			});

			that.redisSub.on("message", function (channel, message) {
				that.onMessage(channel, message);
			});

			that.redisSub.on("error", function (error) {
				console.log("redis subscribe error. message: " + error);
			});

			that.redisSub.on("subscribe", function (channel, count) {
				console.log("redis subscribe:client subscribe. channel:" + channel + " count:" + count);
			});

			that.redisSub.on("unsubscribe", function (channel, count) {
				console.log("redis subscribe:client unsubscribe. channel:" + channel + " count:" + count);
			});
		} catch(e) {
			F.log("err", "redis subscribe connect fiale");
			console.log(e);
		}
	}

	this.init();

	this.onMessage = function (channel, message) {
		try {
			var prefix = C.redisPre.subscribe_prefix.replace('%s', '');
			var uniid = channel.replace(prefix, '');
			var res = JSON.parse(message);
			co(mgr_map.im.emitLocal(uniid, res['route'], res['msg'], 'redisSub'));
		} catch(e) {
			F.log("err", "redis subscribe on message fiale");
			console.log(e);
		}
	};

	this.subscribe = function (uniid) {
		if(F.isNull(uniid)) return false;
		var channel = _.str.sprintf(C.redisPre.subscribe_prefix, uniid);
		that.redisSub.subscribe(channel);
	}

	this.unsubscribe = function (uniid) {
		if(F.isNull(uniid)) return false;
		var channel = _.str.sprintf(C.redisPre.subscribe_prefix, uniid);
		that.redisSub.unsubscribe(channel);
	}

	this.publish = function (uniid, route, msg, err_cb) {
		var channel = _.str.sprintf(C.redisPre.subscribe_prefix, uniid);
		var send_data = {
			"route":route,
			"msg":msg
		};
		F.addOtherLogs("imrw/imrw",['##im send by publish: uniid:',uniid," route:",route]);
		that.redisPush.publish(channel, JSON.stringify(send_data), function (err, result) {
			if(err || result == 0) {
                common_mgr.addLogs(["publish err:",err,'channel:',channel,"uniid:",uniid]);
				if(!F.isNull(err_cb)) {
					try{
						err_cb(uniid, route, send_data);
					}catch(e) {
                        common_mgr.addLogs(["publish errcb err:",e]);
					}
				}
			}
		});
	}

}


module.exports = redisSubMgr;
