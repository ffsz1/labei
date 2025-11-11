package com.erban.main.service.clock;

import com.erban.main.config.SystemConfig;
import com.erban.main.model.*;
import com.erban.main.mybatismapper.ClockResultRecordMapper;
import com.erban.main.param.neteasepush.NeteaseSendMsgParam;
import com.erban.main.service.SendSysMsgService;
import com.erban.main.service.base.CacheBaseService;
import com.erban.main.service.record.BillRecordService;
import com.erban.main.service.user.UserPurseService;
import com.erban.main.service.user.UserPurseUpdateService;
import com.erban.main.service.user.UsersService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.*;
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
public class ClockResultService extends CacheBaseService<ClockResultRecord, ClockResultVo> {
    @Autowired
    private ClockResultRecordMapper clockResultRecordMapper;
    @Autowired
    private UsersService usersService;
    @Autowired
    private UserPurseService userPurseService;
    @Autowired
    private ClockAttendService clockAttendService;
    @Autowired
    private SendSysMsgService sendSysMsgService;
    @Autowired
    private UserPurseUpdateService userPurseUpdateService;
    @Autowired
    private BillRecordService billRecordService;

    @Override
    public ClockResultVo getOneByJedisId(String jedisId) {
        return getOne(RedisKey.clock_result.getKey(), jedisId+ DateTimeUtil.convertDate(new Date(), "yyyy-MM-dd"), "select * from clock_result_record where uid = ? and DATE_FORMAT(clock_date,'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d') ", jedisId);
    }

    public ClockResultVo getOneByJedisId(String jedisKey, Object... arg) {
        return getOne(RedisKey.clock_result.getKey(), jedisKey, "select * from clock_result_record where uid = ? and DATE_FORMAT(clock_date,'%Y-%m-%d') = DATE_FORMAT(?,'%Y-%m-%d') ", arg);
    }

    @Override
    public ClockResultVo entityToCache(ClockResultRecord entity) {
        ClockResultVo clockAttendVo = new ClockResultVo();
        clockAttendVo.setClockResultId(entity.getClockResultId());
        clockAttendVo.setClockDate(DateTimeUtil.convertDate(entity.getClockDate(), "yyyy-MM-dd"));
        clockAttendVo.setUid(entity.getUid().longValue());
        clockAttendVo.setGoldAmount(entity.getGoldAmount());
        Users users = usersService.getUsersByUid(entity.getUid().longValue());
        if(users!=null){
            clockAttendVo.setNick(users.getNick());
            clockAttendVo.setAvatar(users.getAvatar());
        }
        return clockAttendVo;
    }

    public String refreshUserResultCache(String jedisCode, String uid) {
        List<ClockResultRecord> tList = jdbcTemplate.query("select * from clock_result_record where uid = ? ORDER BY clock_date desc ", new BeanPropertyRowMapper<>(ClockResultRecord.class), uid);
        if (tList == null || tList.size() == 0) {
            jedisService.hwrite(jedisCode, uid, "");
            return null;
        }
        StringBuffer str = new StringBuffer();
        ClockResultRecord clockResultRecord;
        for (int i = 0; i < tList.size(); i++) {
            clockResultRecord=tList.get(i);
            if (i == 0) {
                str.append(clockResultRecord.getUid().toString()).append(DateTimeUtil.convertDate(clockResultRecord.getClockDate(), "yyyy-MM-dd"));
            } else {
                str.append(",").append(clockResultRecord.getUid().toString()).append(DateTimeUtil.convertDate(clockResultRecord.getClockDate(), "yyyy-MM-dd"));
            }
        }
        jedisService.hwrite(jedisCode, uid, str.toString());
        return str.toString();
    }

    public String getUserResult(Long uid){
        String str = jedisService.hget(RedisKey.clock_user_result.getKey(), uid.toString());
        if (StringUtils.isEmpty(str)) {
            str = refreshUserResultCache(RedisKey.clock_user_result.getKey(), uid.toString());
            if (StringUtils.isEmpty(str)) {
                return null;
            }
        }
        return str;
    }

    public ClockRecordVo getRecord(ClockRecordVo clockRecordVo) {
        ClockRecordVo recordVo;
        String resultRecord = jedisService.get(RedisKey.clock_result_record.getKey());
        if(resultRecord==null){
            recordVo = refreshRecord();
        }else{
            recordVo = gson.fromJson(resultRecord, ClockRecordVo.class);
        }
        clockRecordVo.setClockSuccess(recordVo.getClockSuccess());
        clockRecordVo.setClockError(recordVo.getClockError());
        clockRecordVo.setClockList(recordVo.getClockList());
        return clockRecordVo;
    }

    private ClockRecordVo refreshRecord(){
        ClockRecordVo recordVo = new ClockRecordVo();
        List<ClockUserVo> clockList = new ArrayList<>();
        ClockUserVo clockUserVo;
        int hour = DateTimeUtil.getHour(new Date());
        Date date;
        if(hour>19){
            date = new Date();
        }else {
            date = DateTimeUtil.getNextDay(new Date(), -1);
        }
        Integer attendAmount = jdbcTemplate.queryForObject("select count(1) from clock_attend_record where DATE_FORMAT(clock_date,'%Y-%m-%d') = DATE_FORMAT(?,'%Y-%m-%d') ", Integer.class, date);
        Integer resultAmount = jdbcTemplate.queryForObject("select count(1) from clock_result_record where DATE_FORMAT(clock_date,'%Y-%m-%d') = DATE_FORMAT(?,'%Y-%m-%d') ", Integer.class, date);
        if(attendAmount==null){
            attendAmount=0;
        }
        if(resultAmount==null){
            resultAmount=0;
        }
        recordVo.setClockSuccess(resultAmount);
        recordVo.setClockError(attendAmount-resultAmount);
        List<Map<String, Object>> clockMap = jdbcTemplate.queryForList("select DATE_FORMAT(crr.create_time,'%T') as createTime, crr.uid as uid, u.erban_no as erbanNo, u.avatar as avatar from clock_result_record crr INNER JOIN users u on crr.uid = u.uid where DATE_FORMAT(crr.clock_date,'%Y-%m-%d') = DATE_FORMAT(?,'%Y-%m-%d') ORDER BY clock_result_id", date);
        if(clockMap!=null&&clockMap.size()!=0){
            clockUserVo = new ClockUserVo();
            clockUserVo.setUid(Long.valueOf(clockMap.get(0).get("uid").toString()));
            clockUserVo.setUserNo(Long.valueOf(clockMap.get(0).get("erbanNo").toString()));
            clockUserVo.setAvatar(clockMap.get(0).get("avatar").toString());
            clockUserVo.setClockDesc(clockMap.get(0).get("createTime").toString()+"打卡");
            clockList.add(clockUserVo);
        }else {
            clockUserVo = new ClockUserVo();
            clockUserVo.setUid(0L);
            clockUserVo.setUserNo(0L);
            clockList.add(clockUserVo);
        }
        clockMap = jdbcTemplate.queryForList("select crr.gold_amount as goldAmount, crr.uid as uid, u.erban_no as erbanNo, u.avatar as avatar from clock_result_record crr INNER JOIN users u on crr.uid = u.uid where DATE_FORMAT(crr.clock_date,'%Y-%m-%d') = DATE_FORMAT(?,'%Y-%m-%d') ORDER BY crr.gold_amount DESC ", date);
        if(clockMap!=null&&clockMap.size()!=0){
            clockUserVo = new ClockUserVo();
            clockUserVo.setUid(Long.valueOf(clockMap.get(0).get("uid").toString()));
            clockUserVo.setUserNo(Long.valueOf(clockMap.get(0).get("erbanNo").toString()));
            clockUserVo.setAvatar(clockMap.get(0).get("avatar").toString());
            clockUserVo.setClockDesc(clockMap.get(0).get("goldAmount").toString()+"金币");
            clockList.add(clockUserVo);
        }else {
            clockUserVo = new ClockUserVo();
            clockUserVo.setUid(0L);
            clockUserVo.setUserNo(0L);
            clockList.add(clockUserVo);
        }
        clockMap = jdbcTemplate.queryForList("select COUNT(1) as countNum, crr.uid as uid, u.erban_no as erbanNo, u.avatar as avatar from clock_result_record crr INNER JOIN users u on crr.uid = u.uid GROUP BY crr.uid ORDER BY COUNT(1) DESC ");
        if(clockMap!=null&&clockMap.size()!=0){
            clockUserVo = new ClockUserVo();
            clockUserVo.setUid(Long.valueOf(clockMap.get(0).get("uid").toString()));
            clockUserVo.setUserNo(Long.valueOf(clockMap.get(0).get("erbanNo").toString()));
            clockUserVo.setAvatar(clockMap.get(0).get("avatar").toString());
            clockUserVo.setClockDesc("打卡"+clockMap.get(0).get("countNum").toString()+"次");
            clockList.add(clockUserVo);
        }else {
            clockUserVo = new ClockUserVo();
            clockUserVo.setUid(0L);
            clockUserVo.setUserNo(0L);
            clockList.add(clockUserVo);
        }
        recordVo.setClockList(clockList);
        jedisService.set(RedisKey.clock_result_record.getKey(), gson.toJson(recordVo));
        return recordVo;
    }

    public BusiResult doClock(Long uid) {
        Date date = new Date();
        int hour = DateTimeUtil.getHour(date);
        if(hour!=18&&hour!=19&&hour!=20){
            return new BusiResult(BusiStatus.NOAUTHORITY, "打卡时间还没到");
        }
        Users users = usersService.getUsersByUid(uid);
        if(users==null){
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        ClockAttendVo clockAttendVo = clockAttendService.getOneByJedisId(uid.toString());
        if(clockAttendVo==null){
            return new BusiResult(BusiStatus.NOTEXISTS, "您没有参加这次打卡活动");
        }
        ClockResultVo clockResultVo = getOneByJedisId(uid.toString());
        if(clockResultVo!=null){
            return new BusiResult(BusiStatus.NOAUTHORITY, "您已经打过卡了");
        }
        String jedisId = uid.toString()+DateTimeUtil.convertDate(new Date(), "yyyy-MM-dd");
        String userResultCache = getUserResult(uid);
        if(StringUtils.isEmpty(userResultCache)){
            userResultCache=""+jedisId;
        }else{
            userResultCache+=","+jedisId;
        }
        jedisService.hwrite(RedisKey.clock_user_result.getKey(), uid.toString(), userResultCache);
        ClockResultRecord clockResultRecord = new ClockResultRecord();
        clockResultRecord.setUid(uid.intValue());
        clockResultRecord.setGoldAmount(0);// 结束后统一重新附上结果
        clockResultRecord.setClockDate(new Date());
        clockResultRecord.setCreateTime(new Date());
        clockResultRecordMapper.insert(clockResultRecord);
        saveOneCache(entityToCache(clockResultRecord), RedisKey.clock_result.getKey(), jedisId);
        return new BusiResult(BusiStatus.SUCCESS);
    }

    public BusiResult doTestClock(Long uid) {
        Users users = usersService.getUsersByUid(uid);
        if(users==null){
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        ClockAttendVo clockAttendVo = clockAttendService.getOneByJedisId(uid.toString());
        if(clockAttendVo==null){
            return new BusiResult(BusiStatus.NOTEXISTS, "您没有参加这次打卡活动");
        }
        ClockResultVo clockResultVo = getOneByJedisId(uid.toString());
        if(clockResultVo!=null){
            return new BusiResult(BusiStatus.NOAUTHORITY, "您已经打过卡了");
        }
        String jedisId = uid.toString()+DateTimeUtil.convertDate(new Date(), "yyyy-MM-dd");
        String userResultCache = getUserResult(uid);
        if(StringUtils.isEmpty(userResultCache)){
            userResultCache=""+jedisId;
        }else{
            userResultCache=jedisId+","+userResultCache;
        }
        jedisService.hwrite(RedisKey.clock_user_result.getKey(), uid.toString(), userResultCache);
        ClockResultRecord clockResultRecord = new ClockResultRecord();
        clockResultRecord.setUid(uid.intValue());
        clockResultRecord.setGoldAmount(0);// 结束后统一重新附上结果
        clockResultRecord.setClockDate(new Date());
        clockResultRecord.setCreateTime(new Date());
        clockResultRecordMapper.insert(clockResultRecord);
        saveOneCache(entityToCache(clockResultRecord), RedisKey.clock_result.getKey(), jedisId);
        return new BusiResult(BusiStatus.SUCCESS);
    }

    public BusiResult myClockResult(Long uid, Integer pageNum, Integer pageSize) {
        Users users = usersService.getUsersByUid(uid);
        if(users==null){
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        String attendStr = clockAttendService.getUserAttend(uid);
        String resultStr = getUserResult(uid);
        List<String> attendList = new ArrayList<>();
        if(!StringUtils.isEmpty(attendStr)){
            attendList = StringUtils.splitToList(attendStr, ",");
        }
        List<String> resultList = new ArrayList<>();
        if(!StringUtils.isEmpty(resultStr)){
            resultList = StringUtils.splitToList(resultStr, ",");
        }
        ClockMyResultVo clockMyResultVo = new ClockMyResultVo();
        clockMyResultVo.setGoldAmount(String.valueOf(attendList.size()*100));
        Integer resultAmount = jdbcTemplate.queryForObject("select SUM(gold_amount) from clock_result_record where uid = ?", Integer.class, uid);
        clockMyResultVo.setResultAmount(resultAmount==null?"0":String.valueOf(resultAmount));
        clockMyResultVo.setSuccessClock(String.valueOf(resultList.size()));
        if (pageNum == null || pageNum < 1) pageNum = 1;
        if (pageSize == null || pageSize < 1) pageSize = 10;
        Integer size = attendList.size();
        Integer skip = (pageNum - 1) * pageSize;
        if (skip < size) {
            if (skip + pageSize > size) {
                clockMyResultVo.setResultVoList(getResult(attendList.subList(skip, size), resultList, uid));
            }else{
                clockMyResultVo.setResultVoList(getResult(attendList.subList(skip, skip + pageSize), resultList, uid));
            }
        }
        return new BusiResult(BusiStatus.SUCCESS, clockMyResultVo);
    }

    private List<ClockResultVo> getResult(List<String> attendList, List<String> resultList, Long uid){
        List<ClockResultVo> clockResultVoList = new ArrayList<>();
        ClockAttendVo clockAttendVo;
        ClockResultVo clockResultVo;
        ClockResultVo resultVo;
        Date date = new Date();
        String dateStr = DateTimeUtil.convertDate(date, "yyyy-MM-dd");
        String nextStr = DateTimeUtil.convertDate(DateTimeUtil.getNextDay(date, 1), "yyyy-MM-dd");
        for(String attend:attendList){
            clockResultVo = new ClockResultVo();
            clockResultVo.setUid(uid);
            if(resultList.contains(attend)){
                resultVo = getOneByJedisId(attend, uid, attend.replaceAll(uid.toString(), ""));
                if(resultVo.getGoldAmount()==0){
                    clockResultVo.setStatus(3);
                    clockResultVo.setGoldAmount(0);
                }else{
                    clockResultVo.setStatus(1);
                    clockResultVo.setGoldAmount(resultVo.getGoldAmount());
                }
                clockResultVo.setClockDate(resultVo.getClockDate());
            }else {
                clockAttendVo = clockAttendService.getOneByJedisId(attend, uid, attend.replaceAll(uid.toString(), ""));
                if(dateStr.equals(clockAttendVo.getClockDate())||nextStr.equals(clockAttendVo.getClockDate())){
                    clockResultVo.setStatus(0);
                    clockResultVo.setGoldAmount(0);
                }else{
                    clockResultVo.setStatus(2);
                    clockResultVo.setGoldAmount(-100);
                }
                clockResultVo.setClockDate(clockAttendVo.getClockDate());
            }
            clockResultVoList.add(clockResultVo);
        }
        return clockResultVoList;
    }

    public void doResult(){
        List<ClockAttendRecord> attendRecordList = jdbcTemplate.query("select * from clock_attend_record where DATE_FORMAT(clock_date,'%Y-%m-%d') = DATE_FORMAT(now(),'%Y-%m-%d') ", new BeanPropertyRowMapper<>(ClockAttendRecord.class));
        List<ClockResultRecord> resultRecordList = jdbcTemplate.query("select * from clock_result_record where DATE_FORMAT(clock_date,'%Y-%m-%d') = DATE_FORMAT(now(),'%Y-%m-%d') ", new BeanPropertyRowMapper<>(ClockResultRecord.class));
        if(attendRecordList.size()==0){
            return;
        }
        if(resultRecordList.size()==0){// 全部失败
            for(ClockAttendRecord attendRecord:attendRecordList){
                try{
                    doResultAdd(attendRecord.getUid(), 100L, false);
                }catch (Exception e){

                }
            }
        }else if(attendRecordList.size()==resultRecordList.size()){//全部成功
            for(ClockAttendRecord attendRecord:attendRecordList){
                try{
                    doResultAdd(attendRecord.getUid(), 100L, true);
                }catch (Exception e){

                }
            }
        }else {
            ClockResultRecord resultRecord;
            Integer a=attendRecordList.size();
            Integer c=resultRecordList.size();
            if(c==1){// 只有一个人成功
                try{
                    resultRecord=resultRecordList.get(0);
                    doResultAdd(resultRecord.getUid(), 100L*a, true);
                }catch (Exception ee){

                }
            }else {
                Collections.shuffle(resultRecordList);// 随机打乱
                Double count = c*0.2;
                Integer bigCount = count.intValue()+1;
                Integer minCount = c-bigCount;
                Double price = (a-c)*100*(0.4+Math.random()*(0.6-0.4));
                Integer bigPrice = price.intValue()+1;
                Integer minPrice = (a-c)*100-bigPrice;
                Double d;
                Integer e;
                for (int i = 0;i<bigCount-1;i++){
                    try{
                        d = bigPrice * (0.2+Math.random()*(0.2));
                        e = d.intValue()+1;
                        if((bigPrice-e)<(bigCount-i-1)){
                            e = bigPrice - bigCount+i+1;
                        }
                        bigPrice-=e;
                        System.out.println("第"+(i+1)+"个："+e+"---"+bigPrice);
                        resultRecord=resultRecordList.get(i);
                        doResultAdd(resultRecord.getUid(), 100+e.longValue(), true);
                    }catch (Exception ee){

                    }
                }
                resultRecord=resultRecordList.get(bigCount-1);
                doResultAdd(resultRecord.getUid(), 100+bigPrice.longValue(), true);
                System.out.println("第"+bigCount+"个："+bigPrice);
                for (int i = 0;i<minCount-1;i++){
                    try{
                        d = minPrice * (0.1+Math.random()*(0.1));
                        e = d.intValue()+1;
                        if((minPrice-e)<(minCount-i-1)){
                            e = minPrice - minCount+i+1;
                        }
                        minPrice-=e;
                        System.out.println("第"+(i+1+bigCount)+"个："+e+"---"+minPrice);
                        resultRecord=resultRecordList.get(i+bigCount);
                        doResultAdd(resultRecord.getUid(), 100+e.longValue(), true);
                    }catch (Exception ee){

                    }
                }
                resultRecord=resultRecordList.get(minCount-1+bigCount);
                doResultAdd(resultRecord.getUid(), 100+minPrice.longValue(), true);
                System.out.println("第"+(minCount+bigCount)+"个："+minPrice);
            }
        }
        refreshRecord();
    }

    private void doResultAdd(Integer uid, Long goldPrice, boolean doType){
        String lockVal = null;
        ClockResultRecord clockResultRecord=null;
        ClockResultVo clockResultVo=null;
        try {
            lockVal = jedisLockService.lock(RedisKey.lock_user_purse.getKey(), uid, 10*1000);
            if(lockVal==null){
                return;
            }
            //#################################加锁start#################################
            UserPurse userPurse = userPurseService.getPurseByUid(uid.longValue());
            UserPurse userPurseAfter = userPurseService.updateGoldBySendGiftCache(userPurse, -goldPrice);
            if (userPurseAfter == null) {
                return;
            }
            if(doType){
                clockResultVo = getOneByJedisId(uid.toString());
                clockResultRecord = new ClockResultRecord();
                clockResultRecord.setClockResultId(clockResultVo.getClockResultId());
                clockResultRecord.setGoldAmount(goldPrice.intValue());
                clockResultRecordMapper.updateByPrimaryKeySelective(clockResultRecord);
                clockResultVo.setGoldAmount(goldPrice.intValue());
                saveOneCache(clockResultVo, RedisKey.clock_result.getKey(), uid.toString()+DateTimeUtil.convertDate(new Date(), "yyyy-MM-dd"));
            }
            // 更新送礼物用户的钱包，减金币
            int result = userPurseUpdateService.addChargeGoldNumFromDB(uid.longValue(), goldPrice);
            BillRecord billRecord = new BillRecord();
            billRecord.setBillId(UUIDUitl.get());
            billRecord.setUid(uid.longValue());
            billRecord.setObjId(clockResultRecord==null?"0":clockResultRecord.getClockResultId().toString());
            billRecord.setObjType(Constant.BillType.clockResult);
            billRecord.setGoldNum(goldPrice);
            billRecord.setCreateTime(new Date());
            billRecordService.insertBillRecord(billRecord);
            sendMsg(uid.longValue(), "恭喜您在"+(clockResultVo==null?DateTimeUtil.convertDate(new Date(), "yyyy-MM-dd"):clockResultVo.getClockDate())+"的打卡挑战中分得"+goldPrice+"金币，请前往我的钱包查看。");
        } catch (Exception e) {
            logger.error("doResult uid:" + uid, e);
        }finally {
            jedisLockService.unlock(RedisKey.lock_user_purse.getKey(uid.toString()), lockVal);
        }
        //#################################结束锁end#################################
    }

    public void sendMsg(Long uid, String msg){
        // 发送消息给用户
        NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
        neteaseSendMsgParam.setFrom(SystemConfig.secretaryUid);
        neteaseSendMsgParam.setOpe(0);
        neteaseSendMsgParam.setType(0);
        neteaseSendMsgParam.setTo(uid.toString());
        neteaseSendMsgParam.setBody(msg);
        sendSysMsgService.sendMsg(neteaseSendMsgParam);
    }

}
