package com.erban.main.service;

import com.erban.main.model.Room;
import com.erban.main.service.room.RoomService;
import com.erban.main.vo.RunningRoomVo;
import com.google.gson.Gson;
import com.xchat.common.constant.Constant;
import com.xchat.common.netease.neteaseacc.result.RoomRet;
import com.xchat.common.netease.neteaseacc.result.RoomUserListRet;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.service.JedisService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by liuguofu on 2017/10/5.
 */
@Service
public class CleanExceptionRoomService {
    private static final Logger logger = LoggerFactory.getLogger(CleanExceptionRoomService.class);
    @Autowired
    private JedisService jedisService;
    @Autowired
    private ErBanNetEaseService erBanNetEaseService;
    @Autowired
    private RoomService roomService;

    private Gson gson=new Gson();

    public void cleanExceptionRoom() throws Exception{

        Map<String, String> map = jedisService.hgetAll(RedisKey.room_running.getKey());
        if (map != null && map.size() > 0) {
            Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                String key = entry.getKey();
                if (StringUtils.isNotEmpty(key)) {

                    String roomVoStr = jedisService.hget(RedisKey.room.getKey(), key.toString());
                    RunningRoomVo  roomRunning = gson.fromJson(roomVoStr, RunningRoomVo.class);
                    RoomUserListRet roomUserListRet = erBanNetEaseService
                            .getRoomMemberListInfo(roomRunning.getRoomId(), roomRunning.getUid());

                    Long uid=roomRunning.getUid();
                    if(roomUserListRet==null){
                        logger.info("房间关闭,清除uid="+uid);
                        deleteRunningByUid(roomRunning.getUid());
                        roomService.closeRoom(roomRunning.getUid());
                        continue;
                    }
                    logger.info("处理房间,uid="+uid+"................");
                    if( roomUserListRet.getDesc()==null){
                        continue;
                    }
                    for (Map.Entry<String, List<Map<String, Object>>> roomUserListRetEntry : roomUserListRet.getDesc()
                            .entrySet()) {
                        for (Map.Entry<String, Object> entry2 : roomUserListRetEntry.getValue().get(0).entrySet()) {
                            if ("onlineStat".equals(entry2.getKey())) {
                                Room room=roomService.getRoomByUid(roomRunning.getUid());
                                boolean onlineStat = (boolean) entry2.getValue();
                                if (onlineStat) {
                                    room.setIsExceptionClose(false);
                                } else {
                                    if(room.getType()== Constant.RoomType.radio||room.getType()==Constant.RoomType.auct){
                                        logger.info("房主不在线，并且房间类型为竞拍或者轻聊房,清理uid="+uid);
                                        roomService.closeRoom(room.getUid());
                                        room.setValid(false);
                                        room.setOperatorStatus(Constant.RoomOptStatus.out);
                                        room.setIsExceptionClose(false);
                                        deleteRunningByUid(uid);
                                    }else{
                                       int num= getOnlineNum(uid,room.getRoomId());
                                        if(num==0){
                                            roomService.closeRoom(room.getUid());
                                            room.setValid(false);
                                            room.setOperatorStatus(Constant.RoomOptStatus.out);
                                            room.setIsExceptionClose(false);
                                            deleteRunningByUid(uid);
                                        }
                                        room.setOperatorStatus(Constant.RoomOptStatus.out);
                                    }
                                }
//                                roomService.updateRunningRoom(room);
                            }
                        }
                    }
                }
            }
        }
    }
    private void deleteRunningByUid(Long uid){
        jedisService.hdelete(RedisKey.room_running.getKey(),uid.toString(),"");
    }
    private Integer getOnlineNum(Long uid, Long roomId) throws Exception{
        RoomRet roomRet = erBanNetEaseService.getRoomMessage(roomId);
        Map<String, Object> chatroom = roomRet.getChatroom();
        Integer personNum =0;
        for (Map.Entry<String, Object> entry : chatroom.entrySet()) {
            if ("onlineusercount".equals(entry.getKey())) {
                Double num = (Double) entry.getValue();
                 num.intValue();
                logger.info("当前房间为：uid:" + uid + ",实际人数为：" + personNum);
                if (personNum == null || personNum == 0) {
                    personNum = 0;
                }

            }
        }
        return personNum;
    }
}

