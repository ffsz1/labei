package com.juxiao.xchat.task.scheduler.event;

import com.juxiao.xchat.service.task.event.WeekStarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author chris
 * @Title: 周星榜定时任务
 * @date 2019-05-20 16:55
 */
@Component
public class WeekStarTask {
    @Autowired
    private WeekStarService weekStarService;

    /**
     * 周星礼物,周星榜排名
     */
    // @Scheduled(cron = "0 0 0 ? * MON")
    @Scheduled(cron = "0 00 10 ? * MON")
    public void weekStartGift(){
        weekStarService.weekStartGift();
    }
}
