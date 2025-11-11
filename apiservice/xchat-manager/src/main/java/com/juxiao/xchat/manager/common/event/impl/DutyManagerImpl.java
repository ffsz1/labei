package com.juxiao.xchat.manager.common.event.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.event.DutyDailyRecordDao;
import com.juxiao.xchat.dao.event.domain.DutyDailyRecordDO;
import com.juxiao.xchat.dao.event.dto.DutyDailyRecordDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.event.DutyManager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DutyManagerImpl implements DutyManager {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private Gson gson;
    @Autowired
    private DutyDailyRecordDao dailyRecordDao;
    @Autowired
    private RedisManager redisManager;

//    @Override
//    public void updateFreshFinish(Long uid, int dutyId) {
//        String redisKey = RedisKey.duty_fresh_lock.getKey(uid + "_" + dutyId);
//        String lockVal = redisManager.lock(redisKey, 3000);
//        if (StringUtils.isEmpty(lockVal)) {
//            return;
//        }
//
//        try {
//            DutyDailyRecordDTO recordDto = dailyRecordDao.getFreshDuty(uid, dutyId);
//            if (recordDto == null) {
//                Date date = new Date();
//                DutyDailyRecordDO recordDo = new DutyDailyRecordDO();
//                recordDo.setUid(uid);
//                recordDo.setDutyId(dutyId);
//                recordDo.setUdStatus((byte) 2);
//                recordDo.setGoldAmount(null);
//                recordDo.setCreateTime(date);
//                recordDo.setUpdateTime(date);
//                dailyRecordDao.save(recordDo);
//                redisManager.hdel(RedisKey.duty_fresh_record.getKey(), uid.toString());
//                return;
//            }
//
//            if (recordDto.getUdStatus() != null && recordDto.getUdStatus() != 1) {
//                return;
//            }
//
//            DutyDailyRecordDO recordDo = new DutyDailyRecordDO();
//            recordDo.setDutyId(recordDto.getDutyId());
//            recordDo.setUdStatus((byte) 2);
//            recordDo.setUpdateTime(new Date());
//            dailyRecordDao.update(recordDo);
//            redisManager.hdel(RedisKey.duty_fresh_record.getKey(), uid.toString());
//        } catch (Exception e) {
//            logger.error("[ 处理新手任务 ]异常：", e);
//        } finally {
//            redisManager.unlock(redisKey, lockVal);
//        }
//
//    }

    @Override
    public void updateDailyFinish(Long uid, int dutyId) {
        String redisKey = RedisKey.duty_lock.getKey(uid + "_" + dutyId);
        String lockVal = redisManager.lock(redisKey, 10000);
        if (StringUtils.isEmpty(lockVal)) {
            return;
        }

        try {
            DutyDailyRecordDTO recordDto = this.getDailyRecord(uid, dutyId);
            if (recordDto == null) {
                Date date = new Date();
                DutyDailyRecordDO recordDo = new DutyDailyRecordDO();
                recordDo.setUid(uid);
                recordDo.setDutyId(dutyId);
                recordDo.setUdStatus((byte) 2);
                recordDo.setGoldAmount(null);
                recordDo.setCreateTime(date);
                recordDo.setUpdateTime(date);
                dailyRecordDao.save(recordDo);
                redisManager.hdel(RedisKey.duty_daily_record.getKey(), uid.toString());
                return;
            }

            if (recordDto.getUdStatus() != null && recordDto.getUdStatus() != 1) {
                return;
            }

            DutyDailyRecordDO recordDo = new DutyDailyRecordDO();
            recordDo.setDutyId(recordDto.getDutyId());
            recordDo.setUdStatus((byte) 2);
            recordDo.setUpdateTime(new Date());
            dailyRecordDao.update(recordDo);
            redisManager.hdel(RedisKey.duty_daily_record.getKey(), uid.toString());
        } catch (Exception e) {
            logger.error("[ 处理每日任务 ]异常：", e);
        } finally {
            redisManager.unlock(redisKey, lockVal);
        }
    }

    @Override
    public void updateDailytimeFinish(Long uid, int dutyId) {
        String redisKey = RedisKey.duty_lock.getKey(uid + "_" + dutyId);
        String lockVal = redisManager.lock(redisKey, 10000);
        if (StringUtils.isEmpty(lockVal)) {
            return;
        }

        try {
            DutyDailyRecordDTO recordDto = this.getDailyRecord(uid, dutyId);
            if (recordDto == null) {
                Date date = new Date();
                DutyDailyRecordDO recordDo = new DutyDailyRecordDO();
                recordDo.setUid(uid);
                recordDo.setDutyId(dutyId);
                recordDo.setUdStatus((byte) 2);
                recordDo.setGoldAmount(null);
                recordDo.setCreateTime(date);
                recordDo.setUpdateTime(date);
                dailyRecordDao.save(recordDo);
                redisManager.hdel(RedisKey.duty_dailytime_record.getKey(), uid.toString());
                return;
            }

            if (recordDto.getUdStatus() != null && recordDto.getUdStatus() != 1) {
                return;
            }

            DutyDailyRecordDO recordDo = new DutyDailyRecordDO();
            recordDo.setDutyId(recordDto.getDutyId());
            recordDo.setUdStatus((byte) 2);
            recordDo.setUpdateTime(new Date());
            dailyRecordDao.update(recordDo);
            redisManager.hdel(RedisKey.duty_dailytime_record.getKey(), uid.toString());
        } catch (Exception e) {
            logger.error("[ 处理每日在线任务 ]异常：", e);
        } finally {
            redisManager.unlock(redisKey, lockVal);
        }
    }

    public DutyDailyRecordDTO getDailyRecord(Long uid, int dutyId) {
        String json = redisManager.hget(RedisKey.duty_record.getKey(), uid + "_" + dutyId);
        if (StringUtils.isNotBlank(json)) {
            try {
                return gson.fromJson(json, DutyDailyRecordDTO.class);
            } catch (Exception e) {
                redisManager.hdel(RedisKey.duty_record.getKey(), uid + "_" + dutyId);
            }
        }

        DutyDailyRecordDTO recordDto = dailyRecordDao.getDailyDuty(uid, dutyId);
        if (recordDto != null) {
            redisManager.hset(RedisKey.duty_record.getKey(), uid + "_" + dutyId, gson.toJson(recordDto));
        }

        return recordDto;
    }
}
