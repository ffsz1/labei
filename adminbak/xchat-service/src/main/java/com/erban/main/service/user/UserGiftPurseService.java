package com.erban.main.service.user;

import com.erban.main.message.RoomMessage;
import com.erban.main.model.*;
import com.erban.main.model.level.LevelCharmVo;
import com.erban.main.model.level.LevelExerpenceVo;
import com.erban.main.mybatismapper.UserDrawGiftRecordMapper;
import com.erban.main.mybatismapper.UserGiftPurseMapper;
import com.erban.main.service.SendChatRoomMsgService;
import com.erban.main.service.SysConfService;
import com.erban.main.service.base.CacheBaseService;
import com.erban.main.service.duty.DutyService;
import com.erban.main.service.duty.DutyType;
import com.erban.main.service.gift.GiftService;
import com.erban.main.service.home.CheckExcessService;
import com.erban.main.service.level.LevelCharmService;
import com.erban.main.service.level.LevelExperienceService;
import com.erban.main.service.mq.ActiveMQService;
import com.erban.main.service.record.BillRecordService;
import com.erban.main.util.StringUtils;
import com.google.common.collect.Lists;
import com.google.gson.reflect.TypeToken;
import com.xchat.common.UUIDUitl;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

@Service
public class UserGiftPurseService extends CacheBaseService<UserGiftPurse, UserGiftPurse> {
    @Autowired
    private UserGiftPurseMapper userGiftPurseMapper;
    @Autowired
    private UserDrawGiftRecordMapper userDrawGiftRecordMapper;
    @Autowired
    private UsersService usersService;
    @Autowired
    private UserPurseService userPurseService;
    @Autowired
    private UserPurseUpdateService userPurseUpdateService;
    @Autowired
    private BillRecordService billRecordService;
    @Autowired
    private SysConfService sysConfService;
    @Autowired
    private GiftService giftService;
    @Autowired
    private ActiveMQService activeMQService;
    @Autowired
    private CheckExcessService checkExcessService;
    @Autowired
    private SendChatRoomMsgService sendChatRoomMsgService;
    @Autowired
    private LevelExperienceService levelExperienceService;
    @Autowired
    private LevelCharmService levelCharmService;
    @Autowired
    private DutyService dutyService;

    @Override
    public UserGiftPurse getOneByJedisId(String jedisId) {
        return null;
    }

    public UserGiftPurse getOneByJedisId(String uid, String giftId) {
        return getOne(RedisKey.user_gift_purse.getKey(), uid + giftId, "select * from user_gift_purse where uid = ? and gift_id = ? ", uid, giftId);
    }

    @Override
    public UserGiftPurse entityToCache(UserGiftPurse entity) {
        return entity;
    }

    public List<UserGiftPurse> getUserList(Integer uid) {
        List<UserGiftPurse> userGiftPurseList = new ArrayList<>();
        UserGiftPurse userGiftPurse;
        Integer[] giftDrawList = Constant.GiftDrawList.giftDrawList;
        for (Integer id : giftDrawList) {
            userGiftPurse = getOneByJedisId(uid.toString(), id.toString());
            if (userGiftPurse != null) {
                userGiftPurseList.add(userGiftPurse);
            }
        }
        return userGiftPurseList;
    }

    private static double genDoubleNumber() {
        DecimalFormat doubleFormat = new DecimalFormat("0.000000");
        Random ra = new Random();
        double dd = ra.nextDouble();
        double number = new Double(doubleFormat.format(dd));
        return number;
    }

    public boolean isFirst(Long uid, Integer type) {
        String str = jedisService.hget(RedisKey.user_gift_first.getKey(), uid.toString());
        if (StringUtils.isBlank(str)) {
            Integer isTrue = jdbcTemplate.queryForObject("select COUNT(1) from bill_record where uid = ? and obj_type = 10", Integer.class, uid);
            if (isTrue == 0) {
                return true;
            }
            jedisService.hset(RedisKey.user_gift_first.getKey(), uid.toString(), "1");
        }
        return false;
    }

    public Integer doOneDraw(Long uid, Integer type, double giftDrawRange[]) {
        Integer giftId = 0;
        double randomNumber = genDoubleNumber();
        if (randomNumber >= giftDrawRange[0] && randomNumber <= giftDrawRange[1]) {
            giftId = Constant.GiftDraw.gift1;
        } else if (randomNumber > giftDrawRange[1] && randomNumber <= giftDrawRange[2]) {
            giftId = Constant.GiftDraw.gift2;
        } else if (randomNumber > giftDrawRange[2] && randomNumber <= giftDrawRange[3]) {
            giftId = Constant.GiftDraw.gift3;
        } else if (randomNumber > giftDrawRange[3] && randomNumber <= giftDrawRange[4]) {
            giftId = Constant.GiftDraw.gift4;
        } else if (randomNumber > giftDrawRange[4] && randomNumber <= giftDrawRange[5]) {
            giftId = Constant.GiftDraw.gift5;
        } else if (randomNumber > giftDrawRange[5] && randomNumber <= giftDrawRange[6]) {
            giftId = Constant.GiftDraw.gift6;
        }
        return giftId;
    }

    public BusiResult doDraw(Long uid, Integer type, Long roomId) {
        SysConf isExchangeAwards = sysConfService.getSysConfById("lottery_box_option");
        if (isExchangeAwards == null || "0".equals(isExchangeAwards.getConfigValue())) {
            return new BusiResult(BusiStatus.ACTIVITYNOTEND);
        }
        Users users = usersService.getUsersByUid(uid);
        if (users == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        Long goldPrice;
        Integer num;
        double giftDrawRange[];
        boolean first = isFirst(uid, type);
        if (type == 1) {
            goldPrice = 20L;
            num = 1;
            if (first) {
                giftDrawRange = Constant.GiftDrawRange.gift_first20;
            } else {
                giftDrawRange = Constant.GiftDrawRange.gift20;
            }
        } else if (type == 2) {
            goldPrice = 200L;
            num = 10;
            if (first) {
                giftDrawRange = Constant.GiftDrawRange.gift_first200;
            } else {
                giftDrawRange = Constant.GiftDrawRange.gift200;
            }
        } else {
            return new BusiResult(BusiStatus.NOTEXISTS, "类型错误", "");
        }
        String lockVal = null;
        try {
            lockVal = jedisLockService.lock(RedisKey.lock_user_purse.getKey(uid.toString()), 10 * 1000);
            if (StringUtils.isEmpty(lockVal)) {
                return new BusiResult(BusiStatus.SERVERBUSY);
            }
            //#################################加锁start#################################
            UserPurse userPurse = userPurseService.getPurseByUid(uid);
            Long goldNum = userPurse.getGoldNum();
            // 判断余额是否足够
            if (goldNum < goldPrice) {
                return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
            }
            UserPurse userPurseAfter = userPurseService.updateGoldBySendGiftCache(userPurse, goldPrice);
            if (userPurseAfter == null) {
                return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
            }
            // 更新送礼物用户的钱包，减金币
            int result = userPurseUpdateService.reduceGoldNumFromDB(uid, goldPrice);
            if (result != 1) {
                logger.error("reduceGoldFromDB uid:" + uid);
            }
        } catch (Exception e) {
            logger.error("reduceGoldFromCache uid:" + uid, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        } finally {
            jedisLockService.unlock(RedisKey.lock_user_purse.getKey(uid.toString()), lockVal);
        }
        //#################################结束锁end#################################
        Map<Integer, Integer> result = new HashMap<>();
        Integer giftId;
        for (int i = 0; i < num; i++) {
            giftId = doOneDraw(uid, type, giftDrawRange);
            if (result.get(giftId) == null) {
                result.put(giftId, 1);
            } else {
                result.put(giftId, result.get(giftId) + 1);
            }
            UserDrawGiftRecord userDrawGiftRecord = new UserDrawGiftRecord();
            userDrawGiftRecord.setUid(uid);
            userDrawGiftRecord.setGiftId(giftId);
            userDrawGiftRecord.setNum(1);
            userDrawGiftRecord.setRoomId(roomId);
            userDrawGiftRecord.setType(type.byteValue());
            userDrawGiftRecord.setCreateTime(new Date());
            userDrawGiftRecordMapper.insert(userDrawGiftRecord);

            BillRecord billRecord = new BillRecord();
            billRecord.setBillId(UUIDUitl.get());
            billRecord.setUid(uid);
            billRecord.setObjId(type.toString());
            billRecord.setObjType(Constant.BillType.drawGift);
            billRecord.setGiftId(giftId);
            billRecord.setGoldNum(20L);// 记录每次抽奖的金额
            billRecord.setCreateTime(new Date());
            billRecordService.insertBillRecord(billRecord);
        }
        UserGiftPurse userGiftPurse;
        for (Integer id : result.keySet()) {
            userGiftPurse = getOneByJedisId(uid.toString(), id.toString());
            if (userGiftPurse == null) {
                userGiftPurse = new UserGiftPurse();
                userGiftPurse.setUid(uid);
                userGiftPurse.setGiftId(id);
                userGiftPurse.setCountNum(result.get(id));
                userGiftPurse.setCreateTime(new Date());
                userGiftPurseMapper.insertSelective(userGiftPurse);
            } else {
                userGiftPurse.setCountNum(userGiftPurse.getCountNum() + result.get(id));
                userGiftPurseMapper.updateByPrimaryKeySelective(userGiftPurse);
                jedisService.hset(RedisKey.user_gift_purse.getKey(), uid.toString() + id.toString(), gson.toJson(userGiftPurse));
            }
        }
        if (first) {
            jedisService.hset(RedisKey.user_gift_first.getKey(), uid.toString(), "1");
        }
        try {
            if (result.get(14) != null) {
                sendAllRoom(users, roomId, 14, result.get(14));
            }
//            else if (result.get(9) != null) {
//                sendAllRoom(users, roomId, 9, result.get(9));
//            }
        } catch (Exception e) {
            logger.error("发送全服抽礼物失败" + e.getMessage());
        }

        try {
            dutyService.updateDailyDuty(uid, DutyType.gift_draw.getDutyId());
        } catch (Exception e) {
        }


        return new BusiResult(BusiStatus.SUCCESS, gson.toJson(result));
    }

    private void sendAllRoom(Users users, Long roomId, Integer giftId, Integer giftNum) {
        if (giftId == null || giftNum == null) {
            logger.error("发送全服抽礼物失败：id数量为空");
            return;
        }
        Gift gift = giftService.getValidGiftById(giftId);
        if (gift == null) {
            logger.error("发送全服抽礼物失败：礼物失效了");
            return;
        }
        RoomMessage roomMessage = new RoomMessage();
        roomMessage.setMessId(UUIDUitl.get());
        roomMessage.setMessTime(new Date().getTime());
        roomMessage.setUid(users.getUid());
        roomMessage.setRoomId(roomId);
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        map.put("giftName", "【全服】" + gift.getGiftName());
        map.put("count", giftNum);
        map.put("nick", users.getNick());
        data.put("params", gson.toJson(map));
        roomMessage.setData(data);
        // 缓存消息的消费状态，便于队列消息做幂等处理
        jedisService.hwrite(RedisKey.mq_room_message_status.getKey(), roomMessage.getMessId(), gson.toJson(roomMessage));
        try {
            activeMQService.sendRoomMessage(roomMessage);
        } catch (Exception e) {
            checkExcessService.sendSms("发送mq报错");
            logger.error("发送mq报错" + e.getMessage());
        }
    }

    public void handleRoomMessage(RoomMessage roomMessage) {
        long start = System.currentTimeMillis();
        // 获取所有在线房间
        List<Map<String, Object>> roomList = jdbcTemplate.queryForList("SELECT uid ,room_id as roomId from room where valid = 1 and online_num >= 2 and uid <> 500000");
        if (roomList.size() == 0) {
            // 删除该标识，表示消息已经消费过
            jedisService.hdel(RedisKey.mq_room_message_status.getKey(), roomMessage.getMessId());
            return;
        }
        for (Map<String, Object> room : roomList) {
            if (roomMessage.getRoomId() != null && room.get("roomId") != null && roomMessage.getRoomId().intValue() == Integer.valueOf(room.get("roomId").toString())) {// 自己房间和在线人数少于2的不需要发送
                continue;
            }
            sendRoomMessage(roomMessage, Long.valueOf(room.get("roomId").toString()));
        }
        // 删除该标识，表示消息已经消费过
        jedisService.hdel(RedisKey.mq_room_message_status.getKey(), roomMessage.getMessId());
        logger.error("礼物抽奖全服处理总耗时:" + (System.currentTimeMillis() - start));
    }

    @Async
    public void sendRoomMessage(RoomMessage roomMessage, Long roomId) {
        try {
            sendChatRoomMsgService.sendSendChatRoomMsg(roomId, roomMessage.getUid().toString(), Constant.DefMsgType.roomMessage, Constant.DefMsgType.roomMessage, roomMessage.getData());
        } catch (Exception e) {
            logger.error("礼物抽奖全服处理失败:" + e.getMessage(), e);
        }
    }

    public BusiResult reduceGiftPurseCache(Long sendUid, Integer giftId, Integer giftNum, Long giftPrice) {
        String lockVal = null;
        try {
            lockVal = jedisLockService.lock(RedisKey.lock_user_gift.getKey(sendUid.toString() + giftId.toString()), 10 * 1000);
            if (StringUtils.isBlank(lockVal)) {
                return new BusiResult(BusiStatus.SERVERBUSY);
            }
            Map<String, Long> num = new HashMap<>();
            Long afterGoldNum;
            Long useGiftPurseNum;
            Long afterGiftPurseNum;
            UserGiftPurse userGiftPurse = getOneByJedisId(sendUid.toString(), giftId.toString());
            if (userGiftPurse == null || userGiftPurse.getCountNum() == 0) {
                afterGoldNum = Math.abs(giftPrice * giftNum);
                useGiftPurseNum = 0L;
                afterGiftPurseNum = 0L;
                // 扣除赠送用户的金币，扣除成功返回200
                // 多节点部署时可能存在并发问题，需要加上分布式锁
                int result = userPurseUpdateService.reduceGoldFromCache(sendUid, afterGoldNum);
                if (result != 200) {
                    switch (result) {
                        case 503:
                            return new BusiResult(BusiStatus.SERVERBUSY);
                        case 500:
                            return new BusiResult(BusiStatus.SERVERERROR);
                        case 403:
                            return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
                    }
                }
            } else if (userGiftPurse.getCountNum() > giftNum) {
                afterGoldNum = 0L;
                useGiftPurseNum = giftNum.longValue();
                afterGiftPurseNum = new Long(userGiftPurse.getCountNum() - giftNum);
                userGiftPurse.setCountNum(userGiftPurse.getCountNum() - giftNum);
                jedisService.hset(RedisKey.user_gift_purse.getKey(), sendUid.toString() + giftId.toString(), gson.toJson(userGiftPurse));
            } else {
                afterGoldNum = Math.abs(giftPrice * (giftNum - userGiftPurse.getCountNum()));
                useGiftPurseNum = userGiftPurse.getCountNum().longValue();
                afterGiftPurseNum = 0L;
                // 扣除赠送用户的金币，扣除成功返回200
                // 多节点部署时可能存在并发问题，需要加上分布式锁
                int result = userPurseUpdateService.reduceGoldFromCache(sendUid, afterGoldNum);
                if (result != 200) {
                    switch (result) {
                        case 503:
                            return new BusiResult(BusiStatus.SERVERBUSY);
                        case 500:
                            return new BusiResult(BusiStatus.SERVERERROR);
                        case 403:
                            return new BusiResult(BusiStatus.PURSEMONEYNOTENOUGH);
                    }
                }
                userGiftPurse.setCountNum(0);
                jedisService.hset(RedisKey.user_gift_purse.getKey(), sendUid.toString() + giftId.toString(), gson.toJson(userGiftPurse));
            }
            num.put("afterGoldNum", afterGoldNum);
            num.put("useGiftPurseNum", useGiftPurseNum);
            num.put("afterGiftPurseNum", afterGiftPurseNum);
            return new BusiResult(BusiStatus.SUCCESS, num);
        } catch (Exception e) {
            logger.error("reduceGiftPurseCache uid:" + sendUid + ", giftId: " + giftId + ", giftNum: " + giftNum + ", giftPrice: " + giftPrice, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        } finally {
            jedisLockService.unlock(RedisKey.lock_user_gift.getKey(sendUid.toString() + giftId.toString()), lockVal);
        }
    }

    public void refreshRank(Integer type) {
        Date now = new Date();
        if (type == 2) {
            now = DateTimeUtil.getLastDay(now, 1);
        }
        Date startDate = DateTimeUtil.formatBeginDate(now);
        Date endDate = DateTimeUtil.formatEndDate(now);
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select a.uid, u.erban_no as erbanNo, u.nick, u.gender, u.avatar, 0 as experLevel, 0 as charmLevel, SUM(b.gold_price) as tol from bill_record a force index(key3) INNER JOIN users u on a.uid = u.uid INNER JOIN gift b on a.gift_id = b.gift_id where a.obj_type = 10 and a.create_time >= ? and a.create_time <= ? GROUP BY a.uid ORDER BY tol desc limit 20", startDate, endDate);
        LevelExerpenceVo levelExerpenceVo;
        LevelCharmVo levelCharmVo;
        for (Map<String, Object> re : result) {
            levelExerpenceVo = levelExperienceService.getLevelExperience(Long.valueOf(re.get("uid").toString()));
            if (levelExerpenceVo != null) {
                re.put("experLevel", Integer.valueOf(levelExerpenceVo.getLevelName().substring(2)));
            }
            levelCharmVo = levelCharmService.getLevelCharm(Long.valueOf(re.get("uid").toString()));
            if (levelCharmVo != null) {
                re.put("charmLevel", Integer.valueOf(levelCharmVo.getLevelName().substring(2)));
            }
        }
        jedisService.hset(RedisKey.gift_draw_rank.getKey(), type.toString(), gson.toJson(result));
    }

    public BusiResult getRank(Integer type) {
        String result = jedisService.hget(RedisKey.gift_draw_rank.getKey(), type.toString());
        if (StringUtils.isNotBlank(result)) {
            return new BusiResult(BusiStatus.SUCCESS, gson.fromJson(result, new TypeToken<List<?>>() {
            }.getType()));
        } else {
            return new BusiResult(BusiStatus.SUCCESS, Lists.newArrayList());
        }
    }

    /**
     * 清除拉贝相关的信息
     */
    public void deleteDrawGiftNum() {
        jedisService.del(RedisKey.gift_draw_event_num.getKey());
        jedisService.del(RedisKey.gift_draw_num.getKey());
    }

}
