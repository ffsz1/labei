package com.juxiao.xchat.task.scheduler.room;

import com.juxiao.xchat.service.task.room.RoomGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author chris
 * @Title:
 * @date 2019-05-13
 * @time 11:00
 */
@Component
public class RoomGameTask {

    @Autowired
    private RoomGameService roomGameService;

    /**
     * 检查tomcat是否存活
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void checkGameStatus() {
        roomGameService.checkGameStatus();
    }
}
