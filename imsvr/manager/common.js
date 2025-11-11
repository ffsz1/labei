'use strict';
var fs=require('fs');
var F = require('../common/function');
var C = require('../config');

function commonMgr(app) {

    var mgr_path = __dirname;
	this.mgr_map = {};
	var that = this;
	console.log(mgr_path);
	this.init = function (app){
		try{
			if(!fs.existsSync(mgr_path)) {
				F.throwErr("commonMgr path err");
			}
			var files = fs.readdirSync(mgr_path);
            console.log(files);
			files.forEach(function(file, index) {
                try {
				    if (file != 'common.js' && file != 'model.js') {
				    	var class_name = file.substr(0, file.length - 3);
				    	var mgr_obj = require('./' + class_name);
				    	that.mgr_map[class_name] = new mgr_obj(app, that);
				    }
                } catch(e) {
                    console.log(e.stack);
                }
			});
		}catch(e) {
			console.log(e.stack);
		}
			
	}

	this.init(app);

	/* 返回json字符替换规则
	*/
	this.pregReplaceResJson = function* (res, uniid, ctx) {
		// var ip = yield that.mgr_map.redis.getImagePrefix(uniid);
		// var is_https = yield that.mgr_map.redis.getHttpProtoc(uniid);
		// var image_prefix = F.getImagePrefixByReqHost(ip,is_https);
		// var request_prefix = F.getShareHostIm(ip,is_https);
        // res = F.replaceRtmpHost(ip,res,ctx.is_mobile,ctx.is_msite);
        // res = F.replaceResJson(res, [C.pic_prefix,C.request_prefix], [image_prefix,request_prefix]);
		// res = F.replaceCreditStr(res, ctx.credit_type);
        // res = F.replaceCreditUnitStr(res, ctx.credit_type);
		return res;
	}

	// 滚动日志
    this.addDebugLogs = function (data) {
        that.mgr_map.logs.addLogs("debug/debug",data); //TODO 太多地方用到，先改成err 不遗漏
    };

	this.addLogs = function (data) {
		that.mgr_map.logs.addLogs("debug/debug",data); //TODO 太多地方用到，先改成err 不遗漏
	};

    this.addErrLogs = function (data) {
        that.mgr_map.logs.addLogs("err/err",data);
    };

}


module.exports = commonMgr;
