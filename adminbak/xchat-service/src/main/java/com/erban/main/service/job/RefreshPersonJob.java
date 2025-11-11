package com.erban.main.service.job;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.erban.main.service.HomeService;
import com.erban.main.service.room.RoomCleanService;
import com.erban.main.vo.RunningRoomVo;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.erban.main.service.ErBanNetEaseService;
import com.erban.main.service.room.RoomService;
import com.erban.main.vo.RoomVo;
import com.google.gson.Gson;
import com.xchat.common.netease.neteaseacc.result.RoomRet;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.service.JedisService;

/**
 * @author yanghaoyu
 * <p>
 * 定时刷新房间人数
 */
public class RefreshPersonJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(RefreshPersonJob.class);
    @Autowired
    private JedisService jedisService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private HomeService homeService;
    @Autowired
    private ErBanNetEaseService erBanNetEaseService;
    @Autowired
    private RoomCleanService roomCleanService;

    private Gson gson = new Gson();

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        logger.info("定时任务：刷新人数正在执行。");
        try {
            List<RoomVo> roomList = roomService.getHomeRunningRoomList();
            for (RoomVo roomVo : roomList) {
                if (roomVo.getValid()) {
                    RoomRet roomRet = erBanNetEaseService.getRoomMessage(roomVo.getRoomId());
                    Map<String, Object> chatroom = roomRet.getChatroom();
                    for (Entry<String, Object> entry : chatroom.entrySet()) {
                        if ("onlineusercount".equals(entry.getKey())) {
                            Double num = (Double) entry.getValue();
                            Integer personNum = num.intValue();

                            RunningRoomVo runningRoomVo = new RunningRoomVo();
                            if (personNum > 0) {
                                // 牌照房需要过滤机器人
                                if (roomVo.getIsPermitRoom() == 1) {
                                    if(roomCleanService.hasRealUserInRoom(roomVo.getRoomId())){
                                        runningRoomVo.setCount(0);
                                    }else {
                                        // 开房状态时，每扫描一次若在线人数为0，计数器加1
                                        runningRoomVo.setCount(roomVo.getCount() + 1);
                                    }
                                } else {
                                    runningRoomVo.setCount(0);
                                }
                            } else {
                                // 开房状态时，每扫描一次若在线人数为0，计数器加1
                                runningRoomVo.setCount(roomVo.getCount() + 1);
                            }
                            runningRoomVo.setRoomId(roomVo.getRoomId());
                            runningRoomVo.setUid(roomVo.getUid());
                            runningRoomVo.setOnlineNum(personNum);

                            String runningRoomStr = gson.toJson(runningRoomVo);
                            jedisService.hwrite(RedisKey.room_running.getKey(), roomVo.getUid().toString(), runningRoomStr);
                            roomService.updateRoomOnlineNumMysql(roomVo.getUid(),personNum);
                        }
                    }
                }
            }
            //同时刷新首页数据
//            homeService.refreshHomData();
        } catch (Exception e) {
            logger.error("定时任务：查询人数出错", e.getMessage());
        }
    }

    /* 查询出人数做的随机算法 */
    private Integer selectPersonNumMethod(Integer personNum) {
        if (personNum == 0) {
            return 1;
        }
        personNum = personNum * 10;
        Random random = new Random();
        if (personNum > 0 && personNum < 50) {
            personNum = personNum - (random.nextInt(2) + 1);
        } else if (personNum >= 50 && personNum < 100) {
            personNum = personNum - (random.nextInt(4) + 1);
        } else if (personNum >= 100 && personNum < 500) {
            personNum = personNum - (random.nextInt(9) + 1);
        } else if (personNum >= 500 && personNum < 1000) {
            personNum = personNum - (random.nextInt(19) + 1);
        } else if (personNum >= 1000 && personNum < 10000) {
            personNum = personNum - (random.nextInt(29) + 1);
        } else if (personNum >= 10000) {
            personNum = personNum - (random.nextInt(39) + 1);
        }
        return personNum;
    }

}
