'use strict';

const F = require('../common/function');
const request = require('request');
const _ = require('underscore');
_.str = require('underscore.string');
var httplib = require('http');
var httpslib = require('https');
httplib.globalAgent.keepAlive  = true;
//httplib.globalAgent.maxSockets = 100000;
httpslib.globalAgent.keepAlive  = true;
//httpslib.globalAgent.maxSockets = 100000;
var crypto = require('crypto');

var C = require('../config');

module.exports = function (app, commonManager) {


    var mgr_map = commonManager.mgr_map;

    var that = this;

    this.genSignature = function(secretKey,paramsJson){
        var sorter=function(paramsJson){
            var sortedJson={};
            var sortedKeys=Object.keys(paramsJson).sort();
            for(var i=0;i<sortedKeys.length;i++){
                sortedJson[sortedKeys[i]] = paramsJson[sortedKeys[i]]
            }
            return sortedJson;
        }
        var sortedParam=sorter(paramsJson);
        var needSignatureStr="";
        for(var key in sortedParam){
            var value=sortedParam[key];
            needSignatureStr=needSignatureStr+key+value;
        }
        needSignatureStr+=secretKey;
        var md5er = crypto.createHash('md5');//MD5加密工具
        md5er.update(needSignatureStr,"UTF-8");
        return md5er.digest('hex');
    };

    this.checkWord = function* (uid,str) {
        var secretId="3339b3995ab6d00f78f90b8acbb46105";
        // 产品私有密钥，服务端生成签名信息使用，请严格保管，避免泄露
        var secretKey="ee033b2ac930f598be0f2b996fb42f54";
        // 业务ID，易盾根据产品业务特点分配
        var businessId="eabbd79ecce22096d16fe95a19143e3e";
        // 易盾内容安全服务文本在线检测接口地址
        var apiurl="https://as.dun.163yun.com/v3/text/check";
        //请求参数
        var post_data = {
            // 1.设置公有有参数
            secretId:secretId,
            businessId:businessId,
            version:"v3.1",
            timestamp:new Date().getTime(),
            nonce:F.getRandom(9),
            // 2.设置私有参数
            dataId:"xbd"+F.getRandom(9)+new Date().getTime(),
            content:str,
            ip:C.inner_host,
            dataType:"1",
            account:uid,
            deviceType:"4",
            // deviceId:"92B1E5AA-4C3D-4565-A8C2-86Exbd58",
            callback:"exbd-dba1-490c-b4de-e784c2691768",
            publishTime:new Date().getTime()
        };
        var signature=that.genSignature(secretKey,post_data);
        post_data.signature=signature;
        let get_res = yield mgr_map.curl.httpPost("as.dun.163yun.com", 443, '/v3/text/check', post_data, null, 'utf8', 'https');
        console.log("get_res",get_res);
        if (get_res.result) { // 表示http成功
            let get_user_res = JSON.parse(get_res.data);
            if (get_user_res.code != 200) {  // 表示业务逻辑报错
                F.addOtherLogs("yundun/yundun", ["表示业务逻辑报错 返回:",get_res]);
                return 2;
            }

            let res_data = JSON.parse(get_res.data);
            let result = res_data.result;
            let taskId=result.taskId;
            let action=result.action;
            let labelArray=result.labels;
            if(action==0){
                result = 1;
                return result;
            }else if(action==1){
                let errstr = "文本机器检测结果：嫌疑，需人工复审，分类信息如下："+JSON.stringify(labelArray);
                F.addOtherLogs("yundun/yundun", ["敏感词识别:",str,"action="+action+"taskId="+taskId+","+errstr]);
                result = 2;
                return result;
            }else if(action==2){
                let errstr = "文本机器检测结果：不通过，分类信息如下："+JSON.stringify(labelArray);
                F.addOtherLogs("yundun/yundun", ["敏感词识别:",str,"action="+action+"taskId="+taskId+","+errstr]);
                result = 2;
                return result;
            }
        } else { // 表示http超时或返回非200
            F.addOtherLogs("yundun/yundun", ["表示http超时或返回非200 返回:",get_res]);
            return 2;
        }
        return result;
    };


    /**
     * post操作
     * @param url url地址
     * @param data 数据
     * @returns {*}
     */
    this.post = function* (url, data, headers={}) {
        let res = yield sendRequest(url, data, 'post', headers);
        F.addDebugLogs(["send post:",url,data,headers,res]);
        return res;
    };

    /**
     * get操作
     * @param url url地址
     * @returns {*}
     */
    this.get = function* (url, headers={}) {
        let res = yield sendRequest(url, {}, 'get', headers);
        F.addDebugLogs(["send get:",url,headers,res]);
        return res;
    };

    /**
     * 同时支持http https 和设置返回编码的网络库
     * @param host 127.0.0.1
     * @param port 8081
     * @param route /share/v1/getconfig
     * @param params {userid:xxx,username:xxx}
     * @param headers {}
     * @param encoding binary/utf8
     * @param protoc http/https
     * @param timeout_time
     * @returns {*}
     */
    this.httpGet = function* (host, port, route, params = '', headers = {}, encoding = 'utf8', protoc = 'http', timeout_time = 20000) {
        let start_time = new Date().getTime();
        let curl_res = yield sendHttpRequest('GET', host, port, route, params, headers, encoding, protoc, timeout_time);
        let end_times = new Date().getTime();
        F.addOtherLogs("curl/curl", [route, params, ' curl res', {curl_res: curl_res,  total_time: end_times - start_time}]);
        return curl_res;
    };

    this.httpPost = function* (host, port, route, params = '', headers = {}, encoding = 'utf8', protoc = 'http', timeout_time = 20000) {
        let start_time = new Date().getTime();
        let curl_res = yield sendHttpRequest('POST', host, port, route, params, headers, encoding, protoc, timeout_time);
        let end_times = new Date().getTime();
        F.addOtherLogs("curl/curl", [route, params, ' curl res', {curl_res: curl_res,  total_time: end_times - start_time}]);
        return curl_res;
    };

    this.httpPostJson = function* (host, port, route, params = '', headers = {}, encoding = 'utf8', protoc = 'http', timeout_time = 20000) {
        let start_time = new Date().getTime();
        let curl_res = yield sendHttpRequest('POST', host, port, route, params, headers, encoding, protoc, timeout_time,true);
        let end_times = new Date().getTime();
        F.addOtherLogs("curl/curl", [route, params, ' curl res', {curl_res: curl_res,  total_time: end_times - start_time}]);
        return curl_res;
    };

    /**
     * http模块get操作,不打印日志
     * @param url url地址
     * @returns {*}
     */
    this.httpGetNoLogs = function* (host, port, route, params = '', headers = {}, encoding = 'utf8', protoc = 'http', timeout_time = 20000) {
        let curl_res = yield sendHttpRequest('GET', host, port, route, params, headers, encoding, protoc, timeout_time);
        return curl_res;
    };

};

/**
 * curl发送请求
 * @param url url地址
 * @param params 参数
 * @param type 请求类型
 */
function sendRequest(url, params, type = 'post', headers = {}) {
    type = type == 'post' ? 'post' : 'get';
    params = F.isNull(params) ? {} : params;
    return new Promise(function (resolve, reject) {
        let send_data = {method: type, url: url, timeout: 20000};
        if (type=='post') send_data.form = params;
        if (!F.isNull(headers)) send_data.headers = headers;
        request(send_data, function (error, response, body) {
            var res = {};
            if (error) {
                F.addErrLogs(["curl err:",error,response,body]);
                res = {result: 0, message: error, code:502};
            } else if (response.statusCode != 200) {
                res = {result: 0, message: response.body, code:response.statusCode};
            } else {
                res = {result: 1, data: body, code:200};
            }
            resolve(res);
        });
    });
}

/**
 * http/https模块发送请求
 * @param host
 * @param port
 * @param route
 * @param params 参数
 * @param headers
 * @param encoding 可选值： utf8 binary
 */
function sendHttpRequest(type, host, port, route, params = {}, headers = {}, encoding = 'utf8', protoc = 'http', timeout_time = 20000,is_json=false) {
    if (F.isNull(headers)) headers = {};
    if (F.isNull(params)) params = {};
    let http = protoc == 'http'? httplib: httpslib;
    let params_str = '';
    for(let key in params) {
        params_str += key + '=' + encodeURIComponent(params[key]) + '&';
    }
    params_str = _.str.trim(params_str, '&');

    if (type == 'GET') {
        if (route.indexOf('?') == -1) route = route + '?' + params_str;
        else route = route + '&' + params_str;
    } else if (type == 'POST') {
        if (is_json){
            headers["Content-Type"] = "application/json;charset=utf-8";
            params_str = JSON.stringify(params);
        } else {
            headers["Content-Type"] = "application/x-www-form-urlencoded;charset=utf-8";
        }
    }
    headers["Connection"] = "keep-alive";
    let options = {
        hostname: host,
        port: port,
        path: route,
        method: type,
        headers: headers
    };
    if (protoc == "https") options.rejectUnauthorized = false;
    // F.addDebugLogs(['sendHttpRequest option', {options: options, start_time: new Date().getTime()},params_str]);
    let data = '';
    return new Promise(function (resolve, reject) {
        let timeoutEventId = 0;
        let req = http.request(options, function(res) {
            res.setEncoding(encoding);
            res.on('data', function(chunk) {
                data += chunk;
            });

            res.on('end', function() {
                clearTimeout(timeoutEventId);
                resolve({result: true, data: data});
            });

            res.on('abort',function(){
                clearTimeout(timeoutEventId);
                F.addDebugLogs(["exe timeout abort"]);
            })
        });

        req.on('timeout',function(e){
            req.abort();
            F.addDebugLogs(["exe timeout event"]);
            resolve({result: false, timeout: 1, errmsg:"timeout"});
        });

        req.on('error', (e) => {
            clearTimeout(timeoutEventId);
            resolve({result: false, errmsg: e.message});
        });

        if (type == 'POST') req.write(params_str);
        req.end();

        timeoutEventId=setTimeout(function(){
            F.addDebugLogs(["exe timeout",timeout_time]);
            req.emit('timeout',{message:'have been timeout...'});
        },timeout_time);
    });
}
