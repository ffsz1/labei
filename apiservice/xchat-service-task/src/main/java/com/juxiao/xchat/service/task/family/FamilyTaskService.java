package com.juxiao.xchat.service.task.family;

/**
 * @author laizhilong
 * @Title:
 * @Package com.juxiao.xchat.service.task.family
 * @date 2018/9/2
 * @time 15:56
 */
public interface FamilyTaskService {

    /**
     * 定时刷新是否退出家族
     */
    void refreshStatus();

    /**
     * 统计并清除威望
     */
    void countCleanPrestige();
}
