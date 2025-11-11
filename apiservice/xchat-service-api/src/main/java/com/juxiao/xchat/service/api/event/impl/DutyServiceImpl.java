package com.juxiao.xchat.service.api.event.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.RandomUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.event.DutyDailyRecordDao;
import com.juxiao.xchat.dao.event.DutyDao;
import com.juxiao.xchat.dao.event.domain.DutyDailyRecordDO;
import com.juxiao.xchat.dao.event.dto.DutyDTO;
import com.juxiao.xchat.dao.event.dto.DutyDailyRecordDTO;
import com.juxiao.xchat.dao.event.dto.DutyResultDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.common.event.DutyManager;
import com.juxiao.xchat.manager.common.user.UserPurseManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.external.async.AsyncNetEaseTrigger;
import com.juxiao.xchat.manager.external.netease.NetEaseMsgManager;
import com.juxiao.xchat.service.api.event.DutyResultService;
import com.juxiao.xchat.service.api.event.DutyService;
import com.juxiao.xchat.service.api.event.DutyType;
import com.juxiao.xchat.service.api.event.vo.DutiesVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @class: DutyService.java
 * @author: chenjunsheng
 * @date 2018/8/8
 */
@Service
public class DutyServiceImpl implements DutyService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private Gson gson;
    @Autowired
    private DutyDao dutyDao;
    @Autowired
    private DutyDailyRecordDao dailyRecordDao;
    @Autowired
    private DutyManager dutyManager;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private UsersManager usersManager;
    @Autowired
    private UserPurseManager userPurseManager;
    @Autowired
    private AsyncNetEaseTrigger asyncNetEaseTrigger;

    @Override
    public DutiesVO getUserDuties(Long uid) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        UsersDTO users = usersManager.getUser(uid);
        if (users == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

//        List<DutyResultDTO> freshs = this.listFreshDuties(uid);
        List<DutyResultDTO> dailies = this.listDailyDuties(uid);
        List<DutyResultDTO> dailyTime = this.listDailyTime(uid);
        DutiesVO dutiesVo = new DutiesVO();
        try {
            String roomTime = redisManager.hget(RedisKey.daily_room_time.getKey(), uid.toString());
            dutiesVo.setRoomTime(Integer.valueOf(roomTime));
        } catch (Exception e) {
            dutiesVo.setRoomTime(0);
        }

//        dutiesVo.setFresh(freshs);
        dutiesVo.setDaily(dailies);
        dutiesVo.setDailyTime(dailyTime);
        return dutiesVo;
    }

    @Override
    public void achieve(Integer dutyId, Long uid) throws WebServiceException {
        if (dutyId == null || uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        UsersDTO users = usersManager.getUser(uid);
        if (users == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        DutyDTO dutyDto = dutyDao.getDuty(dutyId);
        if (dutyDto == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        Byte dutyStatus;
        DutyDailyRecordDTO recordDto;
        String redisKey = RedisKey.duty_lock.getKey(uid + "_" + dutyId);
        String lockVal = redisManager.lock(redisKey, 10000);
        if (StringUtils.isEmpty(lockVal)) {
            throw new WebServiceException(WebServiceCode.SERVER_BUSY);
        }
        try {
            if (dutyDto.getDutyType() == 1) {
                DutyType dutyType = DutyType.dutyIdOf(dutyDto.getId());
                if (dutyType == null || dutyType.getDutyService() == null) {
                    throw new WebServiceException(WebServiceCode.DUTY_SERVICE_NULL);
                }

                DutyResultService dutyService = dutyType.getDutyService();
                dutyStatus = dutyService.checkUserDutyStatus(uid);
                if (dutyStatus == 1) {
                    throw new WebServiceException(WebServiceCode.DUTY_NOT_FINISH);
                }

                recordDto = dailyRecordDao.getFreshDuty(uid, dutyId);
            } else {
                recordDto = dutyManager.getDailyRecord(uid, dutyId);
                if (recordDto == null || recordDto.getUdStatus() == 1) {
                    throw new WebServiceException(WebServiceCode.DUTY_NOT_FINISH);
                }
            }

            if (recordDto != null && recordDto.getUdStatus() != null && recordDto.getUdStatus() == 3) {
                throw new WebServiceException(WebServiceCode.DUTY_ACHIEVED);
            }

            int goldAmount;
            if (dutyDto.getDutyType() == 3) {
                goldAmount = RandomUtils.randomRegionInteger(1, dutyDto.getGoldAmount());
            } else {
                goldAmount = dutyDto.getGoldAmount();
            }

            DutyDailyRecordDO recordDo = new DutyDailyRecordDO();
            Date date = new Date();
            if (recordDto == null) {
                recordDo.setUid(uid);
                recordDo.setDutyId(dutyId);
                recordDo.setUdStatus((byte) 3);
                recordDo.setGoldAmount(goldAmount);
                recordDo.setCreateTime(date);
                recordDo.setUpdateTime(date);
                dailyRecordDao.save(recordDo);
            } else {
                recordDo.setId(recordDto.getId());
                recordDo.setDutyId(recordDto.getDutyId());
                recordDo.setUdStatus((byte) 3);
                recordDo.setGoldAmount(goldAmount);
                recordDo.setUpdateTime(date);
                dailyRecordDao.update(recordDo);
            }

            userPurseManager.updateAddGold(uid, dutyDto.getGoldAmount().longValue(), false, false,"奖励达成", null, null);

            redisManager.hdel(RedisKey.duty_record.getKey(), uid + "_" + dutyId);
            if (dutyDto.getDutyType() == 1) {
                redisManager.hdel(RedisKey.duty_fresh_record.getKey(), uid.toString());
            } else if (dutyDto.getDutyType() == 2) {
                redisManager.hdel(RedisKey.duty_daily_record.getKey(), uid.toString());
            } else if (dutyDto.getDutyType() == 3) {
                redisManager.hdel(RedisKey.duty_dailytime_record.getKey(), uid.toString());
            }
        } catch (Exception e) {
            throw e;
        } finally {
            redisManager.unlock(redisKey, lockVal);
        }

        this.send(uid.toString(), dutyDto.getDutyName(), dutyDto.getGoldAmount());
    }

    @Override
    public void speakPublic(Long uid) throws WebServiceException {
//        if (uid == null || uid == 0) {
//            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
//        }
//
//        UsersDTO users = usersManager.getUser(uid);
//        if (users == null) {
//            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
//        }
//
//        SpeakInPublicMessageBO messageBO = new SpeakInPublicMessageBO(uid);
//        activeMqManager.sendQueueMessage(MqDestinationKey.SPEAK_IN_PUBLIC_QUEUE, gson.toJson(messageBO));
//        redisManager.hdel(RedisKey.duty_fresh_record.getKey(), uid.toString());
    }

    private List<DutyResultDTO> listFreshDuties(Long uid) {
        String redisKey = RedisKey.duty_fresh_record.getKey();
        String json = redisManager.hget(redisKey, uid.toString());
        if (StringUtils.isNotBlank(json)) {
            return gson.fromJson(json, new TypeToken<List<DutyResultDTO>>() {
            }.getType());
        }

        List<DutyResultDTO> list = dailyRecordDao.listFreshDuties(uid);
        DutyType freshType;
        for (DutyResultDTO dutyDto : list) {
            if (dutyDto == null) {
                continue;
            }

            if (dutyDto.getUdStatus() != null) {
                continue;
            }

            freshType = DutyType.dutyIdOf(dutyDto.getDutyId());
            Byte udStatus;
            if (freshType == null || freshType.getDutyService() == null) {
                udStatus = 1;
            } else {
                udStatus = freshType.getDutyService().checkUserDutyStatus(uid);
            }

            dutyDto.setUdStatus(udStatus);
        }

        redisManager.hset(redisKey, uid.toString(), gson.toJson(list));
        return list;
    }

    private List<DutyResultDTO> listDailyDuties(Long uid) {
        String redisKey = RedisKey.duty_daily_record.getKey();
        String json = redisManager.hget(redisKey, uid.toString());
        if (StringUtils.isNotBlank(json)) {
            return gson.fromJson(json, new TypeToken<List<DutyResultDTO>>() {
            }.getType());
        }

        List<DutyResultDTO> list = dailyRecordDao.listDailyDuties(uid);
        redisManager.hset(redisKey, uid.toString(), gson.toJson(list));
        return list;
    }

    private List<DutyResultDTO> listDailyTime(Long uid) {
        String redisKey = RedisKey.duty_dailytime_record.getKey();
        String json = redisManager.hget(redisKey, uid.toString());
        if (StringUtils.isNotBlank(json)) {
            try {
                return gson.fromJson(json, new TypeToken<List<DutyResultDTO>>() {
                }.getType());
            } catch (Exception e) {
                redisManager.hdel(redisKey, uid.toString());
            }
        }

        dutyManager.updateDailytimeFinish(uid, 11);
        List<DutyResultDTO> list = dailyRecordDao.listDailyTime(uid);
        if (list != null) {
            redisManager.hset(redisKey, uid.toString(), gson.toJson(list));
        }
        return list;
    }

    @Async
    void send(String uid, String dutyName, int goldAmount) {
        try {
            String content = "恭喜您完成【" + dutyName + "】任务，获得 +" + goldAmount + "金币，请前往我的钱包查看！";
            asyncNetEaseTrigger.sendMsg(uid,content);
        } catch (Exception e) {
            logger.error("[ 每日任务 ]推送任务结果异常：", e);
        }
    }
}
