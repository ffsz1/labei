package com.erban.main.service.noble;

import com.erban.main.model.*;
import com.erban.main.mybatismapper.NobleUsersMapper;
import com.erban.main.mybatismapper.NobleUsersMapperMgr;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.prettyNo.PrettyNoService;
import com.erban.main.service.room.RoomService;
import com.erban.main.service.user.UsersService;
import com.google.common.collect.Maps;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.utils.BlankUtil;
import com.xchat.common.utils.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class NobleUsersService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(NobleUsersService.class);


    @Autowired
    private NobleUsersMapper nobleUsersMapper;
    @Autowired
    private NobleUsersMapperMgr nobleUsersMapperMgr;
    @Autowired
    private NobleResService nobleResService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private PrettyNoService prettyNoService;
    @Autowired
    private NobleHelperService nobleHelperService;

    /**
     * 获取用户的贵族信息，cache -> db -> cache
     * 如果贵族信息已过期，返回null
     *
     * @param uid
     * @return
     */
    public NobleUsers getNobleUser(Long uid) {
        String json = jedisService.hget(RedisKey.noble_users.getKey(), uid.toString());
        if (BlankUtil.isBlank(json)) {
            NobleUsers nobleUsers = getValidNobleUserFromDb(uid);
            if (nobleUsers != null) {
                jedisService.hset(RedisKey.noble_users.getKey(), uid.toString(), gson.toJson(nobleUsers));
            }
            return nobleUsers;
        }
        NobleUsers noblesUsers = gson.fromJson(json, NobleUsers.class);
        if (DateTimeUtil.compareTime(noblesUsers.getExpire(), new Date()) > 0) {
            return noblesUsers;
        }
        return null;
    }

    public Map<Long, NobleUsers> getNobleUserMap(String[] uidArr) {
        List<String> list = jedisService.hmread(RedisKey.noble_users.getKey(), uidArr);
        Map<Long, NobleUsers> map = Maps.newHashMap();
        if (BlankUtil.isBlank(list)) {
            return map;
        }
        for (String json : list) {
            if(BlankUtil.isBlank(json)) continue;
            NobleUsers nobleUsers = gson.fromJson(json, NobleUsers.class);
            map.put(nobleUsers.getUid(), nobleUsers);
        }
        return map;
    }

    /**
     * 获取快要过期的贵族用户信息
     * 三天内过期的用户
     *
     * @return
     */
    public List<NobleUsers> getWillExpireNobleUser() {
        NobleUsersExample example = new NobleUsersExample();
        Date today = new Date();
        Date date1 = DateTimeUtil.getNextDay(today, 0);
        Date date2 = DateTimeUtil.getNextDay(today, 3);
        example.createCriteria().andExpireBetween(date1, date2).andStatusEqualTo((byte) 0);
        return nobleUsersMapper.selectByExample(example);
    }

    /**
     * 获取已经过期的贵族用户信息
     *
     * @return
     */
    public List<NobleUsers> getHadExpireNobleUser() {
        NobleUsersExample example = new NobleUsersExample();
        example.createCriteria().andExpireLessThan(new Date()).andStatusEqualTo((byte) 1);
        return nobleUsersMapper.selectByExample(example);
    }

    /**
     * 释放已过期的贵族用户
     */
    public void releaseAllExpireNoble() {
        List<NobleUsers> list = getHadExpireNobleUser();
        if (BlankUtil.isBlank(list)) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            NobleUsers nobleUsers = list.get(i);

        }
    }

    public void releaseExpireNoble(NobleUsers nobleUsers){
        Users users = usersService.getUsersByUid(nobleUsers.getUid());
        users.setNobleId(0);
        users.setNobleName("");
        usersService.updateUser(users);
        // 解绑靓号
        if (nobleUsers.getGoodNum() > 0) {
            BusiResult busiResult = prettyNoService.unBundPrettyNo(nobleUsers.getUid());
            logger.info("unBundPrettyNo result: {}, mess:{}, uid:{}", busiResult.getCode(), busiResult.getMessage()
                    , nobleUsers.getUid());
        }
        Room room = roomService.getRoomByUid(nobleUsers.getUid());
        // 房间背景回收
        if (room != null && !BlankUtil.isBlank(nobleUsers.getRoomBackground()) && nobleUsers.getRoomBackground()
                .equalsIgnoreCase(room.getDefBackpic())) {
            try {
                room.setDefBackpic("");
                roomService.updateRoomNeteaseAndDB(room);
            } catch (Exception e) {
                logger.error("updateRoomNeteaseAndDB error, uid:" + nobleUsers.getUid(), e);
            }
        }
        nobleUsers.setGoodNum(0L);
        nobleUsers.setRoomBackground("");
        nobleUsers.setRoomBackgroundId(null);
        nobleUsers.setStatus((byte) 0);
        nobleUsers.setRecomCount((byte) 0);
        jedisService.hdel(RedisKey.noble_users.getKey(), nobleUsers.getUid().toString());
        logger.info("release noble_user cache uid:{}, nobleId:{}", nobleUsers.getUid(), nobleUsers.getNobleId());
        nobleUsersMapper.updateByPrimaryKeySelective(nobleUsers);
        nobleHelperService.sendNobleHadExpireMess(nobleUsers.getUid(), nobleUsers.getNobleName());
        logger.info("sendNobleHadExpireMess uid:{}, nobleName:{}", nobleUsers.getUid(), nobleUsers.getNobleName());
    }
    /**
     * 给快过期的贵族用户发送提醒消息
     */
    public void sendWillExpireNotice() {
        List<NobleUsers> list = getWillExpireNobleUser();
        if (BlankUtil.isBlank(list)) {
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            NobleUsers nobleUsers = list.get(i);
            logger.info("sendWillExpireNotice uid:{}, expire:{}", nobleUsers.getUid(), nobleUsers.getExpire());
            nobleHelperService.sendNobleWillExpireMess(nobleUsers.getUid(), nobleUsers.getNobleName());
        }
    }

    /**
     * 判断等级是否足够
     *
     * @param uid
     * @param nobleId
     * @return
     */
    public boolean isLevelEnough(Long uid, Integer nobleId) {
        NobleUsers nobleUsers = getNobleUser(uid);
        if (nobleUsers != null && nobleUsers.getNobleId() >= nobleId) {
            return true;
        }
        return false;
    }

    public boolean isExistNoble(Long uid) {
        NobleUsers nobleUsers = nobleUsersMapper.selectByPrimaryKey(uid);
        if (nobleUsers != null) {
            return true;
        }
        return false;
    }

    /**
     * 插入一条贵族的个人信息
     *
     * @param uid
     * @param nobleRight
     * @return
     */
    public NobleUsers addNobleUsers(Long uid, NobleRight nobleRight) {
        logger.info("addNobleUsers param ==>> uid: {}, nobleId: {}", uid, nobleRight.getId());
        NobleUsers nobleUsers = buildNobleUsers(uid, nobleRight);
        int result = nobleUsersMapper.insertSelective(nobleUsers);
        logger.info("addNobleUsers insertSelective result: {}", result);
        return nobleUsers;
    }

    /**
     * 重置用户的贵族信息
     *
     * @param uid
     * @param nobleRight
     * @return
     */
    public NobleUsers resetNobleUsers(Long uid, NobleRight nobleRight) {
        logger.info("resetNobleUsers param ==>> uid: {}, nobleId: {}", uid, nobleRight.getId());
        NobleUsers nobleUsers = buildNobleUsers(uid, nobleRight);
        int result = nobleUsersMapper.updateByPrimaryKey(nobleUsers);
        logger.info("resetNobleUsers updateByPrimaryKeySelective result: {}", result);
        return nobleUsers;
    }


    public int reduceRecomCount(Long uid) {
        return nobleUsersMapperMgr.reduceRecomCount(uid);
    }

    public int updateRenewNobleUsers(Long uid, Byte count, Date expire) {
        return nobleUsersMapperMgr.updateRenewNobleUsers(expire, count, uid);
    }


    public void updateNobleUserDbAndCache(NobleUsers nobleUsers) {
        nobleUsersMapper.updateByPrimaryKeySelective(nobleUsers);
        jedisService.hset(RedisKey.noble_users.getKey(), nobleUsers.getUid().toString(), gson.toJson(nobleUsers));
    }

    public NobleUsers updateNobleUserCache(Long uid) {
        NobleUsers nobleUsers = getValidNobleUserFromDb(uid);
        if (nobleUsers != null) {
            jedisService.hset(RedisKey.noble_users.getKey(), uid.toString(), gson.toJson(nobleUsers));
        }
        return nobleUsers;
    }


    /**
     * 获取未过期的贵族信息
     *
     * @param uid
     * @return
     */
    public NobleUsers getValidNobleUserFromDb(Long uid) {
        NobleUsersExample example = new NobleUsersExample();
        example.createCriteria().andUidEqualTo(uid).andExpireGreaterThanOrEqualTo(new Date()).andStatusEqualTo((byte)1);
        List<NobleUsers> list = nobleUsersMapper.selectByExample(example);
        if (BlankUtil.isBlank(list)) {
            return null;
        }
        return list.get(0);
    }

    private NobleUsers buildNobleUsers(Long uid, NobleRight nobleRight) {
        NobleUsers nobleUsers = new NobleUsers();
        Date date = new Date();
        nobleUsers.setUid(uid);
        nobleUsers.setNobleId(nobleRight.getId());
        nobleUsers.setNobleName(nobleRight.getName());
        nobleUsers.setRecomCount(nobleRight.getRecomRoom());
        nobleUsers.setGoodNum(0L);
        nobleUsers.setStatus((byte) 1);
        nobleUsers.setRankHide((byte) 0);
        nobleUsers.setEnterHide((byte) 0);
        nobleUsers.setOpenTime(date);
        nobleUsers.setRenewTime(date);
        nobleUsers.setExpire(DateTimeUtil.getNextDay(date, 30));
//        nobleUsers.setRoomBackgroundId(null);
//        nobleUsers.setRoomBackground(null);
//        nobleUsers.setZonebgId(null);
//        nobleUsers.setZonebg(null);
//        nobleUsers.setCardbgId(null);
//        nobleUsers.setCardbg(null);
//        nobleUsers.setBadgeId(null);
//        nobleUsers.setBadge(null);
//        nobleUsers.setChatBubbleId(null);
//        nobleUsers.setChatBubble(null);
//        nobleUsers.setMicHaloId(null);
//        nobleUsers.setMicHalo(null);
//        nobleUsers.setMicDecorateId(null);
//        nobleUsers.setMicDecorate(null);
        List<NobleRes> resList = nobleResService.getDefNobleResList(nobleRight.getId());
        for (int i = 0; i < resList.size(); i++) {
            NobleRes nobleRes = resList.get(i);
            switch (nobleRes.getResType()) {
                case Constant.NobleResType.roombg:
                    nobleUsers.setRoomBackgroundId(nobleRes.getId());
                    nobleUsers.setRoomBackground(nobleRes.getValue());
                    break;
                case Constant.NobleResType.bubble:
                    nobleUsers.setChatBubbleId(nobleRes.getId());
                    nobleUsers.setChatBubble(nobleRes.getValue());
                    break;
                case Constant.NobleResType.michalo:
                    nobleUsers.setMicHaloId(nobleRes.getId());
                    nobleUsers.setMicHalo(nobleRes.getValue());
                    break;
                case Constant.NobleResType.headwear:
                    nobleUsers.setMicDecorateId(nobleRes.getId());
                    nobleUsers.setMicDecorate(nobleRes.getValue());
                    break;
                case Constant.NobleResType.badge:
                    nobleUsers.setBadgeId(nobleRes.getId());
                    nobleUsers.setBadge(nobleRes.getValue());
                    break;
                case Constant.NobleResType.cardbg:
                    nobleUsers.setCardbgId(nobleRes.getId());
                    nobleUsers.setCardbg(nobleRes.getValue());
                    break;
                case Constant.NobleResType.zonebg:
                    nobleUsers.setZonebgId(nobleRes.getId());
                    nobleUsers.setZonebg(nobleRes.getValue());
                    break;
            }
        }

        return nobleUsers;
    }

    public static void main(String[] args) {
        Date today = new Date();
        Date date1 = DateTimeUtil.getNextDay(today, 0);
        Date date2 = DateTimeUtil.getNextDay(today, 3);

        System.out.println(date1);
        System.out.println(date2);

        System.out.println(DateTimeUtil.convertDate(date1));
        System.out.println(DateTimeUtil.convertDate(date2));

    }
}
