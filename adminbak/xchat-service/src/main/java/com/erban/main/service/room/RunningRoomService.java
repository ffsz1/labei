package com.erban.main.service.room;

import com.erban.main.service.base.BaseService;
import com.erban.main.service.room.RoomService;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.RunningRoomVo;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liuguofu on 2017/10/4.
 */
@Service
public class RunningRoomService extends BaseService {
    @Autowired
    private RoomService roomService;
    @Autowired
    private UsersService usersService;


    public RunningRoomVo getRunningRoomVoFromCache(Long uid) {
        String roomVoStr = jedisService.hget(RedisKey.room_running.getKey(), uid.toString());
        return gson.fromJson(roomVoStr, RunningRoomVo.class);
    }
}
