package com.juxiao.xchat.task.scheduler.user;

import com.juxiao.xchat.service.task.user.UserPurseDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author chris
 * @Title:
 * @date 2019-05-14
 * @time 09:48
 */
@Component
public class UserPurseDailyTask {
    @Autowired
    private UserPurseDailyService dailyService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void saveDailyPurse() {
        dailyService.saveDailyPurse();
    }
}
