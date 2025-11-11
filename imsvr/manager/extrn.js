/**
 * 直播间男女端共用Manager
 */
'use strict';
const F = require('../common/function');
const C = require('../config');
const _ = require('underscore');
_.str = require('underscore.string');
_.v = require('validator');
const co = require('co');

module.exports = function (app, commonManager) {
    let mgr_map = commonManager.mgr_map;
    let that = this;

    /**
     * 获取zego的accessToken
     */
    this.getZegoAccessToken = function* () {
        let res = yield mgr_map.curl.httpGet(C.java_host, C.java_port, '/zego/v1/getAccessToken', {}, null, 'utf8', 'http');
        if (!res.result) {
            F.throwErr("获取access_token为空");
        }
        let json = JSON.parse(res.data);
        if (json.code != 200 || F.isNull(json.data)) {
            return "";
        }
        return json.data.accessToken;
    };

    this.delZegoStream = function* (room_id, uid) {
        try {
            let stream_id = yield mgr_map.redis.getStreamId(uid);
            if (F.isNull(stream_id)) {
                return;
            }

            let stream_sid = yield mgr_map.redis.getStreamSvrId(uid);
            if (F.isNull(stream_sid)) {
                return;
            }

            let access_token = yield that.getZegoAccessToken();
            if (F.isNull(access_token)) {
                return;
            }
            let req = {
                "access_token": access_token,
                "version": 1,
                "seq": F.timestamp(),
                "room_id": room_id.toString(),
                "user_account": "1",
                "stream_id": stream_id,
                "stream_sid": ""
            };
            let res = yield mgr_map.curl.httpPostJson(C.zego_host, 443, '/cgi/delstream', req, null, 'utf8', 'https');
            F.addDebugLogs(["[ 即构接口 ]后台流删除接口", req, res]);
        } catch (e) {
            F.addErrLogs(["[ 即构接口 ]后台流删除接口异常", e.stack]);
        }
    };

    this.addZegoStream = function* (room_id, uid, appCode) {
        /*if ("10010128" == room_id || "10001950" == room_id) return;
        try {
            let stream_id = yield mgr_map.redis.getStreamId(uid);
            if (F.isNull(stream_id)) {
                return;
            }

            let stream_sid = yield mgr_map.redis.getStreamSvrId(uid);
            if (F.isNull(stream_sid)) {
                return;
            }

            let access_token = yield that.getZegoAccessToken();
            if (F.isNull(access_token)) {
                return;
            }
            if (!F.isNull(appCode) && 2 <= parseInt(appCode)) return;
            let req = {
                "access_token": access_token,
                "version": 1,
                "seq": F.timestamp(),
                "room_id": room_id.toString(),
                "user_account": uid.toString(),
                "stream_id": stream_id,
                "stream_sid": "",
                "extra_info": "" + F.timestamp(),
                "title": "svrpub"+F.timestamp()
            };
            let res = yield mgr_map.curl.httpPostJson(C.zego_host, 443, '/cgi/addstream', req, null, 'utf8', 'https');
            F.addDebugLogs(["[ 即构接口 ]后台流增加接口", req, res]);
        } catch (e) {
            F.addErrLogs(["[ 即构接口 ]后台流增加接口异常", e.stack]);
        }*/

    };

};

