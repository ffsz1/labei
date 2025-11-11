package com.erban.main.service.job;

import com.erban.main.model.Room;
import com.erban.main.service.ErBanNetEaseService;
import com.erban.main.service.room.RoomHotService;
import com.erban.main.service.room.RoomService;
import com.erban.main.vo.RoomVo;
import com.google.gson.Gson;
import com.xchat.common.netease.neteaseacc.result.RoomUserListRet;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.service.JedisService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.Map.Entry;

/**
 * 定时检查房间是否异常关闭任务
 *
 * @author yanghaoyu
 */
public class CheckRoomExceptionJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(CheckRoomExceptionJob.class);
    @Autowired
    private RoomService roomService;

    @Autowired
    private ErBanNetEaseService erBanNetEaseService;

    @Autowired
    private JedisService jedisService;

    @Autowired
    private RoomHotService roomHotService;

    private Gson gson = new Gson();

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        try {
            logger.info("正在执行定时任务：查询异常退出房间数据");
            /* 从缓存中查询正在开播的房间id */
            List<Long> keepAliveRoomUids = roomHotService.getVipRoomListByCheckRoom();
            Map<String, String> map = jedisService.hgetAll(RedisKey.room_running.getKey());
            List<RoomVo> roomList = new ArrayList<>();
            if (map != null && map.size() > 0) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = it.next();
                    String key = entry.getKey();
                    if (StringUtils.isNotEmpty(key)) {
                        String roomVoStr = jedisService.hget(RedisKey.room.getKey(), key.toString());
                        RoomVo roomVo = gson.fromJson(roomVoStr, RoomVo.class);
                        roomList.add(roomVo);
                    }
                }
            } else {
                roomList = roomService.getHomeRunningRoomList();
            }
            for (RoomVo roomVo : roomList) {
                /* 是否是开播状态 */
                if (roomVo!=null && roomVo.getValid()!= null && roomVo.getValid()) {
                    /* 是否是断开连接状态 */
                    /* 查询房间信息 */
                    if (!CollectionUtils.isEmpty(keepAliveRoomUids)) {
                        boolean flag = keepAliveRoomUids.contains(roomVo.getUid());
                        if (flag) {
                            logger.info("当前房间是vip房间，不进行异常关闭检查.uid=" + roomVo.getUid());
                            continue;
                        }
                    }
                    logger.info("当前房间状态是开房，进行是否断开连接判断，uid=" + roomVo.getUid());
                    if (roomVo.getIsExceptionClose()) {
                        /* 断开连接时间是否超过5分钟 */
                        if ((new Date().getTime() - roomVo.getExceptionCloseTime().getTime()) > 60000*10) {
                            logger.info("当前房间已经断开连接超过10分钟，判断是否重连:uid=" + roomVo.getUid());
                            /* 判断是否重连 */
                            /* 查询房间所有成员信息 */
                            Room room = new Room();
                            room.setUid(roomVo.getUid());
                            //查询云信中当前房间的状态
                            RoomUserListRet roomUserListRet = erBanNetEaseService
                                    .getRoomMemberListInfo(roomVo.getRoomId(), roomVo.getUid());
                            if(roomUserListRet==null){
                                roomService.closeRoom(roomVo.getUid());
                                continue;
                            }
                            for (Entry<String, List<Map<String, Object>>> entry : roomUserListRet.getDesc()
                                    .entrySet()) {
                                for (Entry<String, Object> entry2 : entry.getValue().get(0).entrySet()) {
                                    if ("onlineStat".equals(entry2.getKey())) {
                                        boolean onlineStat = (boolean) entry2.getValue();
                                        /* 是否在线，在线为true */
                                        if (onlineStat) {
                                            logger.info("当前房间已经重连,uid:" + roomVo.getUid());
                                            /* 重新连接上 ，将异常信息改为false */
                                            room.setIsExceptionClose(false);
                                        } else {
                                            logger.info("当前房间未重连,正在关闭房间。uid:" + roomVo.getUid());
                                            /* 未重新链接，关闭房间 */
                                            roomService.closeRoom(roomVo.getUid());
                                            room.setValid(false);
                                            room.setIsExceptionClose(false);
                                        }
                                        roomService.updateRunningRoom(room);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("定时任务：查询异常房间失败。", e);
        }
    }
}
