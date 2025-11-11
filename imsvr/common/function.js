'use strict';
// 通用 function
var fs = require('fs');
var path = require('path');
var md5 = require('md5');
var request = require('koa-request');
var async_request = require('request');
//var iconv = require('iconv-lite');
var C = require('../config');
var redis = require('../libs/redis.js');
var thunkify = require('thunkify');
var exec = require('child_process').exec;
//var sleep = require('es6-sleep');

var logs_obj = require('../libs/logs.js');
var os = require('os');
var logs = new logs_obj();

var _ = require('underscore');
//exports.sleep=sleep;
_.str = require('underscore.string');
_.v = require('validator');
var crypto = require('crypto');

String.prototype.endsWith = function (suffix) {
    return this.indexOf(suffix, this.length - suffix.length) !== -1;
};

exports.values = function (obj) {
    return Object.keys(obj).map((k) => obj[k]);
};

Date.prototype.format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours() % 12 == 0 ? 12 : this.getHours() % 12, //小时
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    var week = {
        "0": "/u65e5",
        "1": "/u4e00",
        "2": "/u4e8c",
        "3": "/u4e09",
        "4": "/u56db",
        "5": "/u4e94",
        "6": "/u516d"
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    if (/(E+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "/u661f/u671f" : "/u5468") : "") + week[this.getDay() + ""]);
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
}

Date.getDayOfMonth = function (y, Mm) {
    if (typeof y == 'undefined') {
        y = (new Date()).getFullYear();
    }
    if (typeof Mm == 'undefined') {
        Mm = (new Date()).getMonth();
    }
    var Feb = (y % 4 == 0) ? 29 : 28;
    var aM = new Array(31, Feb, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
    return aM[Mm];
};

Date.getLastMonth = function () {
    var dt = new Date();
    var y = (dt.getMonth() == 0) ? (dt.getFullYear() - 1) : dt.getFullYear();
    var m = (dt.getMonth() == 0) ? 11 : dt.getMonth() - 1;
    var preM = Date.getDayOfMonth(y, m);
    var d = (preM < dt.getDate()) ? preM : dt.getDate();
    var new_time = new Date(y, m, d).format("yyyy-MM-dd HH:mm:ss");
    return new_time;
};

exports.getLocalIP = function () {
    var ifaces = os.networkInterfaces();
    console.log(ifaces);
    for (var dev in ifaces) {
        if (dev.indexOf('eth') == -1) continue;
        return ifaces[dev][0]['address']
    }
    return '127.0.0.1';
};

/**
 * @desc 返回当前的格式化时间
 * @param format
 */
exports.fDatetime = function (format = 'yyyy-MM-dd HH:mm:ss', dateTime) {
    if (dateTime) {
        return new Date(dateTime).format(format);
    }
    return new Date().format(format);
};

// 获取某天的起始时间戳 默认为当天 单位秒
exports.beginTimestamp = function (time1) {
    time1 = time1 == undefined ? this.timestamp() : parseInt(time1);
    var newDate = new Date(time1 * 1000);
    return parseInt(new Date(newDate.format('yyyy-MM-dd') + ' 00:00:00').getTime() / 1000);
};

// 计算日期间隔 单位秒,time1 比 time2 晚时为负数
exports.dateDiff = function (time1, time2) {
    time1 = this.beginTimestamp(time1); // 计算某天的起始时间戳
    time2 = this.beginTimestamp(time2); // 计算某天的起始时间戳
    return parseInt((time2 - time1) / (60 * 60 * 24));
};

/*
 @todo 取得时间戳，默认取到秒 传零取毫秒
 函数名：timestamp
 * 参数：无
 * 返回：当前时间的10位UNIX时间戳
 */
exports.timestamp = function (flag) {
    if (flag == 0) {
        return parseInt(new Date().getTime());
    }
    return parseInt(new Date().getTime() / 1000);
};

// 获取前xx或后xx时间，delta单位为秒
exports.getTimeByDelta = function (delta = 0, from_date_time = null) {
    var today = new Date();
    if (!this.isNull(from_date_time)) today = new Date(from_date_time);
    var yesterday_milliseconds = today.getTime() + 1000 * delta;
    var yesterday = new Date();
    yesterday.setTime(yesterday_milliseconds);
    return yesterday.format("yyyy-MM-dd HH:mm:ss");
};

exports.getTimeStampByDelta = function (delta = 0, from_date_time = null) {
    var today = new Date();
    if (!this.isNull(from_date_time)) today = new Date(from_date_time);
    var yesterday_milliseconds = today.getTime() + 1000 * delta;
    return yesterday_milliseconds;
};

/**
 * 获取主播当地时区的可预约时间在服务器上的时间
 * @param TimezoneOffset    时区（主播的时区。单位分钟）
 * （时区的计算是如果比0时区快那么就是-的，如果比0时区慢那么就是正的。如东八区比0时区快8个小时，那么就是-480分钟）
 */
exports.getTimeByZone = function (TimezoneOffset) {
    var newday = new Date();
    let timestamp = parseInt(new Date().getTime() / 60000) * 60000;//服务器上的时间戳（精确到分钟）
    var serverTimezoneOffset = new Date().getTimezoneOffset();

    TimezoneOffset = TimezoneOffset - serverTimezoneOffset;//主播的时区-服务器的时区。获取时区差
    newday.setTime(timestamp - TimezoneOffset * 60 * 1000);
    // newday：显示用 timestamp：真正当前时间戳
    return {'newday': newday, 'timestamp': timestamp};
}

//AD页面用节目开始日期
exports.anchorReservationTimeDay = function (time) {
    var month = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    var tmpM = this.fDatetime("M", time);
    var tmpData = month[parseInt(tmpM) - 1] + ' ' + this.fDatetime("dd", time);
    return tmpData;
}
//AD节目开始时间
exports.anchorReservationTimeHour = function (time) {
    var hour = this.fDatetime("HH", time);
    var minute = this.fDatetime("mm", time);
    var timeVar = 'am';
    if (hour > 12) {
        hour = hour - 12;
        timeVar = 'pm';
    }
    var tmpData = hour + ':' + minute + ' ' + timeVar;
    return tmpData;
}

/**
 * 根据时间戳和主播时区，获取主播的日期格式
 * @param time
 * @param Timezone
 * @returns {{anchorWeek: number, anchorHours: number, anchorMinutes: number}}
 */
exports.getAnchorTimeByTimestamp = function (time, Timezone) {
    //计算主播的时区
    let anchorTimezone = (Timezone / 60) * -1;//根据时区的分钟取反获取到时区
    let TimezoneInteger = parseInt(anchorTimezone);
    let TimezoneDecimal = (anchorTimezone - TimezoneInteger) * 60;
    if (TimezoneInteger > 0) {
        TimezoneInteger = '+' + TimezoneInteger;
    }
    if (TimezoneDecimal) {
        Timezone = TimezoneInteger + ':' + TimezoneDecimal;
    } else {
        Timezone = TimezoneInteger;
    }

    let Timestamp = new Date().setTime(time * 1000);
    let serverTime = new Date(Timestamp).format("yyyy-MM-dd HH:mm:ss");
    serverTime = serverTime + Timezone;
    let anchorDay = new Date(serverTime).getDay();
    let anchorHours = new Date(serverTime).getHours();
    let anchorMinutes = new Date(serverTime).getMinutes();
    return {anchorWeek: anchorDay, anchorHours: anchorHours, anchorMinutes: anchorMinutes};
}

//var initEnv={};
//exports.luashas=lua.__shas;
exports.Init = function* (Env, actArray) {

    var I = {};
    var fields = Env.request.body.fields;
    if (fields) {
        fields = this.rmHtmlAndJsByJson(fields)
        for (var field in fields) {
            I[field] = fields[field];
        }
    }

    var params = Env.params;
    Env.pattern_url = Env.request.url.split("?")[0];
    if (params) {
        for (var parm in params) {
            I[parm] = params[parm];
            Env.pattern_url = Env.pattern_url.replace(new RegExp("/" + I[parm] + "/", 'g'), "/*/"); //每次只匹配一个
            Env.pattern_url = Env.pattern_url.replace(new RegExp("/" + I[parm] + "$", 'g'), "/*");
        }

        if (I['apiVer']) {
            var pattern = /^v([\.|\d+]+)$/gi;
            var tmpVer = pattern.exec(I['apiVer']);
            if (tmpVer[1] == undefined) {
                I['apiVerNum'] = 1;
            } else {
                I['apiVerNum'] = tmpVer[1];
            }
        } else {
            I['apiVerNum'] = 1;
        }
    }
    //get参数
    var querys = Env.query;
    if (querys) {
        querys = this.rmHtmlAndJsByJson(querys)
        for (var query in querys) {
            I[query] = querys[query];
        }
    }

    var errors = {};

    if (I) {
        // var app_ver = Env.request.accept.headers['app-ver'];
        // if (app_ver == undefined){
        //   app_ver = '';
        // }
        if (!_.isEmpty(errors)) {
            I.errors = errors;
            console.log('[ERR] [%s] [%s] [%s] [%s] [%s] [%s] <%s>', new Date().format('yyyy-MM-dd hh:mm:ss'), Env.ip, app_ver,
                Env.request.method, Env.req.url, errors, Env.request.accept.headers['user-agent']);
        }
        //console.log('[INFO] [%s] [%s] [%s] [%s] [%s] <%s>',new Date().format('yyyy-MM-dd hh:mm:ss'),Env.ip,app_ver,
        //Env.request.method,Env.req.url,Env.request.accept.headers['user-agent']);
        //console.log('[Input] [%O]',I);
    }
    I.real_ip = Env.request.accept.headers['x-real-ip'];
    I.is_https = Env.request.accept.headers['is_https'];
    if (!this.isNull(I.is_https)) {
        I.protocol = "https";
    } else {
        I.protocol = "http";
    }
    if (this.isNull(I.real_ip)) {
        I.real_ip = Env.request.accept.headers.host;
    }
    this.addDebugLogs("I.real_ip:" + I.real_ip);
    Env.clientip = Env.request.accept.headers['x-forwarded-for'];
    if (this.isNull(I.clientip)) {
        Env.clientip = Env.request.accept.headers['x-client-ip'];
        if (this.isNull(Env.clientip)) {
            Env.clientip = Env.request.req.headers['x-forwarded-for'] || Env.request.req.connection.remoteAddress ||
                Env.request.req.socket.remoteAddress || Env.request.req.connection.socket.remoteAddress;
            if (!this.isNull(Env.clientip)) {
                Env.clientip = Env.clientip.match(/\d+.\d+.\d+.\d+/);
                Env.clientip = !this.isNull(Env.clientip) ? Env.clientip[0] : '';
            }
        }
    }
    if (!this.isNull(I.is_ajax) && I.is_ajax == 1) Env.request.accept.headers['x-requested-with'] = 'xmlhttprequest';
    //this.addOtherLogs("imrw/imrw",[JSON.stringify(["##http receive:",Env.req.url,"clientip",Env.clientip,I,Env.session.userid,Env.request.req.headers['cookie']])]);
    return I;
};

/*
 @todo获取页面返回信息（返回对象）
 @status 状态码
 @msg  返回信息
 @level 返回信息等级
 @data 数据项（对象）
 */
exports.returnMsg = function (status, msg, level, data) {
    var returnData = {};
    var code = status || 10200;
    returnData.state = {
        msg: msg || "",
        code: code
    };
    returnData.data = {};
    if (typeof data === "object" && data !== null) {
        returnData.data = data;
    }

    if (C.env != 'production') {
        this.addLogs(['[RETURN] [%s] [%s]', status, msg]);
    }
    return returnData;
};

exports.pcCheckParams = function* (ctx) {
    ctx.I = yield this.Init(ctx, [1, 11]);
    if (ctx.I.errors) {
        ctx.jsonp = this.returnMsg(10400, ctx.I.errors.msg, ctx.I.errors.level);
        return false;
    } else {
        return true;
    }
};

/*
 @todo 获取随机数
 @len 随机数位数
 */
exports.getRandom = function (len) {
    return Math.floor(Math.random() * (Math.pow(10, len) - Math.pow(10, len - 1)) + Math.pow(10, len - 1));
};

/**
 * 获取指定范围的随机数
 * @param Min
 * @param Max
 * @returns {*}
 * @constructor
 */
exports.RandomNumBoth = function (Min, Max) {
    return Math.floor(Math.random() * (Max - Min + 1) + Min);
}

var all_chat = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];
exports.getRandomNumAndChat = function (len) {
    var random_str = '';
    for (var i = 0; i < len; i++) {
        var c = all_chat[Math.floor(Math.random() * (all_chat.length))];
        random_str += c;
    }
    return random_str;
};

/**
 * 随机生成字母
 * @type {string[]}
 */
var randomChat = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];
exports.getRandomChat = function (len) {
    var random_str = '';
    for (var i = 0; i < len; i++) {
        var c = randomChat[Math.floor(Math.random() * (randomChat.length))];
        random_str += c;
    }
    return random_str;
};

exports.intArray = function (arr) {
    if (_.isObject(arr)) {
        for (var i in arr) {
            if (_.v.isIn(i, ['uid'])) {
                arr[i] = parseInt(arr[i]);
            }
        }
    }
};

/*
 isJson 尝试判断字符串是否为json类型。返回json对象或false
 */
exports.isJson = function (str) {
    try {
        return JSON.parse(str);
    } catch (e) {
        return false;
    }
};

/**
 * @desc 取得范围内的随机数
 * @param min
 * @param max
 * @returns {*}
 */
exports.getRandomNum = function (min, max) {
    var range = max - min;
    var rand = Math.random();
    return (min + Math.round(rand * range));
};

exports.pad = function (number, length, pos) {
    var str = '' + number;
    while (str.length < length) {
        if ('r' == pos) {
            str = str + '0';
        } else {
            str = '0' + str;
        }
    }
    return str;
};

/**
 * @desc 转成16进制
 * @param chr
 * @param padLen
 */
exports.toHex = function (chr, padLen) {
    if (null == padLen) {
        padLen = 2;
    }
    return this.pad(chr.toString(16), padLen);
};

/**
 * @desc 获取登陆的设备
 * @param ua
 * @returns {*}
 */
exports.getDevice = function (ua) {
    if (ua && ua.length > 0) {
        ua = ua.toLowerCase();
        if (ua.match(/iPhone/i) == "iphone") {
            return 'iphone';
        } else if (ua.match(/Android/i) == "android") {
            return 'android';
        }
    }
    return '';
};

/**
 * @desc 取得推流的一些参数
 * @param device
 * @returns {*}
 */
exports.getPushParms = function (device) {
    return device == 'android' ? C.android_push_parms : (device == 'iphone' ? C.ios_push_parms : {});
};

/**
 * @desc socket通知
 * @param url
 */
exports.socketNotify = function (url) {
    var options = {
        url: url
    };
    async_request.get(options, function (error, response, body) {
        if (error) {
            console.log(error);
        }
        console.log(body);
    });
};
/**
 * @desc 生成直播url
 * @param uid 主播id
 * @param lhid live_history自增id
 * @returns {string}
 */
exports.genLiveUrl = function (uid, lhid) {
    var url = C.live.live_url;
    url = _.str.sprintf(url, uid, lhid);
    return url;
};

/**
 * @desc 生成IM地址
 * @param uid
 * @returns {string}
 */
exports.genImUrl = function (tokenKey, uid) {
    var url = C.im_url;
    url = _.str.sprintf(url, uid, tokenKey);
    return url;
};


/**
 * @desc 生成flv播放地址
 * @param path
 * @returns {string}
 */
exports.genFlvPlayBackUrl = function (path) {
    return C.live.flv_playback_url + path;
};

exports.isNull = function (obj) {
    if (obj == null || typeof(obj) == "undefined" || obj.length == 0) {
        return true;
    }
    if (typeof(obj) == 'object' && !(obj instanceof Date) && Object.keys(obj).length == 0) {
        return true;
    }
    return false;
};

// private method for UTF-8 encoding
exports._utf8_encode = function (string) {
    string = string.replace(/\r\n/g, "\n");
    let utftext = "";
    for (let n = 0; n < string.length; n++) {
        let c = string.charCodeAt(n);
        if (c < 128) {
            utftext += String.fromCharCode(c);
        } else if ((c > 127) && (c < 2048)) {
            utftext += String.fromCharCode((c >> 6) | 192);
            utftext += String.fromCharCode((c & 63) | 128);
        } else {
            utftext += String.fromCharCode((c >> 12) | 224);
            utftext += String.fromCharCode(((c >> 6) & 63) | 128);
            utftext += String.fromCharCode((c & 63) | 128);
        }

    }
    return utftext;
};

// private method for UTF-8 decoding
exports._utf8_decode = function (utftext) {
    let string = "";
    let i = 0;
    let c = 0, c1 = 0, c2 = 0, c3 = 0;
    while (i < utftext.length) {
        c = utftext.charCodeAt(i);
        if (c < 128) {
            string += String.fromCharCode(c);
            i++;
        } else if ((c > 191) && (c < 224)) {
            c2 = utftext.charCodeAt(i + 1);
            string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
            i += 2;
        } else {
            c2 = utftext.charCodeAt(i + 1);
            c3 = utftext.charCodeAt(i + 2);
            string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
            i += 3;
        }
    }
    return string;
};

// public method for encoding
exports.base64_encode = function (input) {
    let _keyStr = C._keyStr;
    let output = "";
    let chr1, chr2, chr3, enc1, enc2, enc3, enc4;
    let i = 0;
    input = this._utf8_encode(input);
    while (i < input.length) {
        chr1 = input.charCodeAt(i++);
        chr2 = input.charCodeAt(i++);
        chr3 = input.charCodeAt(i++);
        enc1 = chr1 >> 2;
        enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
        enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
        enc4 = chr3 & 63;
        if (isNaN(chr2)) {
            enc3 = enc4 = 64;
        } else if (isNaN(chr3)) {
            enc4 = 64;
        }
        output = output +
            _keyStr.charAt(enc1) + _keyStr.charAt(enc2) +
            _keyStr.charAt(enc3) + _keyStr.charAt(enc4);
    }
    return output;
};

// public method for decoding
exports.base64_decode = function (input) {
    let _keyStr = C._keyStr;
    let output = "";
    let chr1, chr2, chr3;
    let enc1, enc2, enc3, enc4;
    let i = 0;
    input = input.replace(/[^A-Za-z0-9\(\)\-]/g, "");
    while (i < input.length) {
        enc1 = _keyStr.indexOf(input.charAt(i++));
        enc2 = _keyStr.indexOf(input.charAt(i++));
        enc3 = _keyStr.indexOf(input.charAt(i++));
        enc4 = _keyStr.indexOf(input.charAt(i++));
        chr1 = (enc1 << 2) | (enc2 >> 4);
        chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
        chr3 = ((enc3 & 3) << 6) | enc4;
        output = output + String.fromCharCode(chr1);
        if (enc3 != 64) {
            output = output + String.fromCharCode(chr2);
        }
        if (enc4 != 64) {
            output = output + String.fromCharCode(chr3);
        }
    }
    output = this._utf8_decode(output);
    return output;
};


exports.myEncode = function (str) {
    str = str.toString();
    let private_key = C.private_key;
    let str2 = "";
    for (let i = 0, j = 0; i < str.length; i++, j++) {
        if (j > private_key.length - 1) j = 0;
        str2 += String.fromCharCode(str.charCodeAt(i) ^ private_key.charCodeAt(j));
    }
    return this.base64_encode(str2);
};

exports.myDecode = function (str) {
    str = str.toString();
    let private_key = C.private_key;
    str = this.base64_decode(str);
    let str2 = "";
    for (let i = 0, j = 0; i < str.length; i++, j++) {
        if (j > private_key.length - 1) j = 0;
        str2 += String.fromCharCode(str.charCodeAt(i) ^ private_key.charCodeAt(j));
    }
    return str2;
};

/**
 * @desc 取得sdk sign
 * @param token
 * @param ts
 */
exports.genSdkSign = function (token, ts) {
    var md5Str = 'token' + token + 'ts' + ts;
    var md5Str = md5.digest_s(md5.digest_s(md5Str) + '.' + C.sdk_sign_key);
    return md5Str;
};

/**
 * @desc 取得m3u8的长度
 * @param hlsUrl
 * @returns {*|number}
 */
exports.getM3u8Len = function* (hlsUrl) {
    var content = yield this.urlGetContent(hlsUrl);
    var pattern = /#EXTINF:([\d|.+]+),/g;
    var arr = '';
    var len = 0;
    while ((arr = pattern.exec(content)) != null) {
        len += parseFloat(arr[1]);
    }
    return Math.floor(len);
};

exports.assertParamNull = function* (parent_obj, key_str) {
    var key_arr = key_str.split(",");
    for (let i = 0; i < key_arr.length; i++) {
        var field = _.str.trim(key_arr[i]);
        var exe_str = _.str.vsprintf("this.isNull(parent_obj.%s)", [field]);
        if (eval(exe_str)) {
            this.throwErr(this.log("err:", "params %s is null!", [key_arr[i]]));
            return false;
        }
    }
    return true;
}

/**
 * @desc 取得sdk sign
 * @param token
 * @param ts
 */
exports.checkParamsNull = function* (Env, key_str) {
    if (this.isNull(Env.I)) Env.I = yield this.Init(Env);
    return yield this.assertParamNull(Env.I, key_str);
};

exports.log = function (level, tpl, arr) {
    arr = arr || [];
    var line = _.str.vsprintf(tpl, arr);
    console.log(line);
    logs.addLogs("debug/debug", [line]);
    return line;
};

exports.addOtherLogs = function (name, data) {
    logs.addLogs(name, data);
}

exports.addDebugLogs = function (data) {
    if (!this.isNull(C.NO_LOG_DEBUG)) return;
    logs.addLogs("debug/debug", data);
}

exports.addLogs = function (data) {
    logs.addLogs("debug/debug", data);
    //logs.addLogs("sys/sys",data);
}

exports.addErrLogs = function (data) {
    logs.addLogs("debug/debug", data);
    //logs.addLogs("sys/sys",data);
    logs.addLogs("err/err", data);
    console.log([data]);
}

exports.addSlowLogs = function (data) {
    logs.addLogs("slow/slow", data);
}

exports.addWebErrLogs = function (data) {
    logs.addLogs("web_err/web_err", data);
}

exports.vsprintf = function (tpl, arr = []) {
    return _.str.vsprintf(tpl, arr);
};

exports.sqlprintf = function (tpl, arr = []) {
    for (var i = 0; i < arr.length; i++) {
        var item = arr[i];
        if (typeof(item) == 'string') item = "'" + item + "'";
        arr[i] = item;
    }
    return _.str.vsprintf(tpl, arr);
};

exports.logFile = function (level, tpl, arr) {
    arr = arr || [];
    var line = _.str.vsprintf(tpl, arr);
    console.log(level + ":" + line);
    return line;
};

exports.assert = function (ctx, str) {
    if (false == eval(str)) {
        this.throwErr(str + " not match!");
    }
    return true;
};

exports.imCheckParamsNull = function* (req_data, key_str) {
    if (this.isNull(req_data)) this.throwErr("req data is null");
    var key_arr = key_str.split(",");
    for (let i = 0; i < key_arr.length; i++) {
        var field = _.str.trim(key_arr[i]);
        var exe_str = _.str.vsprintf("this.isNull(req_data.%s)", [field]);
        if (eval(exe_str)) {
            this.throwErr(this.log("err:", "params %s is null!", [key_arr[i]]));
            return false;
        }
    }
    return true;
};

exports.isValidNumber = function* (int_value) {
    if (int_value < Number.MAX_SAFE_INTEGER / 1000 && int_value > Number.MIN_SAFE_INTEGER / 1000) return true;
    return false;
}

exports.imAssert = function (req_data, str) {
    if (false == eval(str)) {
        this.throwErr(str + " not match!");
    }
    return true;
};

exports.throwErr = function (msg, code = null, data = null, use_msg = false) {
    code = code || 10003;
    var err = new Error(msg);
    this.log("err:", " msg: %s, code:%s", [msg, code]);
    err.code = code;
    err.data = data;
    err.use_msg = use_msg;
    throw err;
}
/**
 *
 * @param code
 * @param data
 * @param show_code 0:表示男士 1:表示女士
 * @param role_type
 */
exports.throwErrCode = function (code, data = null, show_code = null, role_type = null) {
    var msg = this.makeErrMsg(code, role_type);
    if (this.isNull(show_code)) this.throwErr(msg, code, data, true);
    else this.throwErr(msg, show_code, data, true);
}

/**
 *
 * @param code
 * @param params
 * @param data
 * @param show_code 0:表示男士 1:表示女士
 * @param role_type
 */
exports.throwErrCodeWithParam = function (code, params = [], data = null, show_code = null, role_type = null) {
    var msg = this.makeErrMsgWithParam(code, params, role_type);
    if (this.isNull(show_code)) this.throwErr(msg, code, data, true);
    else this.throwErr(msg, show_code, data, true);
}

/**
 *
 * @param code
 * @param role_type 0:表示男士 1:表示女士
 * @returns {*}
 */
exports.makeErrMsg = function (code, role_type = null) {
    let real_code = code;
    if (!this.isNull(role_type)) {
        if (1 == role_type) real_code += "anchor";
        else real_code += "man";
        if (real_code in C.err_msg) {
        } else {
            real_code = code
        }
        ;
    }

    var msg = C.err_msg[real_code];
    if (this.isNull(msg)) {
        msg = C.err_msg["10003"];
    }
    return msg;
}

/**
 *
 * @param code
 * @param params
 * @param role_type 0:表示男士 1:表示女士
 * @returns {*}
 */
exports.makeErrMsgWithParam = function (code, params = [], role_type = null) {
    let real_code = code;
    if (!this.isNull(role_type)) {
        if (1 == role_type) real_code += "anchor";
        else real_code += "man";
        if (real_code in C.err_msg) {
        } else {
            real_code = code
        }
        ;
    }

    var msg = C.err_msg[real_code];
    if (this.isNull(msg)) {
        msg = C.err_msg["10003"];
    }
    else msg = _.str.vsprintf(msg, params);
    return msg;
}


exports.tipsWithParam = function (code, params = []) {
    var msg = C.tips[code];
    if (this.isNull(msg)) {
        msg = '';
    }
    else msg = _.str.vsprintf(msg, params);
    return msg;
}

// 组装缩略图名字
exports.makeThumb = function (img, type) {
    if (this.isNull(img)) return '';
    if (this.isNull(C.thumb_suffix[type])) return img;
    var suffix = C.thumb_suffix[type];
    var img_name_list = img.split('.');
    if (img_name_list.length <= 1) return img + suffix;
    img_name_list[img_name_list.length - 2] += suffix;
    return img_name_list.join(".");
}

//单位毫秒
exports.sleep = function* (time) {
    return new Promise(function (resolve, reject) {
        setTimeout(function () {
            resolve();
        }, time);
    });
}

exports.exists = function* (path) {
    return new Promise(function (resolve, reject) {
        fs.exists(path, function (exists) {
            resolve(exists);
        });
    });
}

exports.readFile = function* (path) {
    return new Promise(function (resolve, reject) {
        fs.readFile(path, function (err, data) {
            if (err) {
                var err = new Error(`Can not read file:${path}`);
                err.code = 10003;
                reject(err);
            } else {
                resolve(data);
            }
        });
    });
}

exports.readFileByChunk = function* (path) {
    let content = '';
    return new Promise(function (resolve, reject) {
        let fReadStream = fs.createReadStream(path, {
            encoding: 'utf8',
            start: 0,
            bufferSize: 10240
        });
        fReadStream.on('data', function (chunk) {
            content += chunk;
        });
        fReadStream.on('end', function () {
            resolve(content);
        });
    });
}

/**
 * 获取静态文件的路径
 * @param path
 * @returns {*}
 */
exports.getPublicPath = function (real_ip, is_https) {
    var path = this.getImagePrefixByReqHost(real_ip, is_https);
    var static_path = path.replace(C.image_upload_path, C.image_static);
    return static_path;
}

/**
 * 获取文件上传服务器
 * @param path
 * @returns {*}
 */
exports.getServerHost = function (real_ip, is_https) {
    var path = this.getImagePrefixByReqHost(real_ip, is_https);
    var static_path = path.replace(C.image_upload_path, "");
    return static_path;
}

exports.getServerIP = function (real_ip) {
    if (!this.isNull(C.image_ip)) return C.image_ip;
    if (!this.isNull(real_ip)) return real_ip.split(":")[0];
    return C.svr_ip;
}

exports.getCurHost = function (ctx) {
    let host = ctx.I.real_ip.split(":");
    let ip = host[0];
    let port = parseInt(host[1]);
    if (host.length == 1) port = 80;
    let http = 'http';
    let websocket = 'ws';
    if (!this.isNull(ctx.I.is_https)) {
        if (host.length == 1) port = 443;
        http = "https";
        websocket = "wss";
    }
    return {http: http, websocket: websocket, ip: ip, port: port};
}

/**
 * 返回请求地址信息
 * @param req_host
 * @param is_https
 * @returns {*}
 */
exports.getHostInfo = function (req_host, is_https) {
    var proto = !this.isNull(is_https) ? "https" : "http";
    if (!this.isNull(C.image_proto)) {
        proto = C.image_proto;
    }
    var port = (C.image_port == 80) ? 80 : C.image_port;
    if (!this.isNull(C.image_ip)) {
        return {'proto': proto, 'ip': C.image_ip, 'port': port, 'image_upload_path': C.image_upload_path};
    } else {
        var ip = this.isNull(req_host) ? C.svr_ip : req_host.split(":")[0];
        return {'proto': proto, 'ip': ip, 'port': port, 'image_upload_path': C.image_upload_path};
    }
}

exports.getImagePrefixByReqHost = function (req_host, is_https) {
    var proto = !this.isNull(is_https) ? "https" : "http";
    if (!this.isNull(C.image_proto)) {
        proto = C.image_proto;
    }
    var port = (C.image_port == 80 || C.image_port == 443) ? "" : ":" + C.image_port;
    var image_prefix = "";
    if (!this.isNull(C.image_ip)) {
        image_prefix = _.str.vsprintf("%s://%s%s%s", [proto, C.image_ip, port, C.image_upload_path]);
    } else {
        var ip = this.isNull(req_host) ? C.svr_ip : req_host.split(":")[0];
        image_prefix = _.str.vsprintf("%s://%s%s%s", [proto, ip, port, C.image_upload_path]);
    }
    return image_prefix;
};

exports.replaceRtmpHost = function (req_host, res, is_mobile, is_msite) {
    try {
        var find = C.RTMP_FLAG;
        var msg_str = JSON.stringify(res);
        if (req_host.indexOf("192") == 0) {
            msg_str = msg_str.replace(new RegExp(C.RTMP_FLAG, 'g'), "192.168.88.133");
        } else {
            msg_str = msg_str.replace(new RegExp(C.RTMP_FLAG, 'g'), "172.25.32.133");
        }
        // if (!this.isNull(C.RTMP_HOST)) {
        //     msg_str = msg_str.replace(new RegExp(C.RTMP_FLAG,'g'), C.RTMP_HOST);
        // } else {
        //     var ip = this.isNull(req_host)?C.svr_ip:req_host.split(":")[0];
        //     msg_str = msg_str.replace(new RegExp(C.RTMP_FLAG,'g'), ip);
        // }
        //if (msg_str.indexOf("controlManPushNotice") >= 0) this.addDebugLogs(["qiguaide is mobile:", is_mobile]);
        if (this.isNull(is_mobile) && this.isNull(is_msite)) { //pc
            msg_str = msg_str.replace(new RegExp("tobefitpublish", 'g'), "publish_flash");
            msg_str = msg_str.replace(new RegExp("tobefitplay", 'g'), "play_flash");
        } else { // mobile
            msg_str = msg_str.replace(new RegExp("tobefitpublish", 'g'), "publish_standard");
            msg_str = msg_str.replace(new RegExp("tobefitplay", 'g'), "play_standard");
        }
        return JSON.parse(msg_str);
    } catch (e) {
        console.log(e);
        return res;
    }
};

/* 返回json字符替换 
* res 返回json对象
* find 替换的字符串数组
* replace 替换的内容数组
*/
exports.replaceResJson = function (res, find, replace) {
    try {
        var msg_str = JSON.stringify(res);
        for (var i = 0; i < find.length; i++) {
            msg_str = msg_str.replace(new RegExp(find[i], 'g'), replace[i]);
        }
        return JSON.parse(msg_str);
    } catch (e) {
        this.addErrLogs(['replaceResJson 报错', res]);
        return res;
    }
}

exports.deepCopy = function (obj) {
    return JSON.parse(JSON.stringify(obj));
}

exports.setResJson = function (ctx, errno = 0, res_data = null) {
    if (!this.isNull(res_data) && !this.isNull(ctx.I.real_ip)) {
        var image_prefix = this.getImagePrefixByReqHost(ctx.I.real_ip, ctx.I.is_https);
        var request_prefix = this.getShareHost(ctx);
        // res_data = this.replaceRtmpHost(ctx.I.real_ip, res_data, ctx.is_mobile, ctx.is_msite);
        // res_data = this.replaceResJson(res_data, [C.pic_prefix, C.request_prefix], [image_prefix, request_prefix]);
        //res_data = this.replaceCreditStr(res_data, ctx.credit_type);
    }
    if (this.isNull(ctx.res_list)) ctx.res_list = [];
    ctx.res_list.push(this.packResJson(errno, res_data));
    //ctx.jsonp = this.packResJson(errno, res_data);
}

exports.packResJson = function (errno = 0, res_data = null) {
    var data;
    if (errno == 0) {
        data = {"errno": 0, "data": res_data};
    } else {
        data = {"errno": errno, "errmsg": C.err_msg[errno], "data": res_data};
    }
    return data;
};

exports.packResErrMsg = function (errno = 0, err_msg = null, res_data = null) {
    let data;
    if (errno == 0) {
        data = {"errno": 0, "data": res_data};
        return data;
    }

    if (this.isNull(err_msg)) {
        data = {"errno": errno, "errmsg": C.err_msg[errno], "data": res_data};
        return data;
    }
    data = {"errno": errno, "errmsg": err_msg, "data": res_data};
    return data;
}

exports.setQueryJson = function (fields, where, values, limit = '', order = '', use_redis = false, group = '', having = '') {
    return {
        'fields': fields,
        'where': where,
        'values': values,
        'limit': limit,
        'order': order,
        'use_redis': use_redis,
        'group': group,
        'having': having
    };
}

//判断是否为接口请求
exports.isAjax = function (ctx) {
    if ((ctx.request.accept.headers['x-requested-with'] && ctx.request.accept.headers['x-requested-with'].toLowerCase() == 'xmlhttprequest') || !this.isNull(ctx.request.header['dev-type']) || !this.isNull(ctx.I["dev-type"]) || !this.isNull(ctx.request.headers['device']) || !this.isNull(ctx.I["device"])) {
        return true;
    } else {
        return false;
    }
}

/**
 * 获取接口请求的客服端类型
 * @param ctx
 * @returns {*}
 */
exports.getDevType = function (ctx) {
    if (this.isNull(ctx)) return '';
    let devType = ctx.request.header['dev-type'];
    devType = this.isNull(devType) ? ctx.I["dev-type"] : devType;
    return this.isNull(devType) ? 10 : devType;
}

/**
 * 获取原图
 * @param fileName
 * @param isBlurry  如果为true则返回模糊图
 * @returns {*}
 */
exports.getOriginImgUrl = function (fileName, isBlurry = false, replace = '') {
    if (this.isNull(fileName)) return '';
    let cutLetter = fileName.substring(0, 6);
    if (cutLetter == 'http:/' || cutLetter == 'https:') {

    } else if (fileName.indexOf(C.pic_prefix) == 0) {

    } else {
        fileName = _.str.trim(fileName, '/');
        fileName = `${C.pic_prefix}${fileName}`;
    }
    let url = fileName;
    if (replace) {
        url = url.replace("/origin/", "/" + replace + "/");
    }
    if (isBlurry) {
        url = url.replace("/origin/", "/origin_blurry/");
        url = url.replace("/big/", "/big_blurry/");
        url = url.replace("/small/", "/small_blurry/");
        url = url.replace("/micro/", "/micro_blurry/");
        url = url.replace("/tiny/", "/tiny_blurry/");
        var path_list = url.split("/");
        var pic_name = path_list.pop();
        var name_list = pic_name.split(".");
        var ext = name_list.pop();
        var just_name = name_list.join(".");
        let suffix = md5.digest_s(just_name + C.blurry_key);
        path_list.push(suffix + "." + ext);
        url = path_list.join("/");
    }
    return url;
}

/**
 * 通过文件名获取文件url地址
 * @param fileName 文件名
 * @returns {*} url地址
 */
exports.getFileUrl = function (fileName) {
    if (this.isNull(fileName)) return '';
    let cutLetter = fileName.substring(0, 6);
    if (cutLetter == 'http:/' || cutLetter == 'https:') {

    } else if (fileName.indexOf(C.pic_prefix) == 0) {

    } else {
        fileName = _.str.trim(fileName, '/');
        fileName = `${C.pic_prefix}${fileName}`;
    }
    fileName = fileName.replace("/origin/", "/big/");
    fileName = fileName.replace("/origin_blurry", "/big_blurry");
    return fileName;
};

// 获取图片对应高分辨压缩图
exports.getHighThumb = function (fileName) {
    if (this.isNull(fileName)) return "";
    var url = this.getFileUrl(fileName);
    return url;
    url = url.replace("/origin/", "/big/");
    url = url.replace("/origin_blurry", "/big_blurry");
    return url;
};

// 获取图片对应较高分辨压缩图
exports.getBiggerMidThumb = function (fileName) {
    if (this.isNull(fileName)) return "";
    var url = this.getFileUrl(fileName);
    url = url.replace("/big/", "/middle/");
    url = url.replace("/big_blurry", "/middle_blurry");
    url = url.replace("/origin/", "/middle/");
    url = url.replace("/origin_blurry", "/middle_blurry");
    return url;
};

// 获取图片对应中分辨压缩图
exports.getMidThumb = function (fileName) {
    if (this.isNull(fileName)) return "";
    var url = this.getFileUrl(fileName);
    url = url.replace("/big/", "/small/");
    url = url.replace("/big_blurry", "/small_blurry");
    url = url.replace("/origin/", "/small/");
    url = url.replace("/origin_blurry", "/small_blurry");
    return url;
};

// 获取图片对应低分辨压缩图
exports.getLowThumb = function (fileName) {
    if (this.isNull(fileName)) return "";
    var url = this.getFileUrl(fileName);
    url = url.replace("/big/", "/micro/");
    url = url.replace("/big_blurry", "/micro_blurry");
    url = url.replace("/origin/", "/micro/");
    url = url.replace("/origin_blurry", "/micro_blurry");
    return url;
};

// 获取图片对应超低分辨压缩图
exports.getTinyThumb = function (fileName) {
    if (this.isNull(fileName)) return "";
    var url = this.getFileUrl(fileName);
    url = url.replace("/big/", "/tiny/");
    url = url.replace("/big_blurry", "/tiny_blurry");
    url = url.replace("/origin/", "/tiny/");
    url = url.replace("/origin_blurry", "/tiny_blurry");
    return url;
};

/**
 * 通过文件名获取图片模糊图url地址
 * @param fileName 文件名
 * @returns {*} url地址
 */
exports.getBlurryImg = function (fileName) {
    if (this.isNull(fileName)) {
        return '';
    }
    var url = this.getFileUrl(fileName);
    url = url.replace("/origin/", "/origin_blurry/");
    url = url.replace("/big/", "/big_blurry/");
    url = url.replace("/small/", "/small_blurry/");
    url = url.replace("/micro/", "/micro_blurry/");
    url = url.replace("/tiny/", "/tiny_blurry/");
    var path_list = url.split("/");
    var pic_name = path_list.pop();
    var name_list = pic_name.split(".");
    var ext = name_list.pop();
    var just_name = name_list.join(".");
    let suffix = md5.digest_s(just_name + C.blurry_key);
    path_list.push(suffix + "." + ext);
    url = path_list.join("/");
    return url;
};

/**
 * 通过文件名获取图片缩略图url地址
 * @param fileName 文件名
 * @returns {*} url地址
 */
exports.getImgThumb = function (fileName) {
    return this.getHighThumb(fileName);
};

/**
 * 通过文件名获取图片模糊图缩略图url地址
 * @param fileName 文件名
 * @returns {*} url地址
 */
exports.getBlurryImgThumb = function (fileName) {
    if (this.isNull(fileName)) {
        return '';
    }
    var url = this.getBlurryImg(fileName);
    return this.getHighThumb(url);
};


/**
 * 获取当前时间戳
 * @returns {Number} 时间戳 秒
 */
exports.time = function (str = '') {
    let date = new Date();
    if (!this.isNull(str)) {
        let [num, dateType] = str.split(' ');
        switch (dateType) {
            case 'month':
                date.setMonth(date.getMonth() + parseInt(num));
                break;
            case 'day':
                date.setDate(date.getDate() + parseInt(num));
                break;
        }
    }
    return parseInt(date.getTime() / 1000);
};

/**
 * 时间字符串转出时间戳
 * @returns {Number} 时间戳
 */
exports.strToTime = function (str) {
    return parseInt(new Date(str).getTime() / 1000);
};

/**
 * 根据时间戳计算，现在距离预约时间的时间
 * @param time
 * @returns {{d: number, h: number, m: number, s: number, difftime: number}}
 */
exports.getMisTime = function (time) {
    //time是服务器返回回来的时间戳，单位为妙
    //本地当前时间的时间戳，单位为毫秒，需要转换为妙 /1000
    var localTime = Math.floor((new Date().getTime()) / 1000);
    var difftime = time - localTime;//服务器时间与本地时间的时间差
    var d = Math.floor(difftime / 3600 / 24);
    var h = Math.floor(difftime / 3600 % 24);
    var m = Math.floor(((difftime % (3600 * 24)) % 3600) / 60);
    var s = (difftime % (3600 * 24)) % 60;
    var obj = {
        d: d,//时间天数位数字
        h: h,//时间小时位数字
        m: m,//时间分位数字
        s: s,//时间秒位数字
        difftime: difftime//时间差，秒级
    };
    return obj;
}

/**
 * 获取用户主播亲密度等级
 * @param intimacy 亲密度值
 * @returns {Number} 等级
 */
exports.getIntimacyLevel = function (intimacy) {
    let level = 0;
    if (intimacy >= 10000 && intimacy < 100000) {
        level = 1;
    } else if (intimacy >= 100000 && intimacy < 500000) {
        level = 2;
    } else if (intimacy >= 500000 && intimacy < 2000000) {
        level = 3;
    } else if (intimacy >= 2000000 && intimacy < 5000000) {
        level = 4;
    } else if (intimacy >= 5000000) {
        level = 5;
    }
    return level;
};

/**
 * 处理逗号分隔的枚举值
 * @param value 枚举值
 */
exports.handleEnum = function (value) {
    var statusList = [];
    value = value.split(',');
    for (let i = 0; i < value.length; i++) {
        value[i] = parseInt(value[i]);
        if (!isNaN(value[i])) {
            statusList.push(value[i]);
        }
    }
    if (this.isNull(statusList)) {
        this.throwErrCode(10001);
    } else {
        return statusList;
    }
};

exports.isTime = function (str) {
    var res = str.search(/^(\d{4}|\*)\-(\d{2}|\*)\-(\d{2}|\*) (\d{2}|\*):(\d{2}|\*):(\d{2}|\*)$/);
    return 0 == res;
};

exports.isInt = function (str) {
    str = str.toString();
    var res = str.search(/^(\d+)$/);
    return 0 == res;
};

/* 返回函数执行结果
* result true,false
* data result=true:返回数据
* msg result=false:返回说明
*/
exports.setFuncData = function (result, data = '') {
    if (result == false) {
        return {
            'result': false,
            'msg': data
        };
    } else {
        return {
            'result': true,
            'data': data
        };
    }
};

/* 返回manager返回结果
* result true,false
* data result=true:返回数据
*/
exports.setManagerData = function (result = true, data = '') {
    if (result == false) {
        return {
            'result': false,
            'errno': data.code,
            'errmsg': data.message
        };
    } else {
        let res = {'result': true};
        if (!this.isNull(data)) {
            res = Object.assign(res, data);
        }
        return res;
    }
};

/**
 * 根据出生年计算年龄
 * @param age
 * @returns {number}
 */
exports.countAge = function (age) {
    var returnAge;
    var birthYear = this.fDatetime('yyyy', age);
    var birthMonth = this.fDatetime('MM', age);
    var birthDay = this.fDatetime('dd', age);
    var nowYear = this.fDatetime('yyyy');
    var nowMonth = this.fDatetime('MM');
    var nowDay = this.fDatetime('dd');
    if (nowYear == birthYear) {
        returnAge = 0;
    }
    else {
        var ageDiff = nowYear - birthYear;
        if (ageDiff > 0) {
            if (nowMonth == birthMonth) {
                var dayDiff = nowDay - birthDay;
                if (dayDiff < 0) {
                    returnAge = ageDiff - 1;
                }
                else {
                    returnAge = ageDiff;
                }
            }
            else {
                var monthDiff = nowMonth - birthMonth;
                if (monthDiff < 0) {
                    returnAge = ageDiff - 1;
                }
                else {
                    returnAge = ageDiff;
                }
            }
        }
        else {
            returnAge = -1;
        }
    }
    return returnAge;

}

/**
 * 判断元素是否在数组中
 * @param item
 * @param array
 * @returns true/false
 */
exports.in_array = function (item, arr) {
    return _.v.isIn(item, arr);
};

/**
 * 对数组进行递归编码
 * @param arr
 * @returns {*}
 */
exports.batchHtmlEncode = function (arr) {
    if (!Array.isArray(arr)) {
        return '';
    }
    for (let index in arr) {
        if (Array.isArray(arr[index])) {
            arr[index] = this.batchHtmlEncode(arr[index]);
        } else {
            arr[index] = this.htmlEncode(arr[index]);
        }
    }
    return arr;
}

/**
 * 删除json中的html标签
 * @param data
 */
exports.rmHtmlAndJsByJson = function (data) {
    if (this.isNull(data)) return data;
    let json_str = JSON.stringify(data);
    json_str = this.rmHtmlAndJs(json_str);
    return JSON.parse(json_str);
}

/**
 * html编码
 * @param str
 * @returns {string}
 */
exports.htmlEncode = function (str) {
    var s = "";
    if (str.length == 0) return "";
    s = str.replace(/&/g, "&amp;");
    s = s.replace(/</g, "&lt;");
    s = s.replace(/>/g, "&gt;");
    s = s.replace(/ /g, "&nbsp;");
    //s = s.replace(/\'/g,"&#39;");
    //s = s.replace(/\"/g,"&quot;");
    return s;
}

/**
 * html解码
 * @param str
 * @returns {string}
 */
exports.htmlDecode = function (str) {
    var s = "";
    if (str.length == 0) return "";
    s = str.replace(/&amp;/g, "&");
    s = s.replace(/&lt;/g, "<");
    s = s.replace(/&gt;/g, ">");
    s = s.replace(/&nbsp;/g, " ");
    s = s.replace(/&#39;/g, "\'");
    s = s.replace(/&quot;/g, "\"");
    return s;
}

/**
 * 去除html标签和js代码
 */
exports.rmHtmlAndJs = function (str) {
    var res = str.replace(/<script>.*<\/script>/gi, "");
    res = res.replace(new RegExp("<([^>]+)>", 'g'), "〈$1〉");
    return res;
}

/**
 * 调用模板
 * @param ctx
 * @param url
 * @param res
 */
exports.render = function* (ctx, url, res) {
    var image_prefix = this.getImagePrefixByReqHost(ctx.I.real_ip, ctx.I.is_https);
    var request_prefix = this.getShareHost(ctx);
    res = this.replaceResJson(res, [C.pic_prefix, C.request_prefix], [image_prefix, request_prefix]);
    res = this.replaceCreditStr(res, ctx.credit_type);
    ctx.state = this.replaceResJson(ctx.state, [C.pic_prefix, C.request_prefix], [image_prefix, request_prefix]);
    if (url == 'broadcast/index') {
        //标示是否为index页面
        res.isIndex = true;
    } else {
        res.isIndex = false;
    }
    //CSS和JS的版本号
    res.version = '1.1.15';
    res.F = this;
    res.apptitleH5Page = this.isNull(res.apptitle) ? '' : res.apptitle;
    yield ctx.render(url, res);
}

/**
 * 获取动态视频上传的服务器类型
 * @returns {number}
 */
exports.getServerType = function () {
    return C.is_debug ? 1 : 0;
}

/**
 * 获取redis无轮询自增id
 * @param redis_co redisco句柄
 * @param uniid 自增id的标识
 */
exports.getNextNoRoundId = function* (redis_co, uniid) {
    var key = C.redisPre.pack_req_id_key;
    var next_id = yield redis_co.HINCRBY(key, uniid, 1);
    next_id = parseInt(next_id);
    return next_id;
};

/**
 * 请求验证码的key
 */
exports.getCodeKey = function () {
    let timestamp = this.timestamp();
    let code = this.getRandom(4);
    return md5.digest_s(timestamp + '_' + code);
}

/**
 * 获取redis中的验证码值
 * @param codeKey
 */
exports.getRedisCodeKey = function (codeKey) {
    return md5.digest_s(codeKey + '@#$Qw1g');
}

/**
 * 豆转信用点
 */
exports.getCreditByDou = function (dou) {
    return dou / C.credit_coefficient;
}

/**
 * 信用点转豆
 */
exports.getDouByCredit = function (credit) {
    return credit * C.credit_coefficient;
}

/**
 * 设置信用点规则字符串
 */
exports.setCreditStr = function (dou) {
    return _.str.vsprintf(C.credit_dou_match_str, [dou.toString()]);
}

/**
 * 替换信用点规则字符串
 * #credit_%s_preg#
 */
exports.replaceCreditStr = function (res, credit_type = 2) {
    return false;
    try {
        let str = JSON.stringify(res);
        let key = C.credit_dou_match_str.replace('%s', '[\\d|.|-]+');
        let match_res = str.match(new RegExp(key, 'g'));
        if (!this.isNull(match_res)) {
            let has_set = {};
            for (var i = 0; i < match_res.length; i++) {
                if (this.isNull(has_set[match_res[i]])) {
                    let number = match_res[i].match(/[\d|.|-]+/);
                    number = number[0];
                    if (credit_type == 1) {
                        number = this.getCreditByDou(number);
                    } else {
                        number = Math.round(number);
                    }
                    has_set[match_res[i]] = number;
                    str = str.replace(new RegExp('"' + match_res[i] + '"', 'g'), number);
                    str = str.replace(new RegExp('\\\"' + match_res[i] + '\\\"', 'g'), number);
                }
            }
        }

        return JSON.parse(str);
    } catch (e) {
        this.addErrLogs(['replaceCreditStr err', res, credit_type, e.stack]);
        return res;
    }
};

/**
 * 替换信用点单位规则字符串
 * #credit_unit_preg#
 */
exports.replaceCreditUnitStr = function (res, credit_type = 2) {
    try {
        let str = JSON.stringify(res);
        let unit = credit_type == 1 ? C.credit_unit_str : C.dou_unit_str;
        str = str.replace(new RegExp(C.credit_unit_match_str, 'g'), unit);
        return JSON.parse(str);
    } catch (e) {
        this.addErrLogs(['replaceCreditUnitStr err', res, credit_type, e.stack]);
        return res;
    }
};

/**
 * 获取请求地址
 * @param ctx
 * @returns {*}
 */
exports.getShareHost = function (ctx) {
    let protoc = 'http';
    if (!this.isNull(ctx.I.is_https)) protoc = "https";
    let ShareLink = this.vsprintf("%s://%s/", [protoc, ctx.I.real_ip]);
    return ShareLink;
}
/**
 * 获取分享地址
 * @param ctx
 * @returns {*}
 */
exports.getShareHostIm = function (req_host, is_https) {
    var proto = !this.isNull(is_https) ? "https" : "http";
    return this.vsprintf("%s://%s/", [proto, req_host]);
}


exports.isCharmDate = function (ctx) {
    if (this.isNull(ctx.I.real_ip)) return false;
    let host = ctx.I.real_ip.split(":");
    let ip = host[0];
    if (ip.indexOf('.charmlive.com') >= 0) return false;
    return true;
}

exports.initSession = function (ctx) {
    if (typeof ctx.session.length == 'number') {
        this.addLogs(['user visitorLogin session Array', ctx.session]);
        ctx.session = {};
    }
    let cookie = {
        "signed": false,
        "httpOnly": false,
        "path": "/",
        "overwrite": true
        //"maxage": 86400000,
    };
    if (this.isCharmDate(ctx)) cookie.maxage = 86400000;
    //if (!this.isNull(ctx.I.real_ip)) cookie.domain = ctx.I.real_ip;
    ctx.session.cookie = cookie;
}

/**
 * 获取主机信息
 * @param ctx
 * @returns {{http: string, websocket: string, ip: *, port: Number}}
 */
exports.getHostAndProtoc = function (ctx) {
    let host = ctx.I.real_ip.split(":");
    let ip = host[0];
    let port = parseInt(host[1]);
    if (host.length == 1) port = 80;
    let http = 'http';
    let websocket = 'ws';
    if (!this.isNull(ctx.I.is_https)) {
        if (host.length == 1) port = 443;
        http = "https";
        websocket = "wss";
    }
    return {'http': http, 'websocket': websocket, 'ip': ip, 'port': port};
}

/**
 * 获取主机地址
 * @param ctx
 * @returns {{http: string, websocket: string, ip: *, port: Number}}
 */
exports.getHostUrl = function (ctx) {
    let host = ctx.I.real_ip.split(":");
    let ip = host[0];
    let port = parseInt(host[1]);
    if (host.length == 1) port = 80;
    let http = 'http';
    let websocket = 'ws';
    if (!this.isNull(ctx.I.is_https)) {
        if (host.length == 1) port = 443;
        http = "https";
        websocket = "wss";
    }
    return this.vsprintf("%s://%s:%s", [http, ip, port]);
}

/**
 * 如果变量为空则返回null
 * @param parameter
 * @returns {null}
 */
exports.returnNull = function (parameter) {
    return this.isNull(parameter) ? null : parameter;
}

/**
 * judgeOrigin允许访问的URL地址集
 * @type {{[demo.charmdate.com]: number, [www.charmdate.com]: number, [192.168.8.242:94]: number, [192.168.88.17:8717]: number, [192.168.88.17:8817]: number, [live.charmdate.com]: number, [demo-live.charmdate.com]: number}}
 */
exports.origin_set = {
    'demo.charmdate.com': 1,
    'www.charmdate.com': 1,
    '192.168.8.242:94': 1,
    '192.168.88.17:8717': 1,
    '192.168.88.17:8817': 1,
    '192.168.88.17:3107': 1,
    '192.168.88.159:9090': 1,
    '192.168.88.95:9090': 1,
    '192.168.88.170:9090': 1,
    '192.168.88.78:9090': 1,
    '192.168.88.17:8117': 1,
    '192.168.88.40': 1,
    'live.charmdate.com': 1,
    'demo-live.charmdate.com': 1,
    'demo.charmlive.com': 1,
    'www.charmlive.com': 1
};

/**
 * 判断请求的origin是否为指定域名
 * @param ctx
 * @returns {number}
 */
exports.judgeOrigin = function (ctx) {
    if (this.isNull(ctx.req.headers['origin'])) return true;
    let origin = ctx.req.headers['origin'];
    origin = origin.split("://");
    let originHost = origin[1];
    if (!(originHost.indexOf('192.168.88') == -1) || originHost in this.origin_set) {
        return true;
    } else {
        this.throwErrCode(10001);
    }
}

/**
 * 计算字符串长度，中文算两个
 * @param str
 * @returns {number}
 */
exports.getByteLen = function (str) {
    var len = 0;
    for (var i = 0; i < str.length; i++) {
        var length = str.charCodeAt(i);
        if (length >= 0 && length <= 128) {
            len += 1;
        }
        else {
            len += 2;
        }
    }
    return len;
}

/**
 * H5页面url控制参数
 * @param ctx
 * @returns {*}
 */
exports.getH5UrlParameter = function (opentype, apptitle) {
    opentype = this.isNull(opentype) ? 0 : opentype;
    apptitle = this.isNull(apptitle) ? '' : apptitle;

    return `opentype=${opentype}&apptitle=${apptitle}`;
}

/**
 * 获取预约列表的预约时间
 * @returns {number}
 */
exports.getScheduleTime = function () {
    return this.time() - 120;
}

exports.toStr = function (id) {
    if (this.isNull(id)) return id;
    return id.toString();
}

/**
 * 去除字符串头尾的字符串
 * @param str
 * @param rep
 * @returns {*}
 */
exports.trim = function (str, rep) {
    return _.str.trim(str, rep);
}

/**
 * 获取day天后的7天内开始和结束时间戳
 * @param day   多少天后开始
 * @returns {{startTime: number, lastTime: number}}
 */
exports.getWeekTimestamps = function (day = 1, span = 6) {
    if (!this.isNull(C.is_debug)) {
        day = 0;
    }
    let time = this.time();
    let startTime = time + day * 24 * 3600;
    startTime = this.fDatetime('yyyy-MM-dd HH', startTime * 1000);
    let str = startTime + ':00:00';
    startTime = this.strToTime(str);//以小时为分界线

    let lastTime = startTime + span * 24 * 3600;
    let lastData = this.fDatetime('yyyy-MM-dd', lastTime * 1000);
    lastTime = this.strToTime(lastData + ' 24:00:00');//获取最后一天的完整时间

    return {'startTime': startTime, 'lastTime': lastTime};
}

/**
 * 验证我司后台修改节目状态时的key（鉴权用）
 * @param key
 * @param live_show_id
 */
exports.verLiveShowKey = function (key, live_show_id) {
    let codeKey = this.getRedisCodeKey(live_show_id);
    if (key != codeKey) {
        this.throwErrCode(13002);
    }
}

exports.coExec = thunkify(function (cmd, cb) {
    exec(cmd, function (err, stdout, stderr) {
        cb(stderr, stdout);
    });
});

exports.includesWithoutType = function (arr, a) {
    for (let i = 0; i < arr.length; i++) {
        if (a == arr[i]) return true;
    }
    return false;
}

/**
 * 获取指定月的第一天
 * @param date
 * @returns {Date}
 */
exports.getCurrentMonthFirst = function (date) {
    var date = new Date(date);
    date.setDate(1);
    return date;
}

/**
 * 获取指定月份的最后一天
 * @param date
 * @returns {Date}
 */
exports.getCurrentMonthLast = function (date) {
    var date = new Date(date);
    var currentMonth = date.getMonth();
    var nextMonth = ++currentMonth;
    var nextMonthFirstDay = new Date(date.getFullYear(), nextMonth, 1);
    var oneDay = 1000 * 60 * 60 * 24;
    return new Date(nextMonthFirstDay - oneDay);
}

exports.cipher = function (buf) {
    let algorithm = "aes-128-cbc";
    var encrypted = "";
    var key = new Buffer(C.aes_key);
    let iv = new Buffer(C.aes_iv);
    var cip = crypto.createCipheriv(algorithm, key, iv);
    //cip.setAutoPadding(false);
    // buf = customPadding(buf);
    encrypted += cip.update(buf, 'utf8', 'base64');
    encrypted += cip.final('base64');
    return encrypted;
}

exports.decipher = function (encrypted) {
    let algorithm = "aes-128-cbc";
    var decrypted = "";
    var key = new Buffer(C.aes_key);
    let iv = new Buffer(C.aes_iv);
    var decipher = crypto.createDecipheriv(algorithm, key, iv);
    //decipher.setAutoPadding(false);
    decrypted += decipher.update(encrypted, 'base64', 'utf8');
    decrypted += decipher.final('utf8');
    return decrypted;
}

/*根据经纬度获取附近几公里的经纬度范围*/
exports.getMaxMinLongitudeLatitude = function(longitude,latitude,distince){
    console.log("MaxMinLongitudeLatitude",longitude,latitude);
    let r = 6371.393;    // 地球半径千米
    let lng = longitude;
    let lat = latitude;
    let dlng = 2 * Math.asin(Math.sin(distince / (2 * r)) / Math.cos(lat * Math.PI / 180));
    dlng = dlng * 180 / Math.PI;// 角度转为弧度
    let dlat = distince / r;
    dlat = dlat * 180 / Math.PI;
    let minlat = lat - dlat;
    let maxlat = lat + dlat;
    let minlng = lng - dlng;
    let maxlng = lng + dlng;
    return {
        minlng:minlng.toString(),
        maxlng:maxlng.toString(),
        minlat:minlat.toString(),
        maxlat:maxlat.toString()
    }
}
/*计算两个经纬度之间的距离*/
exports.caculateLL = function(lat1, lng1, lat2, lng2) {
    var radLat1 = lat1 * Math.PI / 180.0;
    var radLat2 = lat2 * Math.PI / 180.0;
    var a = radLat1 - radLat2;
    var b = lng1 * Math.PI / 180.0 - lng2 * Math.PI / 180.0;
    var s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
    s = s * 6378.137;
    s = Math.round(s * 10000) / 10000;
    return s
}
/*对象数组去重*/
exports.uniqueObjectArrary = function (arr,prop) {
    let dictionary = {};
    let result = [];
    if(arr == null || arr.length == 0 || prop == null){
        return result;
    }
    for(let i = 0;i<arr.length;i++){
        dictionary[arr[i][prop]] = arr[i];
    }
    for(let j=0;j<Object.keys(dictionary).length;j++){
        result.push(dictionary[Object.keys(dictionary)[j]]);
    }
    return result;
}