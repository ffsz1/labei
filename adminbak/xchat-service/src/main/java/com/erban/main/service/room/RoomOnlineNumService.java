package com.erban.main.service.room;

import com.erban.main.model.*;
import com.erban.main.service.base.CacheBaseService;
import com.xchat.common.redis.RedisKey;
import org.springframework.stereotype.Service;

@Service
public class RoomOnlineNumService extends CacheBaseService<RoomOnlineNum, RoomOnlineNum> {

    @Override
    public RoomOnlineNum getOneByJedisId(String jedisId) {
        return getOne(RedisKey.room_online_num.getKey(), jedisId, "select * from room_online_num where uid = ? ", jedisId);
    }

    @Override
    public RoomOnlineNum entityToCache(RoomOnlineNum entity) {
        return entity;
    }

    public Integer getNeedAddNum(String uid, Integer onlineNum) {
        try{
            RoomOnlineNum roomOnlineNum = getOneByJedisId(uid);
            if(roomOnlineNum==null){
                return 0;
            }
            if(roomOnlineNum.getType().intValue()==1){
                return roomOnlineNum.getFactor();
            }
            if(roomOnlineNum.getType().intValue()==2 && onlineNum!=null){
                return roomOnlineNum.getFactor()*onlineNum/100;
            }
            return 0;
        }catch (Exception e){
            logger.error("获取自定义人数错误"+e.getMessage());
            return 0;
        }
    }

}
