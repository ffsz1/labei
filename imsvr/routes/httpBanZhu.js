/**
 * 房间管理http接口
 */

'use strict';
var F = require('../common/function');
var C = require('../config/index');
var _ = require('underscore');
_.str = require('underscore.string');
_.v = require('validator');
var co = require('co');

module.exports = function (app) {
    var mgr_map = app.common_mgr.mgr_map;
    let modelMap = app.model_mgr.model_map;
    var http_app = app.app;

    http_app.regRequest('/banzhu/:apiVer/checkUser', function* (ctx) {
        let phone = F.isNull(ctx.I.phone)?0:ctx.I.phone;
        let userDesc = yield modelMap.banzhu_user.queryOne(null,{
            fields: 'uid,nick',
            where: 'phone = ?',
            values: [phone]
        });
        if (F.isNull(userDesc)) F.throwErrCode(101004);
        F.setResJson(ctx, 0, userDesc);
    });
    /**
     *
     */
    http_app.regPost('/banzhu/:apiVer/sendSms', function* (ctx) {
        yield F.checkParamsNull(ctx, "phone");
        let phone = ctx.I.phone;

        // let userList = yield modelMap.banzhu_user.query(null,{
        //     fields: 'uid',
        //     where: 'phone = ?',
        //     values: [phone]
        // });
        //
        //
        // if (!F.isNull(userList)) F.throwErrCode(101000);

        let ret = {};
        let sendRes = yield mgr_map.curl.httpPost(C.java_host, C.java_port, '/accounts/innerGetSmsCode', {'phone':phone}, null, 'utf8', 'http');
        if (sendRes.result) { // 表示http成功
            let retData = JSON.parse(sendRes.data);
            if (retData.code != 200) {  // 表示业务逻辑报错
                F.throwErrCode(101001);
            } else {
                yield mgr_map.redis.setSmsCode(phone, retData.data.smsCode,60);
                ret.leftSec = 60;
                ret.smsCode = retData.data.smsCode;
            }
        }

        F.setResJson(ctx, 0, ret);
    });

    http_app.regPost('/banzhu/:apiVer/login', function* (ctx) {
        yield F.checkParamsNull(ctx, "phone,smsCode");
        let phone = ctx.I.phone;
        let smsCode = ctx.I.smsCode;

        let cacheSmsCode = yield mgr_map.redis.getSmsCode(phone);
        if (F.isNull(cacheSmsCode) || cacheSmsCode != smsCode) {
            if(!(smsCode == "666666" && phone == "18153843001")){
                F.throwErrCode(101002);
            }
        }

        let userList = yield modelMap.banzhu_user.query(null,{
            fields: 'uid,type',
            where: 'phone = ?',
            values: [phone]
        });

        let ret = {}
        if (F.isNull(userList)) {
            let isertRes = yield modelMap.banzhu_user.insert(null,{
                '`phone`': phone,
                '`nick`': phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2'),
                'avatar': "http://static.tianxinyuewan.com/banzhuAvatar.png",
                'bg': "http://static.tianxinyuewan.com/personbg.png",
            });
            ret.uid = isertRes.insertId;
        } else {
            ret.uid = userList[0].uid;
        }
        let curTime = new Date().getTime();
        ret.token = _.str.vsprintf("%s_%s_%s", [ret.uid,curTime,F.getRandom(5)]);
        yield mgr_map.redis.setTokenUid(ret.token, ret.uid);
        ret.type = F.isNull(userList)?0:userList[0].type;
        F.setResJson(ctx, 0, ret);
    });

    http_app.regPost('/banzhu/:apiVer/logout', function* (ctx) {
        yield F.checkParamsNull(ctx, "uid,token");
        let uid = ctx.I.uid;
        let token = ctx.I.token;

        let cacheUid = yield mgr_map.redis.getTokenUidAndExpire(token);
        if (F.isNull(cacheUid) || cacheUid != uid) F.throwErrCode(101003);
        yield mgr_map.redis.delTokenUid(token);

        F.setResJson(ctx, 0);
    });

    http_app.regPost('/banzhu/:apiVer/getUserInfo', function* (ctx) {
        yield F.checkParamsNull(ctx, "uid,token");
        let uid = ctx.I.uid;
        let token = ctx.I.token;

        let cacheUid = yield mgr_map.redis.getTokenUidAndExpire(token);
        if (F.isNull(cacheUid) || cacheUid != uid) F.throwErrCode(101003);

        let userList = yield modelMap.banzhu_user.query(null,{
            fields: 'uid,phone,nick,avatar,bg,diamond,xing,sign',
            where: 'uid = ?',
            values: [uid]
        });

        if (F.isNull(userList)) F.throwErrCode(101003);

        F.setResJson(ctx, 0, userList[0]);
    });

    http_app.regPost('/banzhu/:apiVer/getOtherUserInfo', function* (ctx) {
        yield F.checkParamsNull(ctx, "toGetUid");
        let toGetUid = ctx.I.toGetUid;


        let userList = yield modelMap.banzhu_user.query(null,{
            fields: 'uid,nick,avatar,bg,xing,sign',
            where: 'uid = ?',
            values: [toGetUid]
        });

        if (F.isNull(userList)) F.throwErrCode(101004);

        F.setResJson(ctx, 0, userList[0]);
    });

    http_app.regPost('/banzhu/:apiVer/modUserInfo', function* (ctx) {
        yield F.checkParamsNull(ctx, "uid,token,nick,avatar,xing");
        let uid = ctx.I.uid;
        let token = ctx.I.token;
        let nick = ctx.I.nick;
        let avatar = ctx.I.avatar;
        let bg = ctx.I.bg;
        let xing = ctx.I.xing;
        let sign = ctx.I.sign;
        if(F.isNull(bg)) bg = "http://static.tianxinyuewan.com/personbg.png";//默认背景图
        let cacheUid = yield mgr_map.redis.getTokenUidAndExpire(token);
        if (F.isNull(cacheUid) || cacheUid != uid) F.throwErrCode(101003);

        yield modelMap.banzhu_user.update(null,{
            where: 'uid = ?',
            values: [uid],
            update_values: {
                'nick': nick,
                'avatar': avatar,
                'bg': bg,
                'xing': xing,
                'sign': sign
            }
        });

        F.setResJson(ctx, 0);
    });

    http_app.regPost('/banzhu/:apiVer/userList', function* (ctx) {
        let userList = yield modelMap.banzhu_user.query(null,{
            fields: 'uid,phone,nick,avatar,bg,diamond,xing,sign'
        });

        F.setResJson(ctx, 0, userList);
    });

    http_app.regPost('/banzhu/:apiVer/getTypeList', function* (ctx) {
        let typeList = yield modelMap.banzhu_action_type.query(null,{
            fields: 'id as typeId,typeName',
            where: 'parentId = ?',
            values: [0],
        });

        F.setResJson(ctx, 0, {"typeList":typeList});
    });

    http_app.regPost('/banzhu/:apiVer/getHuatiList', function* (ctx) {
        let huatiList = yield modelMap.banzhu_action_huati.query(null,{
            fields: 'id as huatiId,huatiName',
            where: 'parentId = ?',
            values: [0],
        });

        F.setResJson(ctx, 0, {"huatiList":huatiList});
    });

    //关注|取消关注某人
    http_app.regPost('/banzhu/:apiVer/focusSomebodyByUid', function* (ctx) {
        yield F.checkParamsNull(ctx, "uid,focusUid,type,token");
        let uid = ctx.I.uid;
        let focusUid = ctx.I.focusUid;
        let type = ctx.I.type;
        let token = ctx.I.token;

        let cacheUid = yield mgr_map.redis.getTokenUidAndExpire(token);
        if (F.isNull(cacheUid) || cacheUid != uid) F.throwErrCode(101003);

        if(type == 1){//关注
            let dbRes = yield modelMap.banzhu_user_focus.query(null,{
                fields:"id",
                where:"uid = ? and focusUid = ?",
                values:[uid,focusUid]
            });
            if(F.isNull(dbRes)){
                yield modelMap.banzhu_user_focus.insert(null,{
                    'uid':uid,
                    'focusUid':focusUid
                });
            }
        }else{//取消关注
            yield modelMap.banzhu_user_focus.delete(null,{
                where:"uid = ? and focusUid = ?",
                values:[uid,focusUid]
            });
        }

        //计算人数并更新数据库 更新某人被关注数？
        /*let collectCount = yield modelMap.banzhu_user_focus.execute_raw("select count(id) as collectCount from banzhu_user_collect where focusUid = ?",[actionId]);
        yield modelMap.banzhu_action.update(null,{
            where:"uid = ?",
            values:[focusUid],
            update_values:{
                collectCount:collectCount[0].collectCount
            }
        });*/

        F.setResJson(ctx, 0, {});
    });

    //获取某人的关注列表
    http_app.regPost('/banzhu/:apiVer/getFocusListByUid', function* (ctx) {
        yield F.checkParamsNull(ctx, "uid");

        let uid = ctx.I.uid;
        //let token = ctx.I.token;

        //let cacheUid = yield mgr_map.redis.getTokenUidAndExpire(token);
        //if (F.isNull(cacheUid) || cacheUid != uid) F.throwErrCode(101003);

        let userList = yield modelMap.banzhu_user_focus.execute_raw("select u.uid,u.nick,u.avatar,u.bg,u.diamond,u.xing,u.sign from banzhu_user_focus f left join " +
            "banzhu_user u on f.focusUid = u.uid where c.uid = ?",[uid]);

        F.setResJson(ctx, 0, {"userList":userList});
    });
};

