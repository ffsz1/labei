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

    let selectField = 'id as missionId,uid,haibao_list,title,missionTime,num,unit,`desc`,domain,type';
    let awardSelField = 'id as missionId,haibao_list,title,missionTime,`desc`,contact,perDrawNum,type';

    http_app.regPost('/banzhu/:apiVer/testRedis', function* (ctx) {
        let ret = {};
        yield mgr_map.redis.outRedisco.LPUSH(["abc",JSON.stringify({"a":1})]);
        yield mgr_map.redis.outRedisco.LPUSH(["abc",JSON.stringify({"a":2})]);
        ret.len = yield mgr_map.redis.outRedisco.LLEN(["abc"]);
        ret.all = yield mgr_map.redis.outRedisco.LRANGE(["abc",0,-1]);
        for (let i = 0; i < ret.all.length; i++) {
            ret.all[i] = JSON.parse(ret.all[i]);
        }
        let list = ctx.I.awardList;
        ret.awardList = list;
        F.addLogs("ret.awardList");
        F.addLogs(ret.awardList);
        try{
            ret.decodeURIComponentList = JSON.parse(decodeURIComponent(list));
            F.addLogs("ret.decodeURIComponentList");
            F.addLogs(ret.decodeURIComponentList);
        }catch (e) {
            ret.decodeURIComponentList = null;
        }
        F.setResJson(ctx, 0, ret);
    });

    //发布任务
    http_app.regPost('/banzhu/:apiVer/addDraw', function* (ctx) {
        yield F.checkParamsNull(ctx, "token,uid,haibao_list,title,missionTime,desc,perDrawNum");
        let domain = F.isNull(ctx.I.domain)?"":ctx.I.domain;
        let token = ctx.I.token;
        let uid = ctx.I.uid;
        let haibao_list = ctx.I.haibao_list;
        let title = ctx.I.title;
        let missionTime = ctx.I.missionTime;
        let desc = ctx.I.desc;
        let contact = ctx.I.contact;
        let perDrawNum = ctx.I.perDrawNum;
        let encodeAwardList = ctx.I.awardList;
        let unit = ctx.I.unit;
        
        if(F.isNull(encodeAwardList)) F.throwErr("奖品不能为null");
        let awardList = [];
        try{
            awardList = JSON.parse(decodeURIComponent(encodeAwardList));
        }catch (e) {
            F.throwErr("awardList decode error:" +  e.stack);
            throw e;
        }

        if (F.isNull(awardList)) F.throwErr("奖品不能为null");
        if (awardList.length > 10)F.throwErr("最多只能设置10个奖品");

        for(let i=0;i<awardList.length;i++){
            if(!F.isNull(awardList[i].drawName) && !F.isNull(awardList[i].drawNum)){
                awardList[i].hasDrawNum = 0;
            }
        }

        let cacheUid = yield mgr_map.redis.getTokenUidAndExpire(token);
        if (F.isNull(cacheUid) || cacheUid != uid) F.throwErrCode(101003);

        let awardBaseInfo = {
            'uid' : uid,
            'domain': domain,
            'haibao_list': haibao_list,
            'title': title,
            'missionTime': missionTime,
            'unit': F.isNull(unit)?'元':unit,
            'num': 0,
            'desc': desc,
            'contact': contact,
            'type':1,//抽奖
            'perDrawNum': perDrawNum
        }

        let insertRes = yield modelMap.banzhu_mission.insert(null, awardBaseInfo);
        let ret = {};
        ret.missionId = insertRes.insertId;
        /*let awardList = [];
        if (F.isNull(ctx.I.drawName1) && F.isNull(ctx.I.drawNum1)) {
            awardList.push({"drawName":ctx.I.drawName1,"drawNum":ctx.I.drawNum1,"hasDrawNum":0});
        }
        if (F.isNull(ctx.I.drawName2) && F.isNull(ctx.I.drawNum2)) {
            awardList.push({"drawName":ctx.I.drawName2,"drawNum":ctx.I.drawNum2,"hasDrawNum":0});
        }
        if (F.isNull(ctx.I.drawName3) && F.isNull(ctx.I.drawNum3)) {
            awardList.push({"drawName":ctx.I.drawName3,"drawNum":ctx.I.drawNum3,"hasDrawNum":0});
        }
        if (F.isNull(ctx.I.drawName4) && F.isNull(ctx.I.drawNum4)) {
            awardList.push({"drawName":ctx.I.drawName4,"drawNum":ctx.I.drawNum4,"hasDrawNum":0});
        }
        if (F.isNull(ctx.I.drawName5) && F.isNull(ctx.I.drawNum5)) {
            awardList.push({"drawName":ctx.I.drawName5,"drawNum":ctx.I.drawNum5,"hasDrawNum":0});
        }
        if (F.isNull(ctx.I.drawName6) && F.isNull(ctx.I.drawNum6)) {
            awardList.push({"drawName":ctx.I.drawName6,"drawNum":ctx.I.drawNum6,"hasDrawNum":0});
        }
        if (F.isNull(ctx.I.drawName7) && F.isNull(ctx.I.drawNum7)) {
            awardList.push({"drawName":ctx.I.drawName7,"drawNum":ctx.I.drawNum7,"hasDrawNum":0});
        }
        if (F.isNull(ctx.I.drawName8) && F.isNull(ctx.I.drawNum8)) {
            awardList.push({"drawName":ctx.I.drawName8,"drawNum":ctx.I.drawNum8,"hasDrawNum":0});
        }
        if (F.isNull(ctx.I.drawName9) && F.isNull(ctx.I.drawNum9)) {
            awardList.push({"drawName":ctx.I.drawName9,"drawNum":ctx.I.drawNum9,"hasDrawNum":0});
        }
        if (F.isNull(ctx.I.drawName10) && F.isNull(ctx.I.drawNum10)) {
            awardList.push({"drawName":ctx.I.drawName10,"drawNum":ctx.I.drawNum10,"hasDrawNum":0});
        }*/
        awardBaseInfo.awardList = awardList;
        let lock = {};
        try {
            lock = yield mgr_map.redis.getLock(C.lock_key.banzhu_award + ret.missionId, 20);
            yield mgr_map.redis.setAward(ret.missionId, awardBaseInfo);
        }
        catch (e) {
            F.addErrLogs(['add award err:', e.stack]);
            throw  e;
        }
        finally {
            yield mgr_map.redis.releaseLock(lock);
        }
        F.setResJson(ctx, 0, ret);
    });

    // 查看抽奖信息
    http_app.regPost('/banzhu/:apiVer/getDraw', function* (ctx) {
        yield F.checkParamsNull(ctx, "missionId,uid");
        let uid = ctx.I.uid;
        let missionId = ctx.I.missionId;

        let awardBaseInfo = {}

        let lock = {};
        let leftDrawNum = 0;
        try {
            lock = yield mgr_map.redis.getLock(C.lock_key.banzhu_award + missionId, 20);
            awardBaseInfo = yield mgr_map.redis.getAward(missionId);

            //if (perHasDrawNum >= awardBaseInfo.perDrawNum) F.throwErrCode(101015);
            let leftTotalNum = 0;
            for (let i = 0; i < awardBaseInfo.awardList.length; i++) {
                let item = awardBaseInfo.awardList[i];
                if (item.hasDrawNum >= item.drawNum) continue;
                leftTotalNum += item.drawNum - item.hasDrawNum;
            }
            //if (leftTotalNum == 0) F.throwErrCode(101014);
            let perHasDrawNum = yield mgr_map.redis.getPerHasDrawCount(missionId, uid);
            leftDrawNum = awardBaseInfo.perDrawNum - perHasDrawNum;
            if(leftDrawNum> leftTotalNum) leftDrawNum = leftTotalNum;
        }
        catch (e) {
            F.addErrLogs(['add award err:', e.stack]);
            throw  e;
        }
        finally {
            yield mgr_map.redis.releaseLock(lock);
        }
        if (F.isNull(awardBaseInfo)) F.throwErrCode(101012);
        let totalAwardNum = 0;
        let hasAwardNum = 0;
        for (let i = 0; i < awardBaseInfo.awardList.length; i++) {
            let item = awardBaseInfo.awardList[i];
            hasAwardNum += item.hasDrawNum;
            totalAwardNum += item.drawNum;
        }
        awardBaseInfo.totalAwardNum = totalAwardNum;
        awardBaseInfo.hasAwardNum = hasAwardNum;
        awardBaseInfo.leftDrawNum = leftDrawNum;
        F.setResJson(ctx, 0, awardBaseInfo);
    });

    // 查看抽奖信息
    http_app.regPost('/banzhu/:apiVer/doDraw', function* (ctx) {
        yield F.checkParamsNull(ctx, "missionId,uid");
        let uid = ctx.I.uid;
        let missionId = ctx.I.missionId;
        let awardBaseInfo = {};
        let lock = {};
        let matchDrawName = "";
        // let matchDrawLevel = 0;
        let matchCode = "";
        try {
            lock = yield mgr_map.redis.getLock(C.lock_key.banzhu_award + missionId, 20);
            awardBaseInfo = yield mgr_map.redis.getAward(missionId);
            if (F.isNull(awardBaseInfo)) F.throwErrCode(101012);
            let perHasDrawNum = yield mgr_map.redis.getPerHasDrawCount(missionId, uid);
            if (perHasDrawNum >= awardBaseInfo.perDrawNum) F.throwErrCode(101015);
            let leftTotalNum = 0;
            for (let i = 0; i < awardBaseInfo.awardList.length; i++) {
                let item = awardBaseInfo.awardList[i];
                if (item.hasDrawNum >= item.drawNum) continue;
                leftTotalNum += item.drawNum - item.hasDrawNum;
            }
            if (leftTotalNum == 0) F.throwErrCode(101014);
            let luckNum = F.RandomNumBoth(1,leftTotalNum);
            let getNum = 0;
            for (let i = 0; i < awardBaseInfo.awardList.length; i++) {
                let item = awardBaseInfo.awardList[i];
                if (item.hasDrawNum >= item.drawNum) continue;
                getNum += item.drawNum - item.hasDrawNum;
                if (luckNum <= getNum) {
                    matchDrawName = item.drawName;
                    // matchDrawLevel = i + 1;
                    item.hasDrawNum += 1;
                    let nextId = yield mgr_map.redis.getNextReqId("doDraw");
                    matchCode = F.getRandom(2).toString()+nextId.toString()+F.getRandom(1).toString();
                    break;
                }
            }
            yield mgr_map.redis.addPerHasAward(missionId,uid,{
                "drawName":matchDrawName,
                "code":matchCode,
                "hasGet":0
            });
            yield mgr_map.redis.addTotalHasAward(missionId,{
                "uid":uid,
                "drawName":matchDrawName,
                "code":matchCode,
                "hasGet":0
            });
            yield mgr_map.redis.setAward(missionId, awardBaseInfo);
        }
        catch (e) {
            F.addErrLogs(['add award err:', e.stack]);
            throw  e;
        }
        finally {
            yield mgr_map.redis.releaseLock(lock);
        }
        let ret = {};
        ret.drawName = matchDrawName;
        ret.code = matchCode;

        F.setResJson(ctx, 0, ret);

    });

    //发布任务
    http_app.regPost('/banzhu/:apiVer/getPerDrawHis', function* (ctx) {
        yield F.checkParamsNull(ctx, "missionId,uid");
        let uid = ctx.I.uid;
        let missionId = ctx.I.missionId;

        let usrDrawHis = yield mgr_map.redis.getPerHasDrawHis(missionId,uid);
        F.setResJson(ctx, 0, {"winHis":usrDrawHis});
    });

    //获取中奖列表
    http_app.regPost('/banzhu/:apiVer/getWinDrawListByMisionId', function* (ctx) {
        yield F.checkParamsNull(ctx, "missionId");
        let missionId = ctx.I.missionId;

        let winDrawList = yield mgr_map.redis.getTotalHasAward(missionId);


        if(!F.isNull(winDrawList)){
            let uids = [];
            for(let i=0;i<winDrawList.length;i++){
                uids.push(winDrawList[i].uid);
            }
            let userList = yield modelMap.banzhu_user.query(null,{
                where: 'uid in (?)',
                values: uids,
                selectField: 'uid,nick,phone'
            });

            let dictionary = {};
            for(let i = 0;i<userList.length;i++){
                dictionary[userList[i].uid] = userList[i];
            }
            for(let j = 0;j<winDrawList.length;j++){
                if(!F.isNull(dictionary[winDrawList[j].uid])){
                    winDrawList[j].nick = dictionary[winDrawList[j].uid].nick;
                    winDrawList[j].avatar = dictionary[winDrawList[j].uid].avatar;
                    winDrawList[j].phone = dictionary[winDrawList[j].uid].phone;
                }
            }
        }

        F.setResJson(ctx, 0, {"winDrawList":winDrawList});
    });

    //发布任务
    http_app.regPost('/banzhu/:apiVer/addMission', function* (ctx) {
        yield F.checkParamsNull(ctx, "token,uid,title,missionTime,num,unit");
        let domain = F.isNull(ctx.I.domain)?"":ctx.I.domain;
        let token = ctx.I.token;
        let uid = ctx.I.uid;
        if(uid==0){
            F.throwErr("uid can't not be null");
        }

        let haibao_list = ctx.I.haibao_list;
        let title = ctx.I.title;
        let missionTime = ctx.I.missionTime;
        let num = ctx.I.num;
        let unit = ctx.I.unit;
        let desc = ctx.I.desc;
        let lastMisTime = new Date();

        if(F.isNull(desc) && F.isNull(haibao_list)){
            F.throwErr("详情和详情图片不能同时为空!");
        }

        let cacheUid = yield mgr_map.redis.getTokenUidAndExpire(token);
        if (F.isNull(cacheUid) || cacheUid != uid) F.throwErrCode(101003);

        let user = yield modelMap.banzhu_user.queryOne(null,{
            fields:"uid,type",
            where: "uid = ? and type = ?",
            values: [uid,1]
        })
        if(F.isNull(user)) F.throwErr(101005);//您不是品牌商不能发布活动或者悬赏，请联系客服

        let insertRes = yield modelMap.banzhu_mission.insert(null, {
                'uid' : uid,
                'domain': domain,
                'haibao_list': haibao_list,
                'title': title,
                'missionTime': missionTime,
                'num': num,
                'unit': unit,
                '`desc`': desc
        });
        yield modelMap.banzhu_user.update(null,{
            where: 'uid = ?',
            values: [uid],
            update_values: {
                'lastMisTime': lastMisTime
            }
        });
        let ret = {};
        ret.missionId = insertRes.insertId;

        F.setResJson(ctx, 0, ret);
    });


    //获取所有人或者我的任务+抽奖列表
    http_app.regPost('/banzhu/:apiVer/getMissionList', function* (ctx) {
        let type = ctx.I.type;
        let uid = ctx.I.uid;
        let domain = F.isNull(ctx.I.domain)?"":ctx.I.domain;

        let queryCond = {
            fields: selectField
        }

        if (!F.isNull(domain)) {
            queryCond.where = "domain = ?";
            queryCond.values = [domain];
        } else {
            //queryCond.where = "type = ?";
            //queryCond.values = [0];
        }

        //let userList = yield modelMap.banzhu_mission.query(null,queryCond);

        //返回嵌套数组的形式并且按lastMisTime排序
        let condition = {
            fields: "uid,nick,avatar",
            order: " lastMisTime desc"
        }
        if(!F.isNull(type)&&type == 1){
            condition.where = "uid = ?";
            condition.values = [uid];
        }
        let userList = yield modelMap.banzhu_user.query(null,condition);

        if(!F.isNull(userList)){
            let userIds = [];
            for(let i=0;i<userList.length;i++){
                userIds.push(userList[i].uid);
            }
            if(F.isNull(queryCond.where)) {
                queryCond.where = "uid in (?)";
            }else{
                queryCond.where += " and uid in (?)";
            }
            queryCond.order = "id desc";
            
            if(F.isNull(queryCond.values)) queryCond.values =[];
            queryCond.values.push(userIds);

            let misstionList = yield modelMap.banzhu_mission.query(null,queryCond);

            let userIdIndex = {};
            for(let i=0;i<userList.length;i++){
                userList[i].missionList = [];//不管有没有都初始化一个空数组
                userIdIndex[userList[i].uid] = userList[i];
            }
            for(let j=0;j<misstionList.length;j++) {
                let user = userIdIndex[misstionList[j].uid];
                if (!F.isNull(user)){
                    if(F.isNull(user.missionList)){
                        user.missionList = [];
                    }
                    user.missionList.push({
                        "missionId": misstionList[j].missionId,
                        "haibao_list": misstionList[j].haibao_list,
                        "title": misstionList[j].title,
                        "missionTime": misstionList[j].missionTime,
                        "num": misstionList[j].num,
                        "unit": misstionList[j].unit,
                        "`desc`": misstionList[j].desc,
                        "type":misstionList[j].type
                    });
                }
            }
        }

        F.setResJson(ctx, 0, {"userList":userList});
    });

    //根据uid获取自己的发布或者参加过的悬赏/任务列表
    http_app.regPost('/banzhu/:apiVer/getMissionListByUid', function* (ctx) {
        yield F.checkParamsNull(ctx, "uid,type");
        let domain = F.isNull(ctx.I.domain)?"":ctx.I.domain;
        let uid = ctx.I.uid;
        let type = ctx.I.type;

        let queryCond = {};
        if(!F.isNull(domain)){
            queryCond.where = "and m.domain = ? ";
            queryCond.values = [uid,0,domain];
        }else {
            queryCond.where = "";
            queryCond.values = [uid,0];
        }

        let result = [];
        if(type == 0){//所有的悬赏
            let sql = "select r.uid,r.missionId,m.haibao_list,m.title,m.missionTime,m.num,m.unit,m.`desc`,m.type from banzhu_renling r left join banzhu_mission m "+
                "on r.missionId = m.id where r.uid = ? and m.type = ? order by m.id desc" + queryCond.where + " UNION " + "select m.uid,m.id as `missionId`,m.haibao_list,m.title,m.missionTime,m.num,m.unit,m.`desc`,m.type from banzhu_mission m "+
                "where m.uid = ? and m.type = ? order by m.id desc" + queryCond.where;
            let args = queryCond.values.concat(queryCond.values);
            result = yield modelMap.banzhu_mission.execute_raw(sql,args);
        }

        if(type == 1){//自己发布的悬赏
            let sql = "select m.uid,m.id as `missionId`,m.haibao_list,m.title,m.missionTime,m.num,m.unit,m.`desc`,m.type from banzhu_mission m "+
                "where m.uid = ? and m.type = ? order by m.id desc" + queryCond.where;
            result = yield modelMap.banzhu_mission.execute_raw(sql,queryCond.values);
        }

        if(type == 2){//参加过的悬赏
            let sql = "select r.uid,r.missionId,m.haibao_list,m.title,m.missionTime,m.num,m.unit,m.`desc`,m.type from banzhu_renling r left join banzhu_mission m "+
                "on r.missionId = m.id where r.uid = ? and m.type = ? order by m.id desc" + queryCond.where;
            result = yield modelMap.banzhu_mission.execute_raw(sql,queryCond.values);
        }

        F.setResJson(ctx, 0, {"missionList":result});
    });

    //根据uid获取自己的发布或者参加过的抽奖/抽奖列表
    http_app.regPost('/banzhu/:apiVer/getDrawListByUid', function* (ctx) {
        yield F.checkParamsNull(ctx, "uid,type");
        let domain = F.isNull(ctx.I.domain)?"":ctx.I.domain;
        let uid = ctx.I.uid;
        let type = ctx.I.type;

        let queryCond = {};
        if(!F.isNull(domain)){
            queryCond.where = "and m.domain = ? ";
            queryCond.values = [uid,1,domain];
        }else {
            queryCond.where = "";
            queryCond.values = [uid,1];
        }

        let result = [];
        if(type == 0){//所有的抽奖
            let sql = "select r.uid,r.missionId,m.haibao_list,m.title,m.missionTime,m.num,m.unit,m.`desc`,m.type,m.perDrawNum,m.contact from banzhu_renling r left join banzhu_mission m "+
                "on r.missionId = m.id where r.uid = ? and m.type = ? order by m.id desc" + queryCond.where + " UNION " + "select m.uid,m.id as `missionId`,m.haibao_list,m.title,m.missionTime,m.num,m.unit,m.`desc`,m.type,m.perDrawNum,m.contact from banzhu_mission m "+
                "where m.uid = ? and m.type = ? " + queryCond.where;
            let args = queryCond.values.concat(queryCond.values);
            result = yield modelMap.banzhu_renling.execute_raw(sql,args);
        }

        if(type == 1){//自己发布的抽奖
            let sql = "select m.uid,m.id as `missionId`,m.haibao_list,m.title,m.missionTime,m.num,m.unit,m.`desc`,m.type,m.perDrawNum,m.contact from banzhu_mission m "+
                "where m.uid = ? and m.type = ? order by m.id desc" + queryCond.where;
            result = yield modelMap.banzhu_renling.execute_raw(sql,queryCond.values);
        }

        if(type == 2){//参加过的抽奖
            let sql = "select r.uid,r.missionId,m.haibao_list,m.title,m.missionTime,m.num,m.unit,m.`desc`,m.type,m.perDrawNum,m.contact from banzhu_renling r left join banzhu_mission m "+
                "on r.missionId = m.id where r.uid = ? and m.type = ? order by m.id desc" + queryCond.where;
            result = yield modelMap.banzhu_renling.execute_raw(sql,queryCond.values);
        }


        F.setResJson(ctx, 0, {"drawList":result});
    });

    //获取任务详情
    http_app.regPost('/banzhu/:apiVer/getMissionDetail', function* (ctx) {
        yield F.checkParamsNull(ctx, "missionId");
        let missionId = ctx.I.missionId;
        let domain = F.isNull(ctx.I.domain)?"":ctx.I.domain;

        let queryCond = {
            fields: selectField,
            where: 'id = ?',
            values: [missionId]
        }

        let missionDesc = yield modelMap.banzhu_mission.queryOne(null,queryCond);
        F.setResJson(ctx, 0, missionDesc);
    });


    //报名接口
    http_app.regPost('/banzhu/:apiVer/missionBaoming', function* (ctx) {
        yield F.checkParamsNull(ctx, "missionId,uid,token,actionIdList,opttyp");
        let missionId = ctx.I.missionId;
        let uid = ctx.I.uid;
        let token = ctx.I.token;
        let actionIdList = ctx.I.actionIdList;
        let opttyp = ctx.I.opttyp;

        let cacheUid = yield mgr_map.redis.getTokenUidAndExpire(token);
        if (F.isNull(cacheUid) || cacheUid != uid) F.throwErrCode(101003);



        let missionDesc = yield modelMap.banzhu_mission.queryOne(null,{
            fields: selectField,
            where: 'id = ?',
            values: [missionId]
        });

        if (F.isNull(missionDesc)) F.throwErrCode(101012);

        let ret = {};
        //改成批量插入和全部删除
        if(opttyp == "insert"){
            if(F.isNull(actionIdList)) F.throwErr("actionList is null!");

            //1.查询同一个uid及其missionId对应的actionId列表
            let missstionAndActionList = yield modelMap.banzhu_renling.query(null,{
                fields: "uid,missionId,actionId,`status`",
                where: 'uid = ? and missionId = ?',
                values: [uid,missionId]
            });

            //2.生成任务字典
            let dictionary = [];
            for (let i=0;i<missstionAndActionList.length;i++){
                dictionary[missstionAndActionList[i].actionId] = 1;
            }

            //3.去掉重复的actionId
            actionIdList.toString();
            actionIdList = actionIdList.toString().split(",");
            for (let j = actionIdList.length-1;j >= 0 ;j--) {
                if (dictionary[actionIdList[j]] === 1) {
                    actionIdList.splice(j,1);
                }
            }

            //4.批量插入新增的actionId列表
            if(!F.isNull(actionIdList)){
                let insertValues = [];

                for (let i = 0; i < actionIdList.length; i++) {
                    insertValues.push({
                        "uid": uid,
                        "missionId": missionId,
                        "actionId": actionIdList[i],
                        "`status`": 0
                    });
                }
                yield modelMap.banzhu_renling.insertAll(null,insertValues);
            }
        }else if(opttyp == "delete"){
            let misstionList = yield modelMap.banzhu_renling.execute_raw("select id from banzhu_renling where status = 1 and mission = ?",[missionId]);
            if(!F.isNull(misstionList)) F.throwErr("任务已经被审核不能删除");

            yield modelMap.banzhu_renling.delete(null, {
                where: 'uid = ? and missionId = ?',
                values: [uid, missionId]
            });
        }else{
            F.throwErr("opttyp is null");
        }

        let countRes = yield modelMap.banzhu_renling.execute_raw("select count(distinct uid) as num from banzhu_renling where missionId = ?", [missionId]);
        ret.num = countRes[0].num;

        F.setResJson(ctx, 0, ret);
    });

    //认领接口
    http_app.regPost('/banzhu/:apiVer/renling', function* (ctx) {
        yield F.checkParamsNull(ctx, "missionId,uid,token,actionIdList");
        let missionId = ctx.I.missionId;
        let uid = ctx.I.uid;
        let token = ctx.I.token;
        let actionIdList = ctx.I.actionIdList;
        actionIdList = actionIdList.split(",");

        let cacheUid = yield mgr_map.redis.getTokenUidAndExpire(token);
        if (F.isNull(cacheUid) || cacheUid != uid) F.throwErrCode(101003);

        let missionDesc = yield modelMap.banzhu_mission.queryOne(null,{
            fields: selectField,
            where: 'id = ?',
            values: [missionId]
        });

        if (F.isNull(missionDesc)) F.throwErrCode(101012);
        if (missionDesc.uid != uid) F.throwErr(101016);

        yield modelMap.banzhu_renling.update(null, {
            where: 'missionId = ? and actionId in (?)',
            values: [missionId, actionIdList],
            update_values: {
                '`status`': 1
            }
        });

        F.setResJson(ctx, 0);
    });

    //获取某个任务或抽奖所有人报名活动列表
    http_app.regPost('/banzhu/:apiVer/getRenlingList', function* (ctx) {
        yield F.checkParamsNull(ctx, "missionId,uid");
        let missionId = ctx.I.missionId;
        let uid =  ctx.I.uid;

        let missionDesc = yield modelMap.banzhu_mission.queryOne(null,{
            fields: selectField,
            where: 'id = ?',
            values: [missionId]
        });

        if (F.isNull(missionDesc)) F.throwErrCode(101012);

        let dbRes = yield modelMap.banzhu_renling.execute_raw(
            "select r.uid,r.missionId,r.actionId,r.`status`,u.nick,u.avatar,a.title,a.haibao_list,a.pic_list,a.actionTime,a.actionEndTime,a.address,a.peopleNum,a.fee,a.isPublic,a.desc " +
            "from banzhu_renling r LEFT JOIN banzhu_mission m on r.missionId = m.id left join banzhu_user u on r.uid = u.uid left join " +
            "banzhu_action a on r.actionId = a.id where r.missionId = ? order by r.uid", [missionId]);

        /*let renlingList = [];
        let lastUid = 0;
        if (!F.isNull(dbRes)) {
            for (let i = 0; i < dbRes.length; i++) {
                let item = dbRes[i];
                if (item.uid != lastUid) {
                    let addItem = {};
                    addItem.uid = item.uid;
                    addItem.missionId = item.missionId;
                    addItem.nick = item.nick;
                    addItem.avatar = item.avatar;
                    addItem.actionList = [];
                    addItem.actionList.push({
                        "actionId": item.actionId,
                        "actionTitle": item.title,
                        "status": item.status
                    });
                    renlingList.push(addItem);
                    lastUid = item.uid;
                } else {
                    let addItem = renlingList[renlingList.length-1];
                    addItem.actionList.push({
                        "actionId": item.actionId,
                        "actionTitle": item.title,
                        "status": item.status
                    });
                }
            }
        }*/


        F.setResJson(ctx, 0, {"baomingList":dbRes,"type":missionDesc.type});
    });

    //获取某个人报名列表
    http_app.regPost('/banzhu/:apiVer/getSomebodyBaoList', function* (ctx) {
        yield F.checkParamsNull(ctx, "missionId,uid");
        let uid = ctx.I.uid;
        let missionId = ctx.I.missionId;

        let dbRes = yield modelMap.banzhu_renling.execute_raw(
            "select r.uid,r.missionId,r.actionId,r.`status`,u.nick,u.avatar,a.title,m.type " +
            "from banzhu_renling r LEFT JOIN banzhu_mission m on r.missionId = m.id left join banzhu_user u on r.uid = u.uid left join " +
            "banzhu_action a on r.actionId = a.id where r.missionId = ? and r.uid = ? order by r.uid", [missionId,uid]);

        let renlingList = [];
        let lastUid = 0;
        if (!F.isNull(dbRes)) {
            for (let i = 0; i < dbRes.length; i++) {
                let item = dbRes[i];
                if (item.uid != lastUid) {
                    let addItem = {};
                    addItem.uid = item.uid;
                    addItem.missionId = item.missionId;
                    addItem.nick = item.nick;
                    addItem.avatar = item.avatar;
                    addItem.type = item.type;
                    addItem.actionList = [];
                    addItem.actionList.push({
                        "actionId": item.actionId,
                        "actionTitle": item.title,
                        "status": item.status
                    });
                    renlingList.push(addItem);
                    lastUid = item.uid;
                } else {
                    let addItem = renlingList[renlingList.length-1];
                    addItem.actionList.push({
                        "actionId": item.actionId,
                        "actionTitle": item.title,
                        "status": item.status
                    });
                }
            }
        }


        F.setResJson(ctx, 0, {"baomingList":renlingList});
    });


    //获取认领成员列表
    http_app.regPost('/banzhu/:apiVer/getPaomaList', function* (ctx) {

        let dbRes = yield modelMap.banzhu_renling.execute_raw(
            "select r.uid,u.nick, r.missionId,m.title as missionTitle,m.num,m.unit, r.actionId,a.title as actionTitle " +
            "from banzhu_renling r LEFT JOIN banzhu_mission m on r.missionId = m.id left join banzhu_user u on r.uid = u.uid left join " +
            "banzhu_action a on r.actionId = a.id where m.type = 0 order by r.missionId, r.uid");

        F.setResJson(ctx, 0, {"paomaList":dbRes});
    });


    //领取奖品
    http_app.regPost('/banzhu/:apiVer/getDrawByCode', function* (ctx) {
        yield F.checkParamsNull(ctx, "missionId,code,uid");
        let missionId = ctx.I.missionId;
        let code = ctx.I.code;
        let uid = ctx.I.uid;

        let queryCond = {
            fields: selectField,
            where: 'id = ?',
            values: [missionId]
        }

        let missionDesc = yield modelMap.banzhu_mission.queryOne(null,queryCond);
        if (F.isNull(missionDesc)) F.throwErrCode(101012);
        if (missionDesc.uid!=uid) F.throwErrCode(101016);

        let totalHasAward = yield mgr_map.redis.getTotalHasAward(missionId);
        if (F.isNull(totalHasAward)) F.throwErrCode(101012);

        let listIndex = 0;
        let listItem = {};
        for(let i=0;i<totalHasAward.length;i++){
            if(totalHasAward[i].code == code){
                totalHasAward[i].hasGet = 1;
                listIndex = i;
                listItem = totalHasAward[i];
                break;
            }
        }
        if (F.isNull(listItem)) F.throwErr("中奖不存在");
        yield mgr_map.redis.setTotalHasAward(missionId,listIndex,listItem);
        F.setResJson(ctx, 0, {});
    });
};

