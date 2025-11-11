package com.erban.web.task;

import com.erban.main.service.room.RoomFlowServie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RoomFlowTask {

    private static final Logger logger = LoggerFactory.getLogger(RoomFlowTask.class);
    @Autowired
    private RoomFlowServie roomFlowServie;

    @Scheduled(cron = "0 0 6 ? * MON")
    public void refreshWeekRoomFlowCache() {
        // 6点种更新房间周流水
        roomFlowServie.refreshWeekRoomFlowCache();
        logger.info("定时任务执行========更新房间周流水 key [room_flow_proportion]");
    }
}
