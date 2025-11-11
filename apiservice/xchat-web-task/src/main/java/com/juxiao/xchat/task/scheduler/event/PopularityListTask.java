package com.juxiao.xchat.task.scheduler.event;

import com.juxiao.xchat.service.task.event.TaskPopularityListService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 人气榜单定时任务
 */
@Component
public class PopularityListTask {
    @Resource
    TaskPopularityListService taskPopularityListService;

    /**
     * 每30秒缓存排名前20的女神/男神榜
     */
    @Scheduled(cron = "*/30 * * * * ?")
    public void cacheTop20List() {
        taskPopularityListService.top20List();
    }

    /**
     * 每周一0点缓存一次上周人气榜单
     */
    // TODO 每周一一次
    // @Scheduled(cron = "0 00 00 ? * MON")
    @Scheduled(cron = "*/30 * * * * ?")
    public void cacheLastWeekRank() {
        taskPopularityListService.lastWeekRank();
    }
}
