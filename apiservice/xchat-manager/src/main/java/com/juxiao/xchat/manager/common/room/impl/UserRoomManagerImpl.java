package com.juxiao.xchat.manager.common.room.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.room.dto.RoomUserinDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.room.UserRoomManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @class: UserRoomManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/13
 */
@Service
public class UserRoomManagerImpl implements UserRoomManager {

    @Autowired
    private Gson gson;
    @Autowired
    private RedisManager redisManager;

    @Override
    public void saveUserInRoom(Long uid, RoomUserinDTO userinDto) {
        redisManager.hset(RedisKey.user_in_room.getKey(), String.valueOf(uid), gson.toJson(userinDto));
    }

    @Override
    public RoomUserinDTO getUserInRoom(Long uid) {
        String userinStr = redisManager.hget(RedisKey.user_in_room.getKey(), String.valueOf(uid));
        if (!StringUtils.isBlank(userinStr)) {
            return gson.fromJson(userinStr, RoomUserinDTO.class);
        }

        //bug，运行一段时间以做数据修复
        userinStr = redisManager.hget(String.valueOf(uid), RedisKey.user_in_room.getKey());
        if (StringUtils.isBlank(userinStr)) {
            return null;
        }

        RoomUserinDTO userinDto = gson.fromJson(userinStr, RoomUserinDTO.class);
        if (userinDto != null) {
            //bug，运行一段时间以做数据修复
            this.saveUserInRoom(uid, userinDto);
            redisManager.hdel(String.valueOf(uid), RedisKey.user_in_room.getKey());
        }
        return userinDto;
    }
}
