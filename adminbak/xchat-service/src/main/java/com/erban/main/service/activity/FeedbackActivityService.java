package com.erban.main.service.activity;

import com.erban.main.config.SystemConfig;
import com.erban.main.model.*;
import com.erban.main.mybatismapper.GiftSendRecordMapperExpand;
import com.erban.main.mybatismapper.UserGiftBonusPerDayMapper;
import com.erban.main.param.neteasepush.NeteaseSendMsgParam;
import com.erban.main.service.SendSysMsgService;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.record.BillRecordService;
import com.erban.main.service.user.UserPurseUpdateService;
import com.xchat.common.config.GlobalConfig;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.BlankUtil;
import com.xchat.common.utils.DateTimeUtil;
import com.xchat.common.utils.PropertyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by yuanyi on 2018/1/8.
 */


@Service
public class FeedbackActivityService extends BaseService{
    @Autowired
    GiftSendRecordMapperExpand giftSendRecordMapperExpand;
    @Autowired
    UserGiftBonusPerDayMapper userGiftBonusPerDayMapper;
    @Autowired
    UserPurseUpdateService userPurseUpdateService;
    @Autowired
    SendSysMsgService sendSysMsgService;
    @Autowired
    BillRecordService billRecordService;
    private static DecimalFormat doubleFormat = new DecimalFormat("0.00");

    public void updateUserGiftBonusPerPay(String time){

        String startTimeString = time + " 00:00:00";
        Date startTime = DateTimeUtil.convertStrToDate(startTimeString);
        String endTimeString = time + " 23:59:59";
        Date endTime = DateTimeUtil.convertStrToDate(endTimeString);

        List<GiftSendRecordVo2> giftSendRecordVo2List = giftSendRecordMapperExpand.getTotalDiamondListByDate(startTime,endTime);

        for(GiftSendRecordVo2 myGiftSendRecord:giftSendRecordVo2List){
            UserGiftBonusPerDayExample userGiftBonusPerDayExample = new UserGiftBonusPerDayExample();
            UserGiftBonusPerDayExample.Criteria criteria = userGiftBonusPerDayExample.createCriteria();
//            criteria.andUidEqualTo(myGiftSendRecord.getUid()).andStatDateEqualTo(DateTimeUtil.getDate(new Date()));
            criteria.andUidEqualTo(myGiftSendRecord.getUid()).andStatDateEqualTo(startTime);
            UserGiftBonusPerDay userGiftBonusPerDay = new UserGiftBonusPerDay();
//            userGiftBonusPerDay.setStatDate(DateTimeUtil.getDate(new Date()));
            userGiftBonusPerDay.setStatDate(startTime);
            userGiftBonusPerDay.setUid(myGiftSendRecord.getUid());
            userGiftBonusPerDay.setCurDiamondNum(myGiftSendRecord.getTotalDiamondNum());
            userGiftBonusPerDay.setForecastDiamondNum(curDiamondNumToForecast(myGiftSendRecord.getTotalDiamondNum()));
            int updateCount = userGiftBonusPerDayMapper.updateByExampleSelective(userGiftBonusPerDay,userGiftBonusPerDayExample);
            if(updateCount == 0){
                userGiftBonusPerDayMapper.insertSelective(userGiftBonusPerDay);
            }

        }
        saveCache(startTime,endTime);
    }

    public BusiResult getData(Long uid){
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        String startTimeString = DateTimeUtil.getTodayStr() + " 00:00:00";
        Date startTime = DateTimeUtil.convertStrToDate(startTimeString);
        String endTimeString = DateTimeUtil.getTodayStr() + " 23:59:59";
        Date endTime = DateTimeUtil.convertStrToDate(endTimeString);

        String userGiftBonusPerDayStr = jedisService.hget(RedisKey.feedback_activity.getKey(),uid.toString());
        UserGiftBonusPerDay userGiftBonusPerDay = null;
        if(BlankUtil.isBlank(userGiftBonusPerDayStr)){
            UserGiftBonusPerDayExample userGiftBonusPerDayExample = new UserGiftBonusPerDayExample();
            UserGiftBonusPerDayExample.Criteria criteria = userGiftBonusPerDayExample.createCriteria();
            criteria.andUidEqualTo(uid).andStatDateBetween(startTime,endTime);
            List<UserGiftBonusPerDay> userGiftBonusPerDayList = userGiftBonusPerDayMapper.selectByExample(userGiftBonusPerDayExample);
            if(userGiftBonusPerDayList == null || userGiftBonusPerDayList.size() ==0){
                return new BusiResult(BusiStatus.USERNOTEXISTS);
            }
            userGiftBonusPerDay = userGiftBonusPerDayList.get(0);
        }else{
            userGiftBonusPerDay = gson.fromJson(userGiftBonusPerDayStr,UserGiftBonusPerDay.class);
        }
        busiResult.setData(userGiftBonusPerDay);
        return busiResult;
    }

    public void computeDiamond() throws Exception{
        // 删缓存
        jedisService.hdeleteKey(RedisKey.feedback_activity.getKey());

        //调试用
        Date lastDate = DateTimeUtil.getDate(DateTimeUtil.getLastDay(new Date(),1));

        //防止漏掉12点前的结算
        String lastDay = getLastDateStr(lastDate);
        updateUserGiftBonusPerPay(lastDay);

        UserGiftBonusPerDayExample userGiftBonusPerDayExample = new UserGiftBonusPerDayExample();
        UserGiftBonusPerDayExample.Criteria criteria = userGiftBonusPerDayExample.createCriteria();
        criteria.andStatDateEqualTo(lastDate);
        List<UserGiftBonusPerDay> userGiftBonusPerDayList = userGiftBonusPerDayMapper.selectByExample(userGiftBonusPerDayExample);
        if(userGiftBonusPerDayList != null || userGiftBonusPerDayList.size() > 0){
            for(UserGiftBonusPerDay myUserGiftBonus:userGiftBonusPerDayList){
                // 计算今天最终得到的钻石分成
                myUserGiftBonus.setBonusDiamondNum(curDiamondNumToForecast(myUserGiftBonus.getCurDiamondNum()));
                if(myUserGiftBonus.getTodayHasFinishBonus() == null || !myUserGiftBonus.getTodayHasFinishBonus()){
                    // 还没充值则充值钻石
//                    int rechargeStatus = userPurseUpdateService.addDiamondNumFromDB(myUserGiftBonus.getUid(),myUserGiftBonus.getBonusDiamondNum());
                    int rechargeStatus = userPurseUpdateService.addDiamondDbAndCache(myUserGiftBonus.getUid(),myUserGiftBonus.getBonusDiamondNum());
                    NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
                    neteaseSendMsgParam.setFrom(SystemConfig.secretaryUid);
                    neteaseSendMsgParam.setOpe(0);
                    neteaseSendMsgParam.setType(0);
                    neteaseSendMsgParam.setTo(myUserGiftBonus.getUid().toString());
                    neteaseSendMsgParam.setBody("恭喜您，您昨日获得" + myUserGiftBonus.getCurDiamondNum() + "钻石，" + GlobalConfig.appName + "回馈您" + myUserGiftBonus.getBonusDiamondNum() + "钻石，请在钱包查看。");
                    if(rechargeStatus == 200){
                        myUserGiftBonus.setTodayHasFinishBonus(true);
                        myUserGiftBonus.setBonusFinishDate(new Date());

                        // 发信息
                        sendSysMsgService.sendMsg(neteaseSendMsgParam);
                        // 做账单
                        billRecordService.insertBillRecord( Long.valueOf(SystemConfig.secretaryUid),  myUserGiftBonus.getUid(),  myUserGiftBonus.getBonusId().toString(), Constant.BillType.bonusPerDaySend, myUserGiftBonus.getBonusDiamondNum(),  null,  null);
                        billRecordService.insertBillRecord(  myUserGiftBonus.getUid(), Long.valueOf(SystemConfig.secretaryUid),  myUserGiftBonus.getBonusId().toString(), Constant.BillType.bonusPerDayRecv, myUserGiftBonus.getBonusDiamondNum(),  null,  null);

                        userGiftBonusPerDayMapper.updateByPrimaryKeySelective(myUserGiftBonus);
                    }
                }

            }
        }
    }

    private void saveCache(Date startTime,Date endTime){
        UserGiftBonusPerDayExample userGiftBonusPerDayExample = new UserGiftBonusPerDayExample();
        UserGiftBonusPerDayExample.Criteria criteria = userGiftBonusPerDayExample.createCriteria();
        criteria.andStatDateBetween(startTime,endTime);
        List<UserGiftBonusPerDay> userGiftBonusPerDayList = userGiftBonusPerDayMapper.selectByExample(userGiftBonusPerDayExample);
        for(UserGiftBonusPerDay myUserGiftBonusPerDay:userGiftBonusPerDayList){
            String json = gson.toJson(myUserGiftBonusPerDay);
            jedisService.hset(RedisKey.feedback_activity.getKey(),myUserGiftBonusPerDay.getUid().toString(),json);
        }
    }

    private double curDiamondNumToForecast(Double diamondNum){

        if(diamondNum > 0 && diamondNum <= 999){
            return new Double(doubleFormat.format(diamondNum * 0.2));
        }else if(diamondNum > 999 && diamondNum <= 1499){
            return new Double(doubleFormat.format(diamondNum * 0.3));
        }else if(diamondNum > 1499 && diamondNum <= 1999){
            return new Double(doubleFormat.format(diamondNum * 0.35));
        }else{
            return new Double(doubleFormat.format(diamondNum * 0.4));
        }
    }

    public String getLastDateStr(Date lastDay){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(lastDay);
    }
}
