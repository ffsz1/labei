package com.juxiao.xchat.service.api.room.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateTimeUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.room.UserIntoRoomRecordDao;
import com.juxiao.xchat.dao.room.domain.UserIntoRoomRecordDO;
import com.juxiao.xchat.dao.room.dto.RoomUserinDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.service.api.room.RoomService;
import com.juxiao.xchat.service.api.room.UserRoomService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserRoomServiceImpl implements UserRoomService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private Gson gson;
    @Autowired
    private RoomService roomService;
    @Autowired
    private UsersManager usersManager;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private UserIntoRoomRecordDao userIntoRoomLogDao;

    @Override
    public RoomUserinDTO userIntoRoom(Long uid, Long roomUid) throws Exception {
        if (uid == null || roomUid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        UsersDTO usersDTO = usersManager.getUser(uid);
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        RoomUserinDTO roomDto = roomService.getRoom(roomUid, null, null, null, null, null);
        if (roomDto == null) {
            return null;
        }
        // 国庆活动房间停留   --代码已废弃
        //openRoomTime(uid, roomUid);
        // 添加用户进入房间的缓存
        redisManager.hset(RedisKey.user_in_room.getKey(), String.valueOf(uid), gson.toJson(roomDto));
        this.saveUserIntoRoomRecord(uid, roomUid);
        return roomDto;
    }

    @Override
    public void userOutRoom(Long uid) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        UsersDTO usersDTO = usersManager.getUser(uid);
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        // 国庆活动房间停留 --代码已废弃
        //closeRoomTime(uid);
        // 删除用户在房间中的缓存
        redisManager.hdel(RedisKey.user_in_room.getKey(), String.valueOf(uid));
//        UsersDO usersDO = new UsersDO();
//        BeanUtils.copyProperties(usersDTO, usersDO);
//        return usersDO;
    }

    @Override
    public RoomUserinDTO getRoomByUid(Long uid) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        String room = redisManager.hget(RedisKey.user_in_room.getKey(), String.valueOf(uid));
        if (StringUtils.isBlank(room)) {
            return new RoomUserinDTO();
        }
        return gson.fromJson(room, RoomUserinDTO.class);
    }

    @Async
    void saveUserIntoRoomRecord(Long uid, Long roomUid) {
        try {
            UserIntoRoomRecordDO recordDo = new UserIntoRoomRecordDO();
            recordDo.setUid(uid);
            recordDo.setRoomUid(roomUid);
            recordDo.setType("0");
            recordDo.setCreateDate(new Date());
            userIntoRoomLogDao.save(recordDo);
        } catch (Exception e) {
            logger.error("[ 用户进入房间记录 ]保存异常：", e);
        }
    }

    /**
     * 获取进入非自己房间的时间
     *
     * @param uid
     * @param roomUid
     */
    /*public void openRoomTime(Long uid, Long roomUid) {
        if (uid != roomUid) {
            String taskDay = DateTimeUtils.getTodayStr();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("roomUid", roomUid.toString());
            Long startTime = System.currentTimeMillis();
            map.put("openStartTime", startTime.toString());
            redisManager.hset(RedisKey.daily_task.name(), uid + "_open_room_" + taskDay, gson.toJson(map));
        }
    }*/

    /**
     * 获取退出非自己房间的时间
     *
     * @param uid
     */
    /*public void closeRoomTime(Long uid) {
        String taskDay = DateTimeUtils.getTodayStr();
        Long endTime = System.currentTimeMillis();
        String rmStr = redisManager.hget(RedisKey.daily_task.name(), uid + "_open_room_" + taskDay);
        if (StringUtils.isNotBlank(rmStr)) {
            Map<String, Object> map = gson.fromJson(rmStr, Map.class);
            Long oldUid = Long.parseLong(map.get("roomUid").toString());
            if (uid != oldUid) {
                Long startTime = Long.parseLong(map.get("openStartTime").toString());
                Long times = (endTime - startTime) / 60000;
                List<Map<String, Object>> roomStaylist = new ArrayList<Map<String, Object>>();
                String roomStayListStr = redisManager.hget(RedisKey.daily_task.name(), uid + "_" + taskDay + "_" + "room_stay");
                if (StringUtils.isNotBlank(roomStayListStr)) {
                    roomStaylist = gson.fromJson(roomStayListStr, new TypeToken<List<Map<String, Object>>>() {
                    }.getType());
                }
                Integer count = 0;
                for (Map<String, Object> m : roomStaylist) {
                    Long roomUid = Long.parseLong(m.get("roomUid").toString());
                    Long mTimes = Long.parseLong(m.get("times").toString());
                    times = mTimes + times;
                    if (oldUid == roomUid) {
                        m.put("times", times.toString());
                        count++;
                    }
                }
                if (count == 0) {
                    Map<String, Object> roomStay = new HashMap<String, Object>();
                    roomStay.put("roomUid", map.get("roomUid").toString());
                    roomStay.put("times", times.toString());
                    roomStaylist.add(roomStay);
                }
                redisManager.hset(RedisKey.daily_task.name(), uid + "_" + taskDay + "_" + "room_stay", gson.toJson(roomStaylist));
            }

        }

    }*/

}
