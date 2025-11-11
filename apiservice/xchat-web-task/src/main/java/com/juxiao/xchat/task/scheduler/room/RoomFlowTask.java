package com.juxiao.xchat.task.scheduler.room;

import com.juxiao.xchat.service.task.room.RoomTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RoomFlowTask {
    @Autowired
    private RoomTaskService roomTaskService;

    @Scheduled(cron = "0 0 6 ? * MON")
    public void refreshWeekRoomFlowCache() {
        roomTaskService.refreshWeekRoomFlowCache();
    }

}
