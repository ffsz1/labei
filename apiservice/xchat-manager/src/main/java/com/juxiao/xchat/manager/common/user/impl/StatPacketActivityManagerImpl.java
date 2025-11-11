package com.juxiao.xchat.manager.common.user.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.utils.DateUtils;
import com.juxiao.xchat.base.utils.UUIDUtils;
import com.juxiao.xchat.dao.user.StatPacketActivityDao;
import com.juxiao.xchat.dao.user.StatPacketRegisterDao;
import com.juxiao.xchat.dao.user.domain.StatPacketActivityDO;
import com.juxiao.xchat.dao.user.domain.StatPacketRegisterDO;
import com.juxiao.xchat.dao.user.dto.StatPacketActivityDTO;
import com.juxiao.xchat.manager.common.user.StatPacketActivityManager;
import com.juxiao.xchat.manager.mq.ActiveMqManager;
import com.juxiao.xchat.manager.mq.bo.ShareRegisterMessageBO;
import com.juxiao.xchat.manager.mq.constant.MqDestinationKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @class: StatPacketActivityManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/14
 */
@Service
public class StatPacketActivityManagerImpl implements StatPacketActivityManager {
    @Autowired
    private StatPacketActivityDao packetActivityDao;
    @Autowired
    private StatPacketRegisterDao packetRegisterDao;

    @Autowired
    private ActiveMqManager activeMqManager;

    @Autowired
    private Gson gson;

    @Override
    public void saveFirstStatPacketActivity(Long uid) {
        StatPacketActivityDO activityDo = new StatPacketActivityDO();
        activityDo.setUid(uid);
        activityDo.setShareCount(0);
        activityDo.setSharePacketCount(0);
        activityDo.setLatestShareDate(null);
        activityDo.setPacketCount(0);
        activityDo.setRegisterCout(0);
        activityDo.setTodayRegisterCount(0);
        activityDo.setLatestRegisterDate(null);
        activityDo.setChargeBonus(0.00);
        activityDo.setTodayRegisterCount(0);
        activityDo.setLatestChargeBonusDate(null);
        activityDo.setCreateTime(new Date());
        packetActivityDao.save(activityDo);
    }

    @Override
    public void updateSharePacketAcitivty(Long uid) {
        StatPacketActivityDTO activityDto = this.getUserPacketActivity(uid);
        StatPacketActivityDO activityDo = new StatPacketActivityDO();
        activityDo.setUid(uid);
        activityDo.setShareCount(activityDto.getShareCount() + 1);
        activityDo.setPacketCount(activityDto.getPacketCount() + 1);
        activityDo.setSharePacketCount(activityDto.getSharePacketCount());
        activityDo.setLatestShareDate(new Date());
        packetActivityDao.update(activityDo);
    }

    @Override
    public void updateRegisterPacketActivity(Long uid, Long srcUid) {
        int count = packetRegisterDao.countUserRegisterUid(uid, srcUid);
        if (count == 0) {
            StatPacketRegisterDO registerDo = new StatPacketRegisterDO();
            registerDo.setRegisterId(UUIDUtils.get());
            registerDo.setUid(uid);
            registerDo.setRegisterUid(srcUid);
            registerDo.setCreateTime(new Date());
            packetRegisterDao.save(registerDo);
        }

        StatPacketActivityDTO activityDto = this.getUserPacketActivity(uid);
        StatPacketActivityDO activityDo = new StatPacketActivityDO();
        activityDo.setUid(uid);
        activityDo.setPacketCount(activityDto.getPacketCount() + 1);
        activityDo.setRegisterCout(activityDto.getRegisterCout() + 1);
        activityDo.setLatestRegisterDate(new Date());
        if (DateUtils.isToday(activityDto.getLatestRegisterDate())) {//今日新增注册，如果最近的一次注册日期是今天，则今日注册数量+1，如果不是，则今天注册数量为1
            activityDo.setTodayRegisterCount(activityDto.getTodayRegisterCount() + 1);
        } else {
            activityDo.setTodayRegisterCount(1);
        }

        if (DateUtils.isToday(activityDto.getLatestChargeBonusDate())) {
            activityDo.setTodayChargeBonus(0.00);
        }

        packetActivityDao.update(activityDo);
        //发送邀请注册礼物消息
        sendShareRegisterMessage(uid, activityDo.getRegisterCout());
    }

    private void sendShareRegisterMessage(Long uid, Integer shareRegisterCount) {
        //邀请不同人数注册会有不同奖励
        Integer missionId = 0;
        switch (shareRegisterCount){
            case 2:
                missionId = 19;
                break;
            case 5:
                missionId = 20;
                break;
            case 9:
                missionId = 21;
                break;
            case 14:
                missionId = 22;
                break;
            case 20:
                missionId = 23;
                break;
            default:
                break;
        }
        if(missionId == 0){
            return;
        }
        ShareRegisterMessageBO messageBO = new ShareRegisterMessageBO();
        messageBO.setUid(uid);
        messageBO.setMissionId(missionId);
        activeMqManager.sendQueueMessage(MqDestinationKey.SHARE_REGISTER_QUEUE, gson.toJson(messageBO));
    }

    @Override
    public void updateBounsPacketActivity(Long uid, double chargeBouns) {
        StatPacketActivityDTO activityDto = this.getUserPacketActivity(uid);
        StatPacketActivityDO activityDo = new StatPacketActivityDO();
        Date date = new Date();
        activityDo.setUid(uid);
        activityDo.setChargeBonus((activityDto.getChargeBonus() == null ? 0 : activityDto.getChargeBonus()) + chargeBouns);
        activityDo.setUpdateTime(date);
        activityDo.setLatestChargeBonusDate(date);
        Double todayChargeBonus = activityDto.getTodayChargeBonus();
        if (todayChargeBonus == null) {
            todayChargeBonus = 0.0;
        }
        if (DateUtils.isToday(activityDto.getLatestChargeBonusDate())) {//今日新增分成
            todayChargeBonus = todayChargeBonus + chargeBouns;
        } else {
            todayChargeBonus = chargeBouns;//今日首笔分成
        }
        activityDo.setTodayChargeBonus(todayChargeBonus);
        packetActivityDao.update(activityDo);
    }

    @Override
    public StatPacketActivityDTO getUserPacketActivity(Long uid) {
        StatPacketActivityDTO activityDto = packetActivityDao.getUserPacketActivity(uid);
        if (activityDto == null) {
            this.saveFirstStatPacketActivity(uid);
            activityDto = packetActivityDao.getUserPacketActivity(uid);
        }
        return activityDto;
    }
}
