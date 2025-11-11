package com.erban.main.service.noble;

import com.erban.main.model.NobleUsers;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.UserVo;
import com.erban.main.vo.noble.NobleUserVo;
import com.google.common.collect.Lists;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.service.JedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NobleUserInRoomService extends BaseService{
    private static final Logger logger = LoggerFactory.getLogger(NobleUserInRoomService.class);

    @Autowired
    private JedisService jedisService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private NobleUsersService nobleUsersService;

    public List<NobleUserVo> getNobleUserInRoomList(Long roomUid) throws Exception{
        Map<String,String> map = jedisService.hgetAll(RedisKey.noble_user_in_room.getKey(roomUid.toString()));
        if(map == null || map.size() == 0){
            return Lists.newArrayList();
        }
        Set<String> keySet = map.keySet();
        List<NobleUserVo> nobleUserVoList = new ArrayList<>(keySet.size());
        for(String key : keySet){
            NobleUserVo nobleUserVo = gson.fromJson(map.get(key),NobleUserVo.class);
            UserVo userVo = usersService.getUserVoByUid(Long.valueOf(key));
            NobleUsers nobleUsers = nobleUsersService.getNobleUser(Long.valueOf(key));
            if(nobleUsers != null){
                nobleUserVo.setAvatar(userVo.getAvatar());
                nobleUserVo.setNick(userVo.getNick());
                nobleUserVo.setGender(userVo.getGender());
                nobleUserVo.setNobleUsers(nobleUsers);
                nobleUserVoList.add(nobleUserVo);
            }
        }
        Collections.sort(nobleUserVoList);
        if(nobleUserVoList.size() > 60){
            return nobleUserVoList.subList(0,60);
        }else{
            return nobleUserVoList;
        }
    }

    public void saveNobleUserInRoomCache(Long uid,Long roomUid){
        NobleUsers nobleUsers = nobleUsersService.getNobleUser(uid);
        if(nobleUsers != null){
            Date date = new Date();
            NobleUserVo nobleUserVo = new NobleUserVo();
            nobleUserVo.setUid(uid);
            nobleUserVo.setEnterTime(date.getTime());
            jedisService.hset(RedisKey.noble_user_in_room.getKey(roomUid.toString()),uid.toString(),gson.toJson(nobleUserVo));
            logger.info("saveNobleUserInRoomCache has done,uid="+uid+",roomUid="+roomUid);
        }
    }

    public void removeNobleUserInRoomCache(Long uid,Long roomUid){
        jedisService.hdel(RedisKey.noble_user_in_room.getKey(roomUid.toString()),uid.toString());
    }
}
