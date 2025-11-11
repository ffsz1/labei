package com.juxiao.xchat.manager.common.user.impl;

import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateUtils;
import com.juxiao.xchat.base.utils.UUIDUtils;
import com.juxiao.xchat.dao.user.UserPacketDao;
import com.juxiao.xchat.dao.user.UserPacketRecordDao;
import com.juxiao.xchat.dao.user.domain.UserPacketDO;
import com.juxiao.xchat.dao.user.domain.UserPacketRecordDO;
import com.juxiao.xchat.dao.user.dto.UserPacketDTO;
import com.juxiao.xchat.dao.user.enumeration.UserPacketRecordTypeEnum;
import com.juxiao.xchat.dao.user.query.UserPacketSumQuery;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.user.StatPacketActivityManager;
import com.juxiao.xchat.manager.common.user.UserPacketManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.juxiao.xchat.base.constant.PacketConstant.fistrtPacketNum;

@Service
public class UserPacketManagerImpl implements UserPacketManager {
    @Autowired
    private UserPacketDao packetDao;
    @Autowired
    private UserPacketRecordDao packetRecordDao;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private StatPacketActivityManager activityManager;

    @Override
    public void addUserPacket(Long uid, double packetNum, UserPacketRecordTypeEnum type, String shareId, Long srcUid) {
        //1.将此次获得的红包发放给用户，累加到用户红包表（userpacket表为用户红包表）
        UserPacketDTO packetDto = this.getUserPacket(uid);
        UserPacketDO packetDo = new UserPacketDO();
        packetDo.setUid(uid);
        packetDo.setPacketNum(packetDto.getPacketNum() + packetNum);
        packetDo.setHistPacketNum(packetDto.getHistPacketNum() + packetNum);
        packetDo.setUpdateTime(new Date());
        packetDao.update(packetDo);

        //2.更新红包获得统计表
        if (UserPacketRecordTypeEnum.PACKET_FOR_SHARE == type) {
            activityManager.updateSharePacketAcitivty(uid);
        } else if (UserPacketRecordTypeEnum.PACKET_FOR_INVITE == type) {
            //邀请注册，如果statpacketregister表中不存在此条注册数据，则生成一条statpacketregister数据，并更新statpacketactivity表，否则不做任何操作
            activityManager.updateRegisterPacketActivity(uid, srcUid);
        } else if (UserPacketRecordTypeEnum.PACKET_FOR_PROFITS == type || type == UserPacketRecordTypeEnum.PACKET_FOR_SUPERIOR) {
            //充值分成奖励
            activityManager.updateBounsPacketActivity(uid, packetNum);
        } else {
            return;
        }

        //3.生成一条用户红包记录
        UserPacketRecordDO packetRecordDo = new UserPacketRecordDO();
        packetRecordDo.setPacketId(UUIDUtils.get());
        packetRecordDo.setPacketNum(packetNum);
        packetRecordDo.setUid(uid);
        packetRecordDo.setObjId(shareId);
        packetRecordDo.setType(type.getValue());
        packetRecordDo.setHasUnpack(false);
        packetRecordDo.setCreateTime(new Date());
        packetRecordDo.setSrcUid(srcUid);
        packetRecordDao.save(packetRecordDo);
    }

    @Override
    public UserPacketDTO getUserPacket(Long uid) {
        UserPacketDTO packetDto = packetDao.getUserPacket(uid);
        if (packetDto == null) {
            Date date = new Date();
            UserPacketDO packetDo = new UserPacketDO();
            packetDo.setUid(uid);
            packetDo.setPacketNum(fistrtPacketNum);
            packetDo.setHistPacketNum(fistrtPacketNum);
            packetDo.setFirstGetTime(date);
            packetDo.setCreateTime(date);
            packetDo.setUpdateTime(date);
            packetDao.save(packetDo);
            packetDto = new UserPacketDTO();
            BeanUtils.copyProperties(packetDo, packetDto);

            activityManager.saveFirstStatPacketActivity(uid);

            UserPacketRecordDO packetRecordDo = new UserPacketRecordDO();
            packetRecordDo.setPacketId(UUIDUtils.get());
            packetRecordDo.setPacketNum(fistrtPacketNum);
            packetRecordDo.setUid(uid);
            packetRecordDo.setType(UserPacketRecordTypeEnum.PACKET_FOR_NEW.getValue());
            packetRecordDo.setHasUnpack(false);
            packetRecordDo.setCreateTime(date);
            packetRecordDao.save(packetRecordDo);
        }
        return packetDto;
    }

    @Override
    public int getUserBonusLevel(Long uid) {
        String bonusLevel = redisManager.hget(RedisKey.bonus_level.getKey(), String.valueOf(uid));
        if (StringUtils.isNotBlank(bonusLevel)) {
            try {
                return Integer.valueOf(bonusLevel);
            } catch (Exception e) {
            }
        }

        Date startTime = DateUtils.getLastMonthStart();
        Date endTime = DateUtils.getLastMonthEnd();
        Double bonusSum = packetRecordDao.sumUserPacketNum(new UserPacketSumQuery(uid, startTime, endTime));
        int level;
        if (bonusSum == null || bonusSum.intValue() < 1000) {
            level = 1;
        } else if (bonusSum.intValue() < 2000) {
            level = 2;
        } else if (bonusSum.intValue() < 3000) {
            level = 3;
        } else {
            level = 4;
        }
        redisManager.hset(RedisKey.bonus_level.getKey(), String.valueOf(uid), String.valueOf(level));
        return level;
    }
}
