package com.erban.admin.main.service;

import com.erban.main.model.Room;
import com.erban.main.model.Users;
import com.erban.main.service.room.RoomHotService;
import com.erban.main.service.room.RoomService;
import com.erban.main.service.user.UsersService;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.GetTimeUtils;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
public class HotRoomService {

    @Autowired
    private UsersService usersService;

    @Autowired
    private RoomHotService roomHotService;

    @Autowired
    private JedisService jedisService;

    @Autowired
    private RoomService roomService;

    private Gson gson = new Gson();

    public void addHotRoom(Long erbanNo, Integer startTime, Integer endTime, Integer seqNo) throws ParseException {
        Users users = usersService.getUsersByErBanNo(erbanNo);
        Date startDate = GetTimeUtils.getTimesnight(startTime);
        Date endDate = GetTimeUtils.getTimesnight(endTime);
        roomHotService.addHotRoom(users.getUid(), startDate, endDate, seqNo);
    }

    public void addVipRoom(Long erbanNo) {
        Users users = usersService.getUsersByErBanNo(erbanNo);
        Room room = roomService.getRoomByUid(users.getUid());
        String roomStr = gson.toJson(room);
        jedisService.hwrite(RedisKey.room_vip.getKey(), room.getUid().toString(), roomStr);
    }
}
