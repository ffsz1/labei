package com.erban.main.service.duty;

import com.erban.main.config.SystemConfig;
import com.erban.main.model.Users;
import com.erban.main.mybatismapper.duty.DutyDailyRecordMapper;
import com.erban.main.mybatismapper.duty.DutyMapper;
import com.erban.main.mybatismapper.duty.domain.DutyDailyRecord;
import com.erban.main.param.neteasepush.NeteaseSendMsgParam;
import com.erban.main.service.SendSysMsgService;
import com.erban.main.service.SpringAppContext;
import com.erban.main.service.common.JedisLockService;
import com.erban.main.service.duty.dto.DutyDTO;
import com.erban.main.service.duty.dto.DutyResultDTO;
import com.erban.main.service.user.UserPurseService;
import com.erban.main.service.user.UsersService;
import com.erban.main.util.StringUtils;
import com.erban.main.wechat.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.RandomUtils;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

/**
 * @class: DutyService.java
 * @author: chenjunsheng
 * @date 2018/8/8
 */
@Service
public class DutyService {
    private final Gson gson = new Gson();
    @Autowired
    private DutyMapper dutyMapper;
    @Autowired
    private DutyDailyRecordMapper dailyRecordMapper;
    @Autowired
    private JedisService jedisService;
    @Autowired
    private JedisLockService jedisLockService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private UserPurseService userPurseService;
    @Autowired
    private SendSysMsgService sendSysMsgService;
    @Autowired
    private SpringAppContext context;

    public List<DutyResultDTO> listFreshDuties(Long uid) {
        String redisKey = RedisKey.duty_fresh_record.getKey();
        String json = jedisService.hget(redisKey, uid.toString());
        if (StringUtils.isNoneBlank(json)) {
            return gson.fromJson(json, new TypeToken<List<DutyResultDTO>>() {
            }.getType());
        }

        List<DutyResultDTO> list = dailyRecordMapper.listFreshDuties(uid);
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

        jedisService.hset(redisKey, uid.toString(), gson.toJson(list));
        return list;
    }

    /**
     * 根据类型查询任务
     *
     * @param uid
     * @return
     */
    public List<DutyResultDTO> listDailyDuties(Long uid) {
        String redisKey = RedisKey.duty_daily_record.getKey();
        String json = jedisService.hget(redisKey, uid.toString());
        if (StringUtils.isNoneBlank(json)) {
            return gson.fromJson(json, new TypeToken<List<DutyResultDTO>>() {
            }.getType());
        }

        List<DutyResultDTO> list = dailyRecordMapper.listDailyDuties(uid);
//        DutyDailyRecord record;
        for (DutyResultDTO dutyDto : list) {
            if (dutyDto == null) {
                continue;
            }

            if (dutyDto.getUdStatus() != null) {
                continue;
            }

            dutyDto.setUdStatus((byte) 1);
        }

        jedisService.hset(redisKey, uid.toString(), gson.toJson(list));
        return list;
    }

    public List<DutyResultDTO> listDailyTime(Long uid) {
        String roomTime = jedisService.hget(RedisKey.daily_room_time.getKey(), uid.toString());
        int onlineTime;
        try {
            onlineTime = Integer.valueOf(roomTime);
        } catch (Exception e) {
            onlineTime = 0;
        }

        this.updateDailyDuty(uid, 11);
        if (onlineTime >= 10) {
            this.updateDailyDuty(uid, 12);
        }

        if (onlineTime >= 30) {
            this.updateDailyDuty(uid, 13);
        }

        if (onlineTime >= 60) {
            this.updateDailyDuty(uid, 14);
        }
        return dailyRecordMapper.listDailyTime(uid);
    }

    public BusiResult achieve(Integer dutyId, Long uid) {
        if (dutyId == null || uid == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        Users users = usersService.getUsersByUid(uid);
        if (users == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }

        DutyDTO dutyDto = dutyMapper.getDuty(dutyId);
        if (dutyDto == null) {
            return new BusiResult<>(BusiStatus.PARAMETERILLEGAL, "不存在的任务", null);
        }

        String redisKey = RedisKey.duty_lock.getKey(dutyId + "_" + uid);
        String lockVal = jedisLockService.lock(redisKey, 10000);
        if (StringUtils.isEmpty(lockVal)) {
            return new BusiResult<>(BusiStatus.SERVERBUSY);
        }

        Byte dutyStatus;
        DutyDailyRecord record;

        try {
            if (dutyDto.getDutyType() == 1) {
                DutyType dutyType = DutyType.dutyIdOf(dutyDto.getId());
                if (dutyType == null || dutyType.getDutyService() == null) {
                    return new BusiResult<>(BusiStatus.PARAMETERILLEGAL, "错误的任务", null);
                }

                DutyResultService dutyService = dutyType.getDutyService();
                dutyStatus = dutyService.checkUserDutyStatus(uid);
                if (dutyStatus == 1) {
                    return new BusiResult<>(BusiStatus.SERVEXCEPTION, "请先完成任务", null);
                }

                record = dailyRecordMapper.getFreshDuty(uid, dutyId);
            } else {
                record = dailyRecordMapper.getDailyDuty(uid, dutyId);
                if (record == null || record.getUdStatus() == 1) {
                    return new BusiResult<>(BusiStatus.SERVEXCEPTION, "请先完成任务", null);
                }
            }

            if (record != null && record.getUdStatus() != null && record.getUdStatus() == 3) {
                return new BusiResult<>(BusiStatus.SERVEXCEPTION, "您已经领取该奖励", null);
            }

            int goldAmount = dutyDto.getGoldAmount();
            if (record == null) {
                Date date = new Date();
                record = new DutyDailyRecord();
                record.setUid(uid);
                record.setDutyId(dutyId);
                record.setUdStatus((byte) 3);
                record.setGoldAmount(goldAmount);
                record.setCreateTime(date);
                record.setUpdateTime(date);
                dailyRecordMapper.save(record);
            } else {
                record.setUdStatus((byte) 3);
                record.setGoldAmount(goldAmount);
                record.setUpdateTime(new Date());
                dailyRecordMapper.update(record);
            }

            userPurseService.updateAddGold(uid, dutyDto.getGoldAmount().longValue());

            if (dutyDto.getDutyType() == 1) {
                jedisService.hdel(RedisKey.duty_fresh_record.getKey(), uid.toString());
            } else if (dutyDto.getDutyType() == 2) {
                jedisService.hdel(RedisKey.duty_daily_record.getKey(), uid.toString());
            }
        } catch (Exception e) {
            return new BusiResult<>(BusiStatus.SERVERBUSY);
        } finally {
            jedisLockService.unlock(redisKey, lockVal);
        }


        try {
            // 发送消息给用户
            NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
            neteaseSendMsgParam.setFrom(SystemConfig.secretaryUid);
            neteaseSendMsgParam.setOpe(0);
            neteaseSendMsgParam.setType(0);
            neteaseSendMsgParam.setTo(uid.toString());
            neteaseSendMsgParam.setBody("恭喜您完成【" + dutyDto.getDutyName() + "】任务，获得 +" + record.getGoldAmount() + "金币，请前往我的钱包查看！");
            sendSysMsgService.sendMsg(neteaseSendMsgParam);
        } catch (Exception e) {
        }

        return new BusiResult(BusiStatus.SUCCESS);
    }

    public BusiResult speakPublic(@RequestParam("uid") Long uid) {
//        if (uid == null || uid == 0) {
//            return new BusiResult(BusiStatus.PARAMERROR);
//        }
//
//        Users users = usersService.getUsersByUid(uid);
//        if (users == null) {
//            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
//        }

        // this.updateFreshDuty(uid, DutyType.speak_in_public.getDutyId());
//        jedisService.hdel(RedisKey.duty_fresh_record.getKey(), uid.toString());
        return new BusiResult(BusiStatus.SUCCESS);
    }

    /**
     * 把任务更新成完成，未领取任务的状态
     *
     * @param uid
     * @param dutyId
     */
//    public void updateFreshDuty(Long uid, Integer dutyId) {
//        if (uid == null || uid == 0 || dutyId == null) {
//            return;
//        }
//
//        DutyDailyRecord record = dailyRecordMapper.getFreshDuty(uid, dutyId);
//        if (record == null) {
//            Date date = new Date();
//            record = new DutyDailyRecord();
//            record.setUid(uid);
//            record.setDutyId(dutyId);
//            record.setUdStatus((byte) 2);
//            record.setGoldAmount(null);
//            record.setCreateTime(date);
//            record.setUpdateTime(date);
//            dailyRecordMapper.save(record);
//            jedisService.hdel(RedisKey.duty_fresh_record.getKey(), uid.toString());
//            return;
//        }
//
//        if (record.getUdStatus() != null && record.getUdStatus() != 1) {
//            return;
//        }
//
//        record.setUdStatus((byte) 2);
//        record.setUpdateTime(new Date());
//        dailyRecordMapper.update(record);
//        jedisService.hdel(RedisKey.duty_fresh_record.getKey(), uid.toString());
//    }
//

    /**
     * 把任务更新成完成，未领取任务的状态
     *
     * @param uid
     * @param dutyId
     */
    public void updateDailyDuty(Long uid, Integer dutyId) {
        if (uid == null || uid == 0 || dutyId == null) {
            return;
        }
        String redisKey = RedisKey.duty_lock.getKey(uid + "_" + dutyId);
        String lockVal = jedisLockService.lock(redisKey, 3000);
        if (StringUtils.isEmpty(lockVal)) {
            return;
        }
        try {
            DutyDailyRecord record = dailyRecordMapper.getDailyDuty(uid, dutyId);
            if (record == null) {
                Date date = new Date();
                record = new DutyDailyRecord();
                record.setUid(uid);
                record.setDutyId(dutyId);
                record.setUdStatus((byte) 2);
                record.setGoldAmount(null);
                record.setCreateTime(date);
                record.setUpdateTime(date);
                dailyRecordMapper.save(record);
                jedisService.hdel(RedisKey.duty_daily_record.getKey(), uid.toString());
                return;
            }

            if (record.getUdStatus() != null && record.getUdStatus() != 1) {
                return;
            }

            record.setUdStatus((byte) 2);
            record.setUpdateTime(new Date());
            dailyRecordMapper.update(record);
            jedisService.hdel(RedisKey.duty_daily_record.getKey(), uid.toString());
        } catch (Exception e) {
            return;
        } finally {
            jedisLockService.unlock(redisKey, lockVal);
        }
    }
}
