package com.juxiao.xchat.manager.common.mcoin.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.mcoin.McoinMissionDao;
import com.juxiao.xchat.dao.mcoin.dto.McoinMissionDTO;
import com.juxiao.xchat.dao.mcoin.dto.McoinMissionInfoDTO;
import com.juxiao.xchat.dao.sysconf.dto.SysConfDTO;
import com.juxiao.xchat.dao.sysconf.enumeration.SysConfigId;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.mcoin.McoinManager;
import com.juxiao.xchat.manager.common.mcoin.McoinMissionManager;
import com.juxiao.xchat.manager.common.sysconf.SysConfManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class McoinMissionManagerImpl implements McoinMissionManager {

    @Autowired
    private Gson gson;
    @Autowired
    private McoinMissionDao mcoinMissionDao;
    @Autowired
    private McoinManager mcoinManager;
    @Autowired
    private SysConfManager sysConfManager;
    @Autowired
    private RedisManager redisManager;

    @Override
    public void finish(Long uid, Integer missionId) {
        SysConfDTO sysConf = sysConfManager.getSysConf(SysConfigId.mcoin_switch);
        if (sysConf == null || StringUtils.isBlank(sysConf.getConfigValue()) || !Boolean.valueOf(sysConf.getConfigValue())) {
            return;
        }

        // 加锁缓存
        String lockVal = redisManager.lock(RedisKey.mcoin_mission_lock.getKey(String.valueOf(uid)), 10000);
        try {
            if (StringUtils.isBlank(lockVal)) {
                return;
            }

            McoinMissionDTO missionDto = this.getMcoinMission(missionId);
            if (missionDto == null || missionDto.getMissionType() == null) {
                return;
            }

            Byte userMissionStatus;
            if (missionDto.getMissionType() == 2 || missionDto.getMissionType() == 3) {
                userMissionStatus = mcoinMissionDao.getUserCurrentDailyMissionStatus(uid, missionId);
            } else if (missionDto.getMissionType() == 1 || missionDto.getMissionType() == 4) {
                userMissionStatus = mcoinMissionDao.getUserCurrentOnceMissionStatus(uid, missionId);
            } else {
                return;
            }

            if (userMissionStatus != null && userMissionStatus != MISSION_STATUS_UNFINISH) {
                return;
            }

            // 1，新手任务；2，每日任务；3，签到任务
            if (missionDto.getMissionType() == 1 ) {
                mcoinMissionDao.saveOrUpdateMcoinOnceMission(uid, missionId, (byte) MISSION_STATUS_FINISH);
            } else if (missionDto.getMissionType() == 2) {
                mcoinMissionDao.saveOrUpdateMcoinDailyMission(uid, missionId, (byte) MISSION_STATUS_FINISH);
            } else if(missionDto.getMissionType() == 4){
                //如果类型是4  邀请码注册的话  直接发送奖品给用户
                try {
                    mcoinMissionDao.saveOrUpdateMcoinOnceMission(uid, missionId, (byte) MISSION_STATUS_GAIN);
                    if (missionDto.getFreebiesType() != null && missionDto.getFreebiesId() != null) {
                        FreebiesTypeEnum freebiesType = FreebiesTypeEnum.itemTypeOf((byte)missionDto.getFreebiesType().intValue());
                        if (freebiesType != null) {
                            freebiesType.sendFreebiesId(uid, missionDto.getFreebiesId(), "邀请" + missionDto.getMissionName() + "任务");
                        }
                    }else{
                        mcoinManager.updateAddMcoin(uid, missionDto.getMcoinAmount());
                        mcoinManager.sendMessageToUser(uid, missionDto);
                    }
                } catch (WebServiceException e) {
                    log.error("[finish]更新每日任务失败,异常信息:{}",e);
                }
            }else {
                return;
            }
            if(!Objects.equals(missionDto.getMissionType(), (byte)4)){
                redisManager.hincrBy(RedisKey.mcoin_mission_finish_hash.getKey(), String.valueOf(uid), 1L);
            }
            redisManager.hdel(RedisKey.mcoin_mission_list.getKey(), uid + "_" + missionDto.getMissionType());
        } finally {
            redisManager.unlock(RedisKey.mcoin_mission_lock.getKey(String.valueOf(uid)), lockVal);
        }
    }

    @Async
    @Override
    public void achieveWeeklyMission(Long uid) {
        SysConfDTO sysConf = sysConfManager.getSysConf(SysConfigId.mcoin_switch);
        if (sysConf == null || StringUtils.isBlank(sysConf.getConfigValue()) || !Boolean.valueOf(sysConf.getConfigValue())) {
            return;
        }

        // 加锁缓存
        String lockVal = redisManager.lock(RedisKey.mcoin_mission_lock.getKey(String.valueOf(uid)), 10000);
        try {
            if (StringUtils.isBlank(lockVal)) {
                return;
            }

            McoinMissionInfoDTO missionDto = mcoinMissionDao.getUserCrntWeeklyMission(uid);
            int missionId;
            if (missionDto == null) {
                missionId = 12;
                McoinMissionDTO missionDTO = this.getMcoinMission(missionId);
                mcoinMissionDao.saveOrUpdateMcoinWeeklyMission(uid, missionId);
                redisManager.hdel(RedisKey.mcoin_mission_list.getKey(), uid + "_3");
                mcoinManager.updateAddMcoin(uid, missionDTO.getMcoinAmount());
                return;
            }

            if (DateUtils.isToday(missionDto.getCompleteDate())) {
                return;
            }

            mcoinMissionDao.saveOrUpdateMcoinWeeklyMission(uid, missionDto.getMissionId());
            if (missionDto.getFreebiesType() != null && missionDto.getFreebiesId() != null) {
                FreebiesTypeEnum freebiesType = FreebiesTypeEnum.itemTypeOf(missionDto.getFreebiesType());
                if (freebiesType != null) {
                    freebiesType.sendFreebiesId(uid, missionDto.getFreebiesId(), "7日签到");
                }
            }
            redisManager.hdel(RedisKey.mcoin_mission_list.getKey(), uid + "_3");
            mcoinManager.updateAddMcoin(uid, missionDto.getMcoinAmount());
        } catch (Exception e) {
            log.error("[ 萌币商城 ] 更新每日任务出错：", e);
        } finally {
            redisManager.unlock(RedisKey.mcoin_mission_lock.getKey(String.valueOf(uid)), lockVal);
        }
    }


    @Override
    public void achieve(Long uid, Integer missionId) throws WebServiceException {
        SysConfDTO sysConf = sysConfManager.getSysConf(SysConfigId.mcoin_switch);
        if (sysConf == null || StringUtils.isBlank(sysConf.getConfigValue()) || !Boolean.valueOf(sysConf.getConfigValue())) {
            return;
        }
        // 加锁缓存
        String lockVal = redisManager.lock(RedisKey.mcoin_mission_lock.getKey(String.valueOf(uid)), 10000);
        try {
            if (StringUtils.isBlank(lockVal)) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }

            McoinMissionDTO missionDto = this.getMcoinMission(missionId);
            if (missionDto == null || missionDto.getMissionType() == null) {
                throw new WebServiceException(WebServiceCode.MISSION_NOT_EXISTS);
            }

            Byte userMissionStatus;
            if (missionDto.getMissionType() == 2 || missionDto.getMissionType() == 3) {
                userMissionStatus = mcoinMissionDao.getUserCurrentDailyMissionStatus(uid, missionId);
            } else if (missionDto.getMissionType() == 1 || missionDto.getMissionType() == 4) {
                userMissionStatus = mcoinMissionDao.getUserCurrentOnceMissionStatus(uid, missionId);
            } else {
                throw new WebServiceException(WebServiceCode.MISSION_UNKONWN);
            }

            if (userMissionStatus == null || userMissionStatus != MISSION_STATUS_FINISH) {
                throw new WebServiceException(WebServiceCode.MISSION_GAIN_FAIL);
            }

            // 1，新手任务；2，每日任务；3，签到任务 4.邀请注册任务
            switch (missionDto.getMissionType()) {
                case 1:
                case 4:
                    mcoinMissionDao.saveOrUpdateMcoinOnceMission(uid, missionId, (byte) MISSION_STATUS_GAIN);
                    break;
                case 2:
                    mcoinMissionDao.saveOrUpdateMcoinDailyMission(uid, missionId, (byte) MISSION_STATUS_GAIN);
                    break;
                default:
                    return;
            }

            redisManager.hdel(RedisKey.mcoin_mission_list.getKey(), uid + "_" + missionDto.getMissionType());
            mcoinManager.updateAddMcoin(uid, missionDto.getMcoinAmount());
            redisManager.hincrBy(RedisKey.mcoin_mission_finish_hash.getKey(), String.valueOf(uid), -1L);
        } finally {
            redisManager.unlock(RedisKey.mcoin_mission_lock.getKey(String.valueOf(uid)), lockVal);
        }
    }

    @Override
    public McoinMissionDTO getMcoinMission(Integer missionId) {
        String missionJson = redisManager.hget(RedisKey.mcoin_mission.getKey(), String.valueOf(missionId));
        if (StringUtils.isNotBlank(missionJson)) {
            try {
                return gson.fromJson(missionJson, McoinMissionDTO.class);
            } catch (Exception e) {
                redisManager.hdel(RedisKey.mcoin_mission.getKey(), String.valueOf(missionId));
            }
        }

        McoinMissionDTO missionDto = mcoinMissionDao.getMcoinMission(missionId);
        if (missionDto != null) {
            redisManager.hset(RedisKey.mcoin_mission.getKey(), String.valueOf(missionId), gson.toJson(missionDto));
        }
        return missionDto;
    }

    @Override
    public List<McoinMissionInfoDTO> listMissions(Long uid, byte missionType) {
        String fieldKey = uid + "_" + missionType;
        String missionList = redisManager.hget(RedisKey.mcoin_mission_list.getKey(), fieldKey);
        if (StringUtils.isNotBlank(missionList)) {
            try {
                return gson.fromJson(missionList, new TypeToken<List<McoinMissionInfoDTO>>() {
                }.getType());
            } catch (Exception e) {
                redisManager.hdel(RedisKey.mcoin_mission_list.getKey(), fieldKey);
            }
        }

        List<McoinMissionInfoDTO> list;
        switch (missionType) {
            case 1:
                list = mcoinMissionDao.listUserMcoinOnceMissions(uid, false);
                break;
            case 2:
                list = mcoinMissionDao.listUserMcoinDailyMissions(uid, false);
                break;
            case 3:
                list = mcoinMissionDao.listUserMcoinWeeklyMissions(uid, false);
                break;
            case 4:
                list = mcoinMissionDao.listUserMcoinShareRegisterMissions(uid, false);
                break;
            default:
                return Lists.newArrayList();
        }

        if (list == null) {
            list = Lists.newArrayList();
        }
        redisManager.hset(RedisKey.mcoin_mission_list.getKey(), fieldKey, gson.toJson(list));
        return list;
    }

    @Override
    public List<McoinMissionInfoDTO> listAuditingMissions(Long uid, byte missionType) {
        List<McoinMissionInfoDTO> list;
        switch (missionType) {
            case 1:
                list = mcoinMissionDao.listUserMcoinOnceMissions(uid, true);
                break;
            case 2:
                list = mcoinMissionDao.listUserMcoinDailyMissions(uid, true);
                break;
            case 3:
                list = mcoinMissionDao.listUserMcoinWeeklyMissions(uid, true);
                break;
            case 4:
                list = mcoinMissionDao.listUserMcoinShareRegisterMissions(uid, true);
                break;
            default:
                list = Lists.newArrayList();
        }

        return list;
    }

    @Override
    public int countUserMission(Long uid) {
        String countStr = redisManager.hget(RedisKey.mcoin_mission_finish_hash.getKey(), String.valueOf(uid));
        if (StringUtils.isNotBlank(countStr) && StringUtils.isNumeric(countStr)) {
            int missionCount = Integer.parseInt(countStr);
            if (missionCount < 0) {
                redisManager.hdel(RedisKey.mcoin_mission_finish_hash.getKey(), String.valueOf(uid));
                missionCount = 0;
            }
            return missionCount;
        }

        int onceMissionCount = mcoinMissionDao.countUserMcoinOnceMissions(uid);
        int dailyMissionCount = mcoinMissionDao.countUserMcoinDailyMissionss(uid);
        int missionCount = onceMissionCount + dailyMissionCount;
        if (missionCount <= 0) {
            redisManager.hset(RedisKey.mcoin_mission_finish_hash.getKey(), String.valueOf(uid), "0");
        } else {
            redisManager.hset(RedisKey.mcoin_mission_finish_hash.getKey(), String.valueOf(uid), String.valueOf(missionCount));
        }

        return missionCount;
    }
}
