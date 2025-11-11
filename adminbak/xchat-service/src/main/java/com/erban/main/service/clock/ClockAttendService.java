package com.erban.main.service.clock;

import com.erban.main.model.BillRecord;
import com.erban.main.model.ClockAttendRecord;
import com.erban.main.model.UserPurse;
import com.erban.main.model.Users;
import com.erban.main.mybatismapper.ClockAttendRecordMapper;
import com.erban.main.service.base.CacheBaseService;
import com.erban.main.service.record.BillRecordService;
import com.erban.main.service.user.UserPurseService;
import com.erban.main.service.user.UserPurseUpdateService;
import com.erban.main.service.user.UsersService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.ClockAttendVo;
import com.erban.main.vo.ClockRecordVo;
import com.erban.main.vo.ClockResultVo;
import com.erban.main.vo.ClockUserVo;
import com.xchat.common.UUIDUitl;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ClockAttendService extends CacheBaseService<ClockAttendRecord, ClockAttendVo> {
    @Autowired
    private ClockAttendRecordMapper clockAttendRecordMapper;
    @Autowired
    private UsersService usersService;
    @Autowired
    private UserPurseService userPurseService;
    @Autowired
    private ClockResultService clockResultService;
    @Autowired
    private UserPurseUpdateService userPurseUpdateService;
    @Autowired
    private BillRecordService billRecordService;

    @Override
    public ClockAttendVo getOneByJedisId(String jedisId) {
        return getOne(RedisKey.clock_attend.getKey(), jedisId+DateTimeUtil.convertDate(new Date(), "yyyy-MM-dd"), "select * from clock_attend_record where uid = ? and DATE_FORMAT(clock_date,'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d') ", jedisId);
    }

    public ClockAttendVo getOneByJedisId(String jedisKey, Object... arg) {
        return getOne(RedisKey.clock_attend.getKey(), jedisKey, "select * from clock_attend_record where uid = ? and DATE_FORMAT(clock_date,'%Y-%m-%d') = DATE_FORMAT(?,'%Y-%m-%d') ", arg);
    }

    @Override
    public ClockAttendVo entityToCache(ClockAttendRecord entity) {
        ClockAttendVo clockAttendVo = new ClockAttendVo();
        clockAttendVo.setClockDate(DateTimeUtil.convertDate(entity.getClockDate(), "yyyy-MM-dd"));
        clockAttendVo.setUid(entity.getUid().longValue());
        Users users = usersService.getUsersByUid(entity.getUid().longValue());
        if(users!=null){
            clockAttendVo.setNick(users.getNick());
            clockAttendVo.setAvatar(users.getAvatar());
        }
        return clockAttendVo;
    }

    public String refreshUserAttendCache(String jedisCode, String uid) {
        List<ClockAttendRecord> tList = jdbcTemplate.query("select * from clock_attend_record where uid = ? ORDER BY clock_date desc ", new BeanPropertyRowMapper<>(ClockAttendRecord.class), uid);
        if (tList == null || tList.size() == 0) {
            jedisService.hwrite(jedisCode, uid, "");
            return null;
        }
        StringBuffer str = new StringBuffer();
        ClockAttendRecord clockAttendRecord;
        for (int i = 0; i < tList.size(); i++) {
            clockAttendRecord=tList.get(i);
            if (i == 0) {
                str.append(clockAttendRecord.getUid().toString()).append(DateTimeUtil.convertDate(clockAttendRecord.getClockDate(), "yyyy-MM-dd"));
            } else {
                str.append(",").append(clockAttendRecord.getUid().toString()).append(DateTimeUtil.convertDate(clockAttendRecord.getClockDate(), "yyyy-MM-dd"));
            }
        }
        jedisService.hwrite(jedisCode, uid, str.toString());
        return str.toString();
    }

    public String getUserAttend(Long uid){
        String str = jedisService.hget(RedisKey.clock_user_attend.getKey(), uid.toString());
        if (StringUtils.isEmpty(str)) {
            str = refreshUserAttendCache(RedisKey.clock_user_attend.getKey(), uid.toString());
            if (StringUtils.isEmpty(str)) {
                return null;
            }
        }
        return str;
    }

    public BusiResult getStatus(Long uid) {
        Date date = new Date();
        int hour = DateTimeUtil.getHour(date);
        Users users = usersService.getUsersByUid(uid);
        if(users==null){
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        Map<String, Object> result = new HashMap<>();
        String nextDate = DateTimeUtil.convertDate(DateTimeUtil.getNextDay(date, 1), "yyyy-MM-dd");
        ClockAttendVo clockAttendVo = getOneByJedisId(uid.toString()+nextDate, uid.toString(), nextDate);
        if(clockAttendVo==null) {
            clockAttendVo = getOneByJedisId(uid.toString());
            if(clockAttendVo==null){// 没有报名
                result.put("status", 0);
            }else {
                result.put("millis", DateTimeUtil.getDateMillis(DateTimeUtil.convertStrToDate(clockAttendVo.getClockDate(), "yyyy-MM-dd"), 18,0,0));
                ClockResultVo clockResultVo = clockResultService.getOneByJedisId(uid.toString());
                String jedisId = uid.toString()+clockAttendVo.getClockDate();
                if(clockResultVo==null){
                    if(hour>19){// 超时，打卡失败
                        if(StringUtils.isEmpty(jedisService.hget(RedisKey.clock_status.getKey(), jedisId))){
                            result.put("status", 3);
                        }else {
                            result.put("status", 0);
                        }
                    }else{// 倒计时，准备打卡
                        result.put("status", 1);
                    }
                }else {// 这次已经打卡成功
                    if(StringUtils.isEmpty(jedisService.hget(RedisKey.clock_status.getKey(), jedisId))){
                        result.put("status", 2);
                    }else {
                        result.put("status", 0);
                    }
                }
            }
        }else{// 报名了第二天就不用判断是否打卡了
            result.put("millis", DateTimeUtil.getDateMillis(DateTimeUtil.convertStrToDate(clockAttendVo.getClockDate(), "yyyy-MM-dd"), 18, 0, 0));
            result.put("status", 1);
        }
        return new BusiResult(BusiStatus.SUCCESS, result);
    }

    public BusiResult resetStatus(Long uid) {
        String jedisId = uid.toString()+DateTimeUtil.convertDate(new Date(), "yyyy-MM-dd");
        jedisService.hwrite(RedisKey.clock_status.getKey(), jedisId, "1");
        return new BusiResult(BusiStatus.SUCCESS);
    }

    public ClockRecordVo getRecord(ClockRecordVo clockRecordVo, Long uid) {
        Date date = new Date();
        String nextDate = DateTimeUtil.convertDate(DateTimeUtil.getNextDay(date, 1), "yyyy-MM-dd");
        ClockAttendVo clockAttendVo = getOneByJedisId(uid.toString()+nextDate, uid.toString(), nextDate);
        if(clockAttendVo==null){
            clockAttendVo = getOneByJedisId(uid.toString());
            if(clockAttendVo==null){// 今天没有报名就显示下一天的统计
                date = DateTimeUtil.getNextDay(date, 1);
            }
        }else{// 已经报名下一天的就显示下一天的统计
            date = DateTimeUtil.getNextDay(date, 1);
        }
        Integer attendAmount = jdbcTemplate.queryForObject("select count(1) from clock_attend_record where DATE_FORMAT(clock_date,'%Y-%m-%d') = DATE_FORMAT(?,'%Y-%m-%d') ", Integer.class, date);
        clockRecordVo.setAttendAmount(attendAmount==null?"0":String.valueOf(attendAmount));
        clockRecordVo.setGoldAmount(attendAmount==null?"0":String.valueOf(attendAmount*100));
        List<ClockAttendRecord> tList = jdbcTemplate.query("select * from clock_attend_record where DATE_FORMAT(clock_date,'%Y-%m-%d') = DATE_FORMAT(?,'%Y-%m-%d') limit 8 ", new BeanPropertyRowMapper<>(ClockAttendRecord.class), date);
        List<ClockUserVo> avatarList = new ArrayList<>();
        ClockUserVo clockUserVo;
        for(ClockAttendRecord t:tList){
            clockUserVo = new ClockUserVo();
            clockUserVo.setAvatar(usersService.getUsersByUid(t.getUid().longValue()).getAvatar());
            avatarList.add(clockUserVo);
        }
        clockRecordVo.setAvatarList(avatarList);
        return clockRecordVo;
    }

    @Deprecated
    public BusiResult attendClock(Long uid) {
        Users users = usersService.getUsersByUid(uid);
        if(users==null){
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        Date date = new Date();
        Date clockAttenDate = DateTimeUtil.getNextDay(date, 1);
        String clockAtteStr = DateTimeUtil.convertDate(clockAttenDate, "yyyy-MM-dd");
        String jedisId = uid.toString()+clockAtteStr;
        ClockAttendVo clockAttendVo = getOneByJedisId(jedisId, uid.toString(), clockAtteStr);
        if(clockAttendVo!=null){
            return new BusiResult(BusiStatus.NOAUTHORITY, "您已经参加活动了");
        }
        String lockVal = null;
        try {
            lockVal = jedisLockService.lock(RedisKey.lock_user_purse.getKey(uid.toString()), 10*1000);
            if (StringUtils.isEmpty(lockVal)) {
                return new BusiResult(BusiStatus.SERVERBUSY);
            }
            //#################################加锁start#################################
            UserPurse userPurse = userPurseService.getPurseByUid(uid);
            Long goldNum = userPurse.getGoldNum();
            // 判断余额是否足够
            if (goldNum.intValue() < 100) {
                return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
            }
            UserPurse userPurseAfter = userPurseService.updateGoldBySendGiftCache(userPurse, 100L);
            if (userPurseAfter == null) {
                return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
            }
            // 更新送礼物用户的钱包，减金币
            int result = userPurseUpdateService.reduceGoldNumFromDB(uid, 100L);
            if(result != 1){
                logger.error("reduceGoldFromDB uid:" + uid);
            }
        } catch (Exception e) {
            logger.error("reduceGoldFromCache uid:" + uid, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }finally {
            jedisLockService.unlock(RedisKey.lock_user_purse.getKey(uid.toString()), lockVal);
        }
        //#################################结束锁end#################################
        String userAttendCache = getUserAttend(uid);
        if(StringUtils.isEmpty(userAttendCache)){
            userAttendCache=""+jedisId;
        }else{
            userAttendCache=jedisId+","+userAttendCache;
        }
        jedisService.hwrite(RedisKey.clock_user_attend.getKey(), uid.toString(), userAttendCache);
        ClockAttendRecord clockAttendRecord = new ClockAttendRecord();
        clockAttendRecord.setUid(uid.intValue());
        clockAttendRecord.setClockDate(clockAttenDate);
        clockAttendRecord.setCreateTime(date);
        clockAttendRecordMapper.insert(clockAttendRecord);
        saveOneCache(entityToCache(clockAttendRecord), RedisKey.clock_attend.getKey(), jedisId);
        BillRecord billRecord = new BillRecord();
        billRecord.setBillId(UUIDUitl.get());
        billRecord.setUid(uid);
        billRecord.setObjId(clockAttendRecord.getClockRecordId().toString());
        billRecord.setObjType(Constant.BillType.clockAttend);
        billRecord.setGoldNum(100L);
        billRecord.setCreateTime(date);
        billRecordService.insertBillRecord(billRecord);
        clockResultService.sendMsg(uid, "【拉贝打卡】恭喜您成功报名"+clockAtteStr+"的打卡活动，请于当天晚上18:00至20:00之间前来打卡。");
        return new BusiResult(BusiStatus.SUCCESS);
    }

}
