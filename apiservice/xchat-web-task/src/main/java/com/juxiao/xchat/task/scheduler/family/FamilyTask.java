package com.juxiao.xchat.task.scheduler.family;

import com.juxiao.xchat.service.task.family.FamilyTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author laizhilong
 * @Title:
 * @Package com.juxiao.xchat.task.scheduler.family
 * @date 2018/9/2
 * @time 17:50
 */
@Component
public class FamilyTask {

//    @Autowired
//    private FamilyTaskService familyTaskService;
//
//
//
//    /**
//     * 每天执行一次
//     * 定时刷新是否退出家族
//     */
//    @Scheduled(cron = "0 0 0 1/1 * ?")
//    public void refreshStatus() {
//        familyTaskService.refreshStatus();
//    }
//
//    /**
//     * 每周一清除家族威望
//     */
//    @Scheduled(cron = "0 0 0 ? * MON")
//    public void cleanTeamGitfRecord(){
//        familyTaskService.countCleanPrestige();
//    }
}
