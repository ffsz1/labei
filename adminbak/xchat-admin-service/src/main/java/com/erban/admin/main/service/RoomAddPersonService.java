package com.erban.admin.main.service;

import com.beust.jcommander.internal.Lists;
import com.erban.main.model.Room;
import com.erban.main.model.Users;
import com.erban.main.mybatismapper.RoomMapper;
import com.erban.main.service.ErBanNetEaseService;
import com.erban.main.service.user.UsersService;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@Service
public class RoomAddPersonService {
    @Autowired
    private UsersService usersService;
    @Autowired
    private ErBanNetEaseService erBanNetEaseService;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private JedisService jedisService;
    private Gson gson = new Gson();

    public void addPerson(Long erbanNo, Long personNum, Byte gender) throws Exception {
        Users users = usersService.getUsersByErBanNo(erbanNo);
        Room room = roomMapper.selectByPrimaryKey(users.getUid());
        List<String> list = null;
        List<String> robotList = Lists.newArrayList();
        String key = null;
        if (gender.equals(Constant.SexRobotType.man)) {
            key = RedisKey.add_man.getKey();
        } else {
            key = RedisKey.add_woman.getKey();
        }
        Map<String, String> map = jedisService.hgetAllBykey(key);
        if (map != null && map.size() > 0) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                Type type = new TypeToken<List<String>>() {
                }.getType();
                list = gson.fromJson(entry.getValue(), type);
                int startNo = Integer.parseInt(entry.getKey());
                int endNo = startNo + personNum.intValue();
                jedisService.hdelete(key, entry.getKey().toString(), null);
                if (endNo > list.size() - 1) {
                    robotList = list.subList(0, personNum.intValue());
                    jedisService.hwrite(key, personNum.toString(), entry.getValue());
                } else {
                    robotList = list.subList(startNo, endNo);
                    jedisService.hwrite(key, String.valueOf(endNo), entry.getValue());
                }
                String listStr = gson.toJson(robotList);
                erBanNetEaseService.addRobotToRoom(room.getRoomId(), listStr);
            }
        } else {
            list = Lists.newArrayList();
            List<Users> usersList = null;
            if (gender.equals(Constant.SexRobotType.man)) {
                usersList = usersService.getUsersByRobot(Constant.SexRobotType.man);
            } else {
                usersList = usersService.getUsersByRobot(Constant.SexRobotType.woMan);
            }
            for (Users user : usersList) {
                list.add(user.getUid().toString());
            }
            String listStr = gson.toJson(list);
            robotList = list.subList(0, personNum.intValue());
            erBanNetEaseService.addRobotToRoom(room.getRoomId(), gson.toJson(robotList));
            jedisService.hwrite(key, personNum.toString(), listStr);
        }
    }
}
