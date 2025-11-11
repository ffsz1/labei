package com.juxiao.xchat.service.api.room.impl;

import com.google.common.collect.Maps;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.room.StatBasicUsersDao;
import com.juxiao.xchat.dao.room.domain.StatBasicUsersDO;
import com.juxiao.xchat.dao.room.dto.RoomDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.event.DutyManager;
import com.juxiao.xchat.manager.common.mcoin.McoinMissionManager;
import com.juxiao.xchat.manager.common.room.RoomManager;
import com.juxiao.xchat.manager.external.im.ImRoomManager;
import com.juxiao.xchat.service.api.item.params.Custom;
import com.juxiao.xchat.service.api.item.params.ImRoomMessage;
import com.juxiao.xchat.service.api.room.RoomAttentionService;
import com.juxiao.xchat.service.api.room.StatBasicUsersService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class StatBasicUsersServiceImpl implements StatBasicUsersService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private StatBasicUsersDao basicUsersDao;
    @Autowired
    private DutyManager dutyManager;
    @Autowired
    private RedisManager redisManager;

    @Autowired
    private McoinMissionManager missionManager;
    @Autowired
    private ImRoomManager imRoomManager;
    @Autowired
    private RoomManager roomManager;
    @Autowired
    private RoomAttentionService attentionService;

    @Override
    public StatBasicUsersDO addBasicUser(Long uid, Long roomUid) throws WebServiceException {
        if (uid == null || roomUid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
//        StatBasicUsersDO statBasicUsers = new StatBasicUsersDO();
//        statBasicUsers.setRoomUid(roomUid);
//        statBasicUsers.setUid(uid);
//        statBasicUsers.setCreateTime(new Date());
//        if (uid != roomUid) {
//            // 添加记录
//            basicUsersDao.save(statBasicUsers);
//        }

//        // 大于60分钟停止
//        String minute = redisManager.hget(RedisKey.daily_room_time.getKey(), uid.toString());
//        if (StringUtils.isEmpty(minute)) {
//            redisManager.hincrBy(RedisKey.daily_room_time.getKey(), uid.toString(), 1L);
//        } else {
//            try {
//                int onlineTime = Integer.valueOf(minute);
//                if (onlineTime < 60) {
//                    redisManager.hincrBy(RedisKey.daily_room_time.getKey(), uid.toString(), 1L);
//                }
//
//                if (onlineTime >= 30) {
//                    dutyManager.updateDailytimeFinish(uid, 13);
//                }
//
//                if (onlineTime >= 60) {
//                    dutyManager.updateDailytimeFinish(uid, 14);
//                }
//            } catch (Exception e) {
//            }
//        }
        long minute = redisManager.hincrBy(RedisKey.daily_room_time.getKey(), uid + "_" + roomUid, 1L);
        if (minute == 10) {
            missionManager.finish(uid, 11);
            //TODO 在线时长超过10分钟推消息通知客户端弹窗关注房主
//            this.push(roomUid, uid, minute);
        }
        return null;
    }

    @Override
    public void countRoomOnline(Long roomUid, Long time, Long uid) {
        StatBasicUsersDO statBasicUsers = basicUsersDao.getLastUpdateTime(roomUid, uid);
        long preMinutes = System.currentTimeMillis() - 59000;
        if (statBasicUsers != null && statBasicUsers.getCreateTime() != null && statBasicUsers.getCreateTime().getTime() > preMinutes) {
            log.warn("[ 房间在线时长 ] 上一次上传不足一分钟，roomUid:>{}, uid:>{}, 前一分钟:>{}, 最新记录时间:>{}", roomUid, uid, preMinutes, statBasicUsers.getCreateTime().getTime());
            return;
        }

//        statBasicUsers = new StatBasicUsersDO();
//        statBasicUsers.setRoomUid(roomUid);
//        statBasicUsers.setUid(uid);
//        statBasicUsers.setCreateTime(new Date());
//        // 添加记录
//        basicUsersDao.save(statBasicUsers);


        long minutes = redisManager.hincrBy(RedisKey.daily_room_time.getKey(), uid + "_" + roomUid, 1L);

        //小丑诞生活动
//        if (!uid.equals(roomUid)) {
//            try {
//                clownBornManager.roomOnline(uid, minutes);
//            } catch (WebServiceException e) {
//            }
//        }
//        try {
                // TODO 在线时长超过10分钟推消息通知客户端弹窗关注房主
//            this.push(roomUid, uid, minutes);
//        } catch (Exception e) {
//
//        }

        if (minutes == 10) {
            try {
                missionManager.finish(uid, 11);
            } catch (WebServiceException e) {
            }

        }

        if (minutes > 0 && minutes % 20 == 0) {
            String freeDrawGiftlimit = redisManager.hget(RedisKey.gift_draw_free_limit_hash.getKey(), uid.toString());
            int toplimit = freeDrawGiftlimit == null ? 0 : Integer.valueOf(freeDrawGiftlimit);
            //每日免费捡海螺次数不能超过3次，此处设置缓存记录每日免费捡海螺次数
            if (toplimit >= 3) {
                return;
            }

            //获取房间在线时长
            redisManager.hincrBy(RedisKey.gift_draw_free_limit_hash.getKey(), uid.toString(), 1L);
        }
    }

    private void push(long roomUid, long uid, long minute) {
        if (minute != 5) {
            return;
        }

        RoomDTO roomDto = roomManager.getUserRoom(roomUid);
        if (roomDto == null) {
            return;
        }
        //如果房主是自己，则不显示关注
        if (roomDto.getUid() == uid) {
            return;
        }

        boolean isAttention = attentionService.checkAttentions(uid, roomDto.getRoomId());
        if (isAttention) {
            return;
        }
        Custom custom = new Custom();
        custom.setFirst(2);
        custom.setSecond(23);

        Map<String, Object> data = Maps.newHashMap();
        data.put("uid", uid);
        data.put("minute", minute);
        custom.setData(data);

        ImRoomMessage msginfo = new ImRoomMessage();
        msginfo.setRoomId(roomDto.getRoomId().toString());
        msginfo.setCustom(custom);

        try {
            imRoomManager.pushRoomMsg(msginfo);
        } catch (Exception e) {
            log.error("[push] 在房间停留到达指定时间推送关注信息失败,异常信息:{}",e);
        }
    }
}
