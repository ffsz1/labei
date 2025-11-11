package com.erban.main.service.user;

import com.beust.jcommander.internal.Maps;
import com.erban.main.model.NobleUsers;
import com.erban.main.model.Room;
import com.erban.main.model.Users;
import com.erban.main.service.noble.NobleUserInRoomService;
import com.erban.main.service.noble.NobleUsersService;
import com.erban.main.service.room.RoomService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.RoomVo;
import com.erban.main.vo.noble.NobleUserVo;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by liuguofu on 2017/10/17.
 */
@Service
public class UserInRoomService {
    @Autowired
    private JedisService jedisService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private NobleUsersService nobleUsersService;
    @Autowired
    private NobleUserInRoomService nobleUserInRoomService;


    private Gson gson=new Gson();

    public BusiResult userIntoRoom(Long uid, Long roomUid) {
        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
        RoomVo roomVo=roomService.getRoomVoByUid(roomUid);
        if(roomVo==null){
           return busiResult;
        }

        nobleUserInRoomService.saveNobleUserInRoomCache(uid,roomUid);
        saveUserInRoomCache(uid,roomVo);
        busiResult.setData(roomVo);
        return busiResult;
    }

    private void saveUserInRoomCache(Long uid,RoomVo roomVo){
        jedisService.hwrite(RedisKey.user_in_room.getKey(),uid.toString(),gson.toJson(roomVo));
    }


    private void deleteBugUserInRoomCache(Long uid){
        jedisService.hdel(uid.toString(),RedisKey.user_in_room.getKey());
    }

    private void removeUserInRoomCache(Long uid){
        jedisService.hdel(RedisKey.user_in_room.getKey(),uid.toString());
    }


    private RoomVo getUserInRoomInfoCache(Long uid){
        String roomVoStr=jedisService.hget(RedisKey.user_in_room.getKey(),uid.toString());
        if(StringUtils.isEmpty(roomVoStr)){//bug，运行一段时间以做数据修复
            roomVoStr=jedisService.hget(uid.toString(),RedisKey.user_in_room.getKey());
            if(StringUtils.isEmpty(roomVoStr)){
                return null;
            }else{//bug，运行一段时间以做数据修复
                saveUserInRoomCache(uid,gson.fromJson(roomVoStr,RoomVo.class));
                deleteBugUserInRoomCache(uid);
            }
        }
        RoomVo roomVo=gson.fromJson(roomVoStr,RoomVo.class);
        return roomVo;
    }

    public BusiResult userOutRoom(Long uid) {
        removeUserInRoomCache(uid);
        return new BusiResult(BusiStatus.SUCCESS);
    }

    public BusiResult<RoomVo> getUserInRoomInfo(Long uid) {
        RoomVo roomVo=getUserInRoomInfoCache(uid);
        if(roomVo==null){
            roomVo=new RoomVo();
        }
        BusiResult<RoomVo> busiResult=new BusiResult<RoomVo>(BusiStatus.SUCCESS);
        busiResult.setData(roomVo);
        return busiResult;
    }
    public Map<Long,RoomVo> getUserInRoomBatch(String[] uidsArray){
        Map<Long,RoomVo> roomVoMap=getUserInRoomMapBatchCache(uidsArray);
//        for(int i=0;i<uidsArray.length;i++){
//            Long uid=Long.valueOf(uidsArray[i]);
//            RoomVo roomVo=roomVoMap.get(uid);
//            if(roomVo==null){
//                roomVo=roomService.getRoomVoByUid(uid);
//                if(roomVo!=null){
//                    roomVoMap.put(uid,roomVo);
//                }
//            }
//        }
        return roomVoMap;

    }

    private Map<Long,RoomVo> getUserInRoomMapBatchCache(String[] uidsArray){
//        List<String> roomListStr = jedisService.hmread(RedisKey.user_in_room.getKey(), uidsArray);
//        if (CollectionUtils.isEmpty(roomListStr)) {
//            return Maps.newHashMap();
//        }
        Map<Long,RoomVo> roomVoMap= Maps.newHashMap();
        for (int i=0;i<uidsArray.length;i++) {
            Long uid=Long.valueOf(uidsArray[i]);
            String roomStr=jedisService.hget(RedisKey.user_in_room.getKey(),uidsArray[i]);
            if (StringUtils.isEmpty(roomStr)) {
                continue;
            }
            RoomVo roomVo = gson.fromJson(roomStr, RoomVo.class);
            roomVoMap.put(uid,roomVo);
        }
        return roomVoMap;
    }
}

