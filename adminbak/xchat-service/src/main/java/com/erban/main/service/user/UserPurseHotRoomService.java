package com.erban.main.service.user;

import com.erban.main.config.SystemConfig;
import com.erban.main.model.*;
import com.erban.main.mybatismapper.HomeHotManualRecommMapper;
import com.erban.main.mybatismapper.UserPurseHotRoomRecordMapper;
import com.erban.main.param.neteasepush.NeteaseSendMsgParam;
import com.erban.main.service.SendSysMsgService;
import com.erban.main.service.base.CacheBaseService;
import com.erban.main.service.record.BillRecordService;
import com.erban.main.service.room.RoomService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.UserPurseHotRoomRecordVo;
import com.xchat.common.UUIDUitl;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserPurseHotRoomService extends CacheBaseService<UserPurseHotRoomRecord, UserPurseHotRoomRecordVo> {
    @Autowired
    private UsersService usersService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private UserPurseService userPurseService;
    @Autowired
    private UserPurseUpdateService userPurseUpdateService;
    @Autowired
    private UserPurseHotRoomRecordMapper userPurseHotRoomRecordMapper;
    @Autowired
    private HomeHotManualRecommMapper homeHotManualRecommMapper;
    @Autowired
    private SendSysMsgService sendSysMsgService;
    @Autowired
    private BillRecordService billRecordService;

    @Override
    public UserPurseHotRoomRecordVo getOneByJedisId(String jedisId) {
        return getOne(RedisKey.user_purse_hot_room_record.getKey(), jedisId, "select * from user_purse_hot_room_record where record_id = ? ", jedisId);
    }

    @Override
    public UserPurseHotRoomRecordVo entityToCache(UserPurseHotRoomRecord entity) {
        UserPurseHotRoomRecordVo userPurseHotRoomRecordVo = new UserPurseHotRoomRecordVo();
        Users users = usersService.getUsersByUid(entity.getUid());
        userPurseHotRoomRecordVo.setUserNo(users.getErbanNo());
        userPurseHotRoomRecordVo.setRoomNo(entity.getErbanNo());
        userPurseHotRoomRecordVo.setGoldNum(entity.getGoldNum());
        userPurseHotRoomRecordVo.setDate(DateTimeUtil.convertDate(entity.getStartTime(), "yyyy-MM-dd"));
        userPurseHotRoomRecordVo.setHour(DateTimeUtil.convertDate(entity.getStartTime(), "HH:mm")+"-"+DateTimeUtil.convertDate(entity.getEndTime(), "HH:mm"));
        userPurseHotRoomRecordVo.setCreateTime(entity.getCreateTime());
        return userPurseHotRoomRecordVo;
    }

    public String refreshListCache(String jedisCode, String jedisKey) {
        return refreshListCacheByKey(null, jedisCode, jedisKey, "getRecordId", "select * from user_purse_hot_room_record where uid = ? order by record_id desc ", jedisKey);
    }

    public String getStrList(String jedisKey){
        String str = jedisService.hget(RedisKey.user_purse_hot_room_record_list.getKey(), jedisKey);
        if(StringUtils.isEmpty(str)){
            str = refreshListCache(RedisKey.user_purse_hot_room_record_list.getKey(), jedisKey);
            if(StringUtils.isEmpty(str)){
                return null;
            }
        }
        return str;
    }

    public BusiResult purse(Long uid, Long erbanNo, String date, String hour){
        Users users = usersService.getUsersByUid(uid);
        if(users==null){
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        Users users1 = usersService.getUsersByErBanNo(erbanNo);
        if(users1==null){
            return new BusiResult(BusiStatus.ROOMNOTEXIST);
        }
        Room room = roomService.getRoomByUid(users1.getUid());
        if(room==null){
            return new BusiResult(BusiStatus.ROOMNOTEXIST);
        }
        Date now = new Date();
        Date startTime = DateTimeUtil.convertStrToDate(date+" "+hour, "yyyy-MM-dd HH:mm");
        if(DateTimeUtil.compareTime(now, startTime)==1){
            return new BusiResult(BusiStatus.DATEEXISTS);
        }
        Date endTime = DateTimeUtil.getNextHour(startTime, 1);
        Integer isCan = jdbcTemplate.queryForObject("SELECT count(1) from home_hot_manual_recomm where uid = ? and `status` = 1 and seq_no = 1 and start_valid_time = ? and end_valid_time = ? ", Integer.class, users1.getUid(), startTime, endTime);
        if(isCan!=null&&isCan!=0){
            return new BusiResult(BusiStatus.HOTEXISTS);
//            return new BusiResult(BusiStatus.HOTEXISTS, jdbcTemplate.queryForList("SELECT CONCAT(DATE_FORMAT(start_valid_time,'%H:%i'), '-', DATE_FORMAT(end_valid_time,'%H:%i')) as hour from home_hot_manual_recomm where `status` = 1 and seq_no = 1 and DATE_FORMAT(start_valid_time,'%Y-%m-%d') = DATE_FORMAT(?,'%Y-%m-%d') order by start_valid_time asc ", startTime));
        }
        Integer gold = 3000;
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
            if (goldNum.intValue() < gold) {
                return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
            }
            UserPurse userPurseAfter = userPurseService.updateGoldBySendGiftCache(userPurse, gold.longValue());
            if (userPurseAfter == null) {
                return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
            }
            // 更新送礼物用户的钱包，减金币
            int result = userPurseUpdateService.reduceGoldNumFromDB(uid, gold.longValue());
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
        String listCache = getStrList(uid.toString());
        UserPurseHotRoomRecord userPurseHotRoomRecord = new UserPurseHotRoomRecord();
        userPurseHotRoomRecord.setUid(uid);
        userPurseHotRoomRecord.setErbanNo(erbanNo.intValue());
        userPurseHotRoomRecord.setGoldNum(gold);
        userPurseHotRoomRecord.setStartTime(startTime);
        userPurseHotRoomRecord.setEndTime(endTime);
        userPurseHotRoomRecord.setCreateTime(now);
        userPurseHotRoomRecordMapper.insertSelective(userPurseHotRoomRecord);
        if(StringUtils.isEmpty(listCache)){
            listCache=""+userPurseHotRoomRecord.getRecordId();
        }else{
            listCache=userPurseHotRoomRecord.getRecordId()+","+listCache;
        }
        jedisService.hwrite(RedisKey.user_purse_hot_room_record_list.getKey(), uid.toString(), listCache);
        HomeHotManualRecomm homeHotManualRecomm = new HomeHotManualRecomm();
        homeHotManualRecomm.setUid(users1.getUid());
        homeHotManualRecomm.setSeqNo(1);
        homeHotManualRecomm.setStatus(new Byte("1"));
        homeHotManualRecomm.setStartValidTime(startTime);
        homeHotManualRecomm.setEndValidTime(endTime);
        homeHotManualRecomm.setCreateTime(now);
        homeHotManualRecommMapper.insertSelective(homeHotManualRecomm);
        BillRecord billRecord = new BillRecord();
        billRecord.setBillId(UUIDUitl.get());
        billRecord.setUid(uid);
        billRecord.setObjId(homeHotManualRecomm.getRecommId().toString());
        billRecord.setObjType(Constant.BillType.purseHot);
        billRecord.setGoldNum(gold.longValue());
        billRecord.setCreateTime(now);
        billRecordService.insertBillRecord(billRecord);
        // 发送消息给用户
        NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
        neteaseSendMsgParam.setFrom(SystemConfig.secretaryUid);
        neteaseSendMsgParam.setOpe(0);
        neteaseSendMsgParam.setType(0);
        neteaseSendMsgParam.setTo(uid.toString());
        neteaseSendMsgParam.setBody("恭喜您成功购买"+DateTimeUtil.convertDate(startTime, "yyyy-MM-dd HH:mm")+"-"+DateTimeUtil.convertDate(endTime, "HH:mm")+"的热门推荐位，您购买的房间【"+erbanNo+"】会在对应时段自动上推荐，请知悉。");
        sendSysMsgService.sendMsg(neteaseSendMsgParam);
        return new BusiResult(BusiStatus.SUCCESS);
    }

}
