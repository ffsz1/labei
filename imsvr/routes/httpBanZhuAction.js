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
    let actionFields = 'id as actionId,uid,typeId,typeName,title,actionTime,actionEndTime,address,peopleNum,fee,femalefee,isPublic,`desc`,haibao_list,pic_list,collectCount,lat,lng';


    //发布活动
    http_app.regPost('/banzhu/:apiVer/addAction', function* (ctx) {
        yield F.checkParamsNull(ctx, "token,uid,typeId,typeName,haibao_list,title,actionTime,actionEndTime,address,peopleNum,fee,isPublic");
        let domain = F.isNull(ctx.I.domain) ? "" : ctx.I.domain;
        let token = ctx.I.token;
        let uid = ctx.I.uid;
        if(uid==0){
            F.throwErr("uid can't not be null");
        }
        let haibao_list = ctx.I.haibao_list;
        let pic_list = ctx.I.pic_list;
        let title = ctx.I.title;
        let actionTime = ctx.I.actionTime;
        let actionEndTime = ctx.I.actionEndTime;
        let address = ctx.I.address;
        let peopleNum = ctx.I.peopleNum;
        let fee = ctx.I.fee;
        let isPublic = ctx.I.isPublic;
        let desc = ctx.I.desc;
        let typeId = ctx.I.typeId;
        let typeName = ctx.I.typeName;
        let femalefee = ctx.I.femalefee;
        let lat = ctx.I.lat;//纬度
        let lng = ctx.I.lng;//经度

        let cacheUid = yield mgr_map.redis.getTokenUidAndExpire(token);
        if (F.isNull(cacheUid) || cacheUid != uid) F.throwErrCode(101003);

        if(F.isNull(desc) && F.isNull(pic_list)){
            F.throwErr("详情和详情图片不能同时为空!")
        }
        if(new Date(actionTime).getTime()>new Date(actionEndTime).getTime()){
            F.throwErrCode(101017);
        }

        if(F.isNull(femalefee)) femalefee = 0;
        if(F.isNull(fee)) fee = 0;

        let inserValues = {
            'domain': domain,
            'haibao_list': haibao_list,
            'pic_list': pic_list,
            'title': title,
            'actionTime': actionTime,
            'actionEndTime': actionEndTime,
            'address': address,
            'peopleNum': peopleNum,
            'fee': fee,
            'isPublic': isPublic,
            '`desc`': desc,
            'typeId': typeId,
            'typeName': typeName,
            'uid': uid,
            'femalefee': femalefee
        }

        if(!F.isNull(lat) && !F.isNull(lng)){
            inserValues.lat = lat;
            inserValues.lng = lng;
        }

        let insertRes = yield modelMap.banzhu_action.insert(null,inserValues);
        let ret = {};
        ret.actionId = insertRes.insertId;
        F.setResJson(ctx, 0, ret);
    });

    //更新活动信息
    http_app.regPost('/banzhu/:apiVer/updateActionByActionId', function* (ctx) {
        yield F.checkParamsNull(ctx, "token,uid,typeId,typeName,haibao_list,title,actionTime,actionEndTime,address,peopleNum,fee,isPublic,actionId");
        let domain = F.isNull(ctx.I.domain) ? "" : ctx.I.domain;
        let token = ctx.I.token;
        let uid = ctx.I.uid;
        if(uid==0){
            F.throwErr("uid can't not be null");
        }
        let haibao_list = ctx.I.haibao_list;
        let pic_list = ctx.I.pic_list;
        let title = ctx.I.title;
        let actionTime = ctx.I.actionTime;
        let actionEndTime = ctx.I.actionEndTime;
        let address = ctx.I.address;
        let peopleNum = ctx.I.peopleNum;
        let fee = ctx.I.fee;
        let isPublic = ctx.I.isPublic;
        let desc = ctx.I.desc;
        let typeId = ctx.I.typeId;
        let typeName = ctx.I.typeName;
        let actionId = ctx.I.actionId;
        let femalefee = ctx.I.femalefee;
        let lat = ctx.I.lat;
        let lng = ctx.I.lng;

        let cacheUid = yield mgr_map.redis.getTokenUidAndExpire(token);
        if (F.isNull(cacheUid) || cacheUid != uid) F.throwErrCode(101003);

        if(F.isNull(desc) && F.isNull(pic_list)){
            F.throwErr("详情和详情图片不能同时为空!")
        }

        let action = yield modelMap.banzhu_action.queryOne(null,{
            fields: actionFields,
            where: "id = ?",
            values: [actionId]
        })

        if(F.isNull(action)) F.throwErr(101011)//该活动已删除
        if(action.uid!=uid) F.throwErr(101016);//没有权限操作

        let update_values = {
            'domain': domain,
            'haibao_list': haibao_list,
            'pic_list': pic_list,
            'title': title,
            'actionTime': actionTime,
            'actionEndTime': actionEndTime,
            'address': address,
            'peopleNum': peopleNum,
            'fee': fee,
            'isPublic': isPublic,
            '`desc`': desc,
            'typeId': typeId,
            'typeName': typeName,
            'uid': uid,
            'femalefee':femalefee,
        }

        if(!F.isNull(lat) && !F.isNull(lng)){
            update_values.lat = lat;
            update_values.lng = lng;
        }

        yield modelMap.banzhu_action.update(null,{
            where: 'id = ?',
            values: [actionId],
            update_values: update_values
        });

        F.setResJson(ctx, 0, {});
    });



    //获取活动列表
    http_app.regPost('/banzhu/:apiVer/getActionList', function* (ctx) {
        yield F.checkParamsNull(ctx, "type");
        let type = ctx.I.type;
        let domain = F.isNull(ctx.I.domain) ? "" : ctx.I.domain;
        let token = ctx.I.token;
        let uid = ctx.I.uid;
        if(!F.isNull(token) && !F.isNull(uid)){
            let cacheUid = yield mgr_map.redis.getTokenUidAndExpire(token);
            //if (F.isNull(cacheUid) || cacheUid != uid) F.throwErrCode(101003);
            if (!F.isNull(cacheUid) && cacheUid == uid) yield mgr_map.redis.setTokenUid(token,uid);//延长token时间
        }

        let queryCond = {
            fields: actionFields,
            where: "1=1",
            values: [],
            order: "priority desc,id desc"
        }

        if (!F.isNull(domain)) {
            queryCond.where += " and domain = ?";
            queryCond.values.push(domain);
        }
        let actionList = [];

        if (type == 1 && !F.isNull(ctx.I.uid)) {
            queryCond.where += " and uid = ?";
            queryCond.values.push(ctx.I.uid);
            actionList = yield modelMap.banzhu_action.query(null, queryCond);
        } else if (type == 2 && !F.isNull(ctx.I.uid)){
            actionList = yield modelMap.banzhu_renling.execute_raw(
                "select a.id as actionId,a.uid,typeId,typeName,title,actionTime,actionEndTime,address,peopleNum,fee,femalefee,isPublic,`desc`,haibao_list,pic_list,collectCount,b.hasPay "+
            "from banzhu_action a LEFT JOIN banzhu_action_bao b on a.id = b.actionId where b.uid = ? order by a.priority desc,a.id desc",[ctx.I.uid]);
        } else {
            queryCond.where += " and isPublic = ?";
            queryCond.values.push(1);
            actionList = yield modelMap.banzhu_action.query(null, queryCond);
        }

        F.setResJson(ctx, 0, actionList);
    });


    //获取活动详情
    http_app.regPost('/banzhu/:apiVer/getActionDetail', function* (ctx) {
        yield F.checkParamsNull(ctx, "actionId");
        let actionId = ctx.I.actionId;
        let uid = ctx.I.uid;

        let actionDesc = yield modelMap.banzhu_action.queryOne(null,{
            fields: actionFields,
            where: 'id = ?',
            values: [actionId]
        });

        if (F.isNull(actionDesc)) F.throwErrCode(101011);
        actionDesc.actionTime = actionDesc.actionTime == null?"":new Date(actionDesc.actionTime).getTime();
        actionDesc.actionEndTime = actionDesc.actionEndTime == null?"":new Date(actionDesc.actionEndTime).getTime();

        let misstionList = [];
        let hasBao = 0;

        //查询接口
        let actionAndMissionList = yield modelMap.banzhu_renling.execute_raw(
            "select r.uid,r.missionId,r.actionId,r.`status`,u.nick,u.avatar,m.title,m.type,a.haibao_list,m.missionTime,m.num,m.unit,m.desc " +
            "from banzhu_renling r LEFT JOIN banzhu_mission m on r.missionId = m.id left join banzhu_user u on r.uid = u.uid left join " +
            "banzhu_action a on r.actionId = a.id where r.actionId = ? order by r.uid", [actionId]);

        for(let i=0;i<actionAndMissionList.length;i++){
            misstionList.push({
                "uid":actionAndMissionList[i].uid,
                "nick":actionAndMissionList[i].nick,
                "type":actionAndMissionList[i].type,//0悬赏  1抽奖
                "title":actionAndMissionList[i].title,
                "missionId":actionAndMissionList[i].missionId,
                "haibao_list":  actionAndMissionList[i].haibao_list,
                "missionTime": actionAndMissionList[i].missionTime == null?"":new Date(actionAndMissionList[i].missionTime).getTime(),
                "num":actionAndMissionList[i].num,
                "unit":actionAndMissionList[i].unit,
                "desc":actionAndMissionList[i].desc==null?"":actionAndMissionList[i].desc.replace(new RegExp("[a-z|A-Z|0-9]{5}", 'g'), "*****"),
                "avatar":actionAndMissionList[i].avatar
            });
        }

        let hasbao = 0;
        if(!F.isNull(uid)){
            let baoList = yield modelMap.banzhu_action_bao.query(null, {
                fields:'hasPay',
                where:'uid = ? and actionId = ?',
                values:[uid,actionId]
            });

            if (F.isNull(baoList)) {
                hasBao = 0;//未报名
            }else if(baoList[0].hasPay == 0 && actionDesc.fee>0){
                hasBao = 1;//已报名未支付
            }else{ //if(hasPay == 1 || (hasPay == 0 && actionDesc.fee==0)){
                hasBao = 2;//已报名且成功
            }
        }

        if(!(hasBao == 2 || actionDesc.uid == uid)){
            actionDesc.desc = actionDesc.desc ==null?"":actionDesc.desc.replace(new RegExp("[a-z|A-Z|0-9]{5}", 'g'), "*****");
        }

        //actionDesc.hasBao = hasBao;

        let countRes = yield modelMap.banzhu_action_bao.execute_raw("select count(id) as num from banzhu_action_bao where actionId = ?", [actionId]);
        actionDesc.num = actionDesc.peopleNum - countRes[0].num;

        actionDesc.missionList = misstionList;

        actionDesc.hasCollect = 0;
        let dbRes = yield modelMap.banzhu_user_collect.query(null,{
            fields:"id",
            where:"uid = ? and actionId = ?",
            values:[uid,actionId]
        });
        if(!F.isNull(dbRes)) actionDesc.hasCollect = 1;

        F.setResJson(ctx, 0, actionDesc);
    });

    http_app.regPost('/banzhu/:apiVer/checkHasBao', function* (ctx) {
        yield F.checkParamsNull(ctx, "actionId,uid");
        let actionId = ctx.I.actionId;
        let uid = ctx.I.uid;

        let actionDesc = yield modelMap.banzhu_action.queryOne(null,{
            fields: actionFields,
            where: 'id = ?',
            values: [actionId]
        });

        if (F.isNull(actionDesc)) F.throwErrCode(101011);

        let baoList = yield modelMap.banzhu_action_bao.query(null, {
            fields:'id,hasPay',
            where:'uid = ? and actionId = ?',
            values:[uid,actionId]
        });
        let hasBao = 0;

        //if (!F.isNull(baoList)) hasBao = 1;

        if (F.isNull(baoList)) {
            hasBao = 0;//未报名
        }else if(baoList[0].hasPay == 0 && actionDesc.fee>0){
            hasBao = 1;//已报名未支付
        }else{ //if(hasPay == 1 || (hasPay == 0 && actionDesc.fee==0)){
            hasBao = 2;//已报名且成功
        }

        F.setResJson(ctx, 0, {"hasBao":hasBao});
    });


    //报名接口
    http_app.regPost('/banzhu/:apiVer/baoming', function* (ctx) {
        yield F.checkParamsNull(ctx, "actionId,uid,token");
        let actionId = ctx.I.actionId;
        let uid = ctx.I.uid;
        let token = ctx.I.token;

        let cacheUid = yield mgr_map.redis.getTokenUidAndExpire(token);
        if (F.isNull(cacheUid) || cacheUid != uid) F.throwErrCode(101003);

        let actionDesc = yield modelMap.banzhu_action.queryOne(null,{
            fields: actionFields,
            where: 'id = ?',
            values: [actionId]
        });

        if (F.isNull(actionDesc)) F.throwErrCode(101011);

        let countRes = yield modelMap.banzhu_action_bao.execute_raw(
            "select count(id) as num from banzhu_action_bao where actionId = ?", [actionId]);
        if (Number(countRes[0].num) >= Number(actionDesc.peopleNum)) F.throwErrCode(101013);


        let baoList = yield modelMap.banzhu_action_bao.query(null, {
            fields:'id',
            where:'uid = ? and actionId = ?',
            values:[uid,actionId]
        });

        let ret = {};
        if (F.isNull(baoList)) {
            let isertRes = yield modelMap.banzhu_action_bao.insert(null,{
                'uid': uid,
                'actionId': actionId
            });
            ret.baoId = isertRes.insertId;
        } else {
            ret.baoId = baoList[0].id;
        }

        countRes = yield modelMap.banzhu_action_bao.execute_raw("select count(id) as num from banzhu_action_bao where actionId = ?", [actionId]);
        ret.num = countRes[0].num;
        ret.needPay = actionDesc.fee > 0 ? 1 : 0;


        F.setResJson(ctx, 0, ret);
    });


    //获取报名成员
    http_app.regPost('/banzhu/:apiVer/getBaoList', function* (ctx) {
        yield F.checkParamsNull(ctx, "actionId");
        let domain = F.isNull(ctx.I.domain) ? "" : ctx.I.domain;
        let actionId = ctx.I.actionId;

        let actionDesc = yield modelMap.banzhu_action.queryOne(null,{
            fields: "fee",
            where: 'id = ?',
            values: [actionId]
        });

        if (F.isNull(actionDesc)) F.throwErrCode(101011);

        let intestList = yield modelMap.banzhu_action_bao.execute_raw(
            "select u.uid, b.createTime,b.hasPay,u.nick,u.avatar,u.xing from banzhu_action_bao b LEFT JOIN banzhu_user u on b.uid = u.uid where actionId = ?", [actionId]);

        let baoList = [];
        for (let j = 0; j < intestList.length; j++) {
            let item = intestList[j];
            if (item.hasPay == 1 || actionDesc.fee == 0) {
                baoList.push(item)
            }
        }

        F.setResJson(ctx, 0, {"baoList":baoList,"intestList":intestList});
    });

    //获取我的所有活动列表
    http_app.regPost('/banzhu/:apiVer/getMyActionList', function* (ctx) {
        yield F.checkParamsNull(ctx, "uid,missionId");
        //我创建的活动
        let uid = ctx.I.uid;
        let missionId = ctx.I.missionId;
        let domain = F.isNull(ctx.I.domain) ? "" : ctx.I.domain;
        let queryCond = {
            fields: actionFields
        }
        queryCond.where = "uid = ?";
        queryCond.values = [uid];
        if (!F.isNull(domain)) {
            queryCond.where += " and domain = ?";
            queryCond.values.push(domain);
        }
        let actionList = yield modelMap.banzhu_action.query(null, queryCond);

        //我报名的活动
        let myBaomingList = yield modelMap.banzhu_renling.query(null,{
            fields: "uid,missionId,actionId,`status`",
            where: 'uid = ? and missionId = ?',
            values: [uid,missionId]
        });
        let dictionary = {};
        for(let i=0;i<actionList.length;i++){
            actionList[i].status = 0;
            dictionary[actionList[i].actionId] = actionList[i];
        }

        for(let j=0;j<myBaomingList.length;j++){
            if(!F.isNull(dictionary[myBaomingList[j].actionId])){
                dictionary[myBaomingList[j].actionId].status = myBaomingList[j].status+1;
            }
        }
        F.setResJson(ctx, 0, {"actionList":actionList});
    });

    //收藏或者取消收藏活动
    http_app.regPost('/banzhu/:apiVer/collectActionById', function* (ctx) {
        yield F.checkParamsNull(ctx, "uid,actionId,type,token");
        //我创建的活动
        let uid = ctx.I.uid;
        let actionId = ctx.I.actionId;
        let type = ctx.I.type;
        let token = ctx.I.token;

        let cacheUid = yield mgr_map.redis.getTokenUidAndExpire(token);
        if (F.isNull(cacheUid) || cacheUid != uid) F.throwErrCode(101003);

        if(type == 1){//收藏
            let dbRes = yield modelMap.banzhu_user_collect.query(null,{
                fields:"id",
                where:"uid = ? and actionId = ?",
                values:[uid,actionId]
            });
            if(F.isNull(dbRes)){
                yield modelMap.banzhu_user_collect.insert(null,{
                    'uid':uid,
                    'actionId':actionId
                });
            }
        }else{//取消收藏
            yield modelMap.banzhu_user_collect.delete(null,{
                where:"uid = ? and actionId = ?",
                values:[uid,actionId]
            });
        }

        //计算人数并更新数据库
        let collectCount = yield modelMap.banzhu_user_collect.execute_raw("select count(id) as collectCount from banzhu_user_collect where actionId = ?",[actionId]);
        yield modelMap.banzhu_action.update(null,{
            where:"id = ?",
            values:[actionId],
            update_values:{
                collectCount:collectCount[0].collectCount
            }
        });

        F.setResJson(ctx, 0, {});
    });

    //获取我的收藏活动
    http_app.regPost('/banzhu/:apiVer/getMyCollectAction', function* (ctx) {
        yield F.checkParamsNull(ctx, "uid");

        let uid = ctx.I.uid;
        //let token = ctx.I.token;

        //let cacheUid = yield mgr_map.redis.getTokenUidAndExpire(token);
        //if (F.isNull(cacheUid) || cacheUid != uid) F.throwErrCode(101003);

        let actionList = yield modelMap.banzhu_user_collect.execute_raw("select a.id as actionId,a.uid,a.typeId,a.typeName,a.title,a.actionTime,a.actionEndTime,a.address," +
            "a.peopleNum,a.fee,a.isPublic,a.`desc`,a.haibao_list,a.pic_list,a.collectCount from banzhu_user_collect c left join " +
            "banzhu_action a on c.actionId = a.id where c.uid = ?",[uid]);

        F.setResJson(ctx, 0, {"actionList":actionList});
    });


    //获取我参加的，收藏的，创建的活动
    http_app.regPost('/banzhu/:apiVer/getMyAllActionList', function* (ctx) {
        yield F.checkParamsNull(ctx, "uid,token");

        let uid = ctx.I.uid;
        let token = ctx.I.token;
        //let domain = ctx.I.domain;

        let cacheUid = yield mgr_map.redis.getTokenUidAndExpire(token);
        if (F.isNull(cacheUid) || cacheUid != uid) F.throwErrCode(101003);

        let myCollectionActionList = yield modelMap.banzhu_user_collect.execute_raw("select a.id as actionId,a.uid,a.typeId,a.typeName,a.title,a.actionTime,a.actionEndTime,a.address," +
            "a.peopleNum,a.fee,a.femalefee,a.isPublic,a.`desc`,a.haibao_list,a.pic_list,a.collectCount from banzhu_user_collect c left join " +
            "banzhu_action a on c.actionId = a.id where c.uid = ? order by a.priority desc,a.id desc",[uid]);

        let myCreateActionList = yield modelMap.banzhu_action.query(null, {
            fields: "id as actionId,uid,typeId,typeName,title,actionTime,actionEndTime,address,peopleNum,fee,femalefee,isPublic,`desc`,haibao_list,pic_list,collectCount ",
            order: "priority desc,id desc",
            where: "uid = ?",
            values: [uid],
        });

        let myParticipateActionList = yield modelMap.banzhu_renling.execute_raw(
            "select a.id as actionId,a.uid,typeId,typeName,title,actionTime,actionEndTime,address,peopleNum,fee,femalefee,isPublic,`desc`,haibao_list,pic_list,collectCount "+
            "from banzhu_action a LEFT JOIN banzhu_action_bao b on a.id = b.actionId where b.uid = ? order by a.priority desc,a.id desc",[ctx.I.uid]);

        let result = [];
        result = result.concat(myCollectionActionList);
        result = result.concat(myCreateActionList);
        result = result.concat(myParticipateActionList);

        F.setResJson(ctx, 0, {"actionList":result});
    });

    //获取某人参加的、创建的活动列表
    http_app.regPost('/banzhu/:apiVer/getSomebodyAllActionListByUid', function* (ctx) {
        yield F.checkParamsNull(ctx, "uid");

        let uid = ctx.I.uid;

        let createActionList = yield modelMap.banzhu_action.query(null, {
            fields: "id as actionId,uid,typeId,typeName,title,actionTime,actionEndTime,address,peopleNum,fee,femalefee,isPublic,`desc`,haibao_list,pic_list,collectCount ",
            order: "priority desc,id desc",
            where: "uid = ?",
            values: [uid],
        });

        let participateActionList = yield modelMap.banzhu_renling.execute_raw(
            "select a.id as actionId,a.uid,typeId,typeName,title,actionTime,actionEndTime,address,peopleNum,fee,femalefee,isPublic,`desc`,haibao_list,pic_list,collectCount "+
            "from banzhu_action a LEFT JOIN banzhu_action_bao b on a.id = b.actionId where b.uid = ? order by a.priority desc,a.id desc",[ctx.I.uid]);

        let result = [];
        result = result.concat(createActionList);
        result = result.concat(participateActionList);

        result = F.uniqueObjectArrary(result,"actionId");//去重

        F.setResJson(ctx, 0, {"actionList":result});
    });

    //获取附近的活动
    http_app.regPost('/banzhu/:apiVer/getNearActionList', function* (ctx) {
        yield F.checkParamsNull(ctx, "lat,lng");//lat  纬度 lng 经度
        let lat = ctx.I.lat;
        let lng = ctx.I.lng;

        let actionList = [];
        for(let i=1;i<=7;i+=2){
            let MaxMinLongitudeLatitude = F.getMaxMinLongitudeLatitude(lng,lat,i);
            actionList = yield modelMap.banzhu_action.query(null,{
                where:"lng between ? and ? and lat between ? and ?",
                fields: actionFields,
                values:[MaxMinLongitudeLatitude.minlng,MaxMinLongitudeLatitude.maxlng,MaxMinLongitudeLatitude.minlat,MaxMinLongitudeLatitude.maxlat]
            })
            if(actionList.length>=10){
                break;
            }
        }
        for(let i=0;i<actionList.length;i++){
            actionList[i].distance = F.caculateLL(actionList[i].lat,actionList[i].lng,lat,lng);
        }
        
        F.setResJson(ctx, 0, {"actionList":actionList});
    });

};

