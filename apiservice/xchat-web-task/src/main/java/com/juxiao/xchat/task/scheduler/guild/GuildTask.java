package com.juxiao.xchat.task.scheduler.guild;

import com.juxiao.xchat.service.task.guild.GuildDailyTurnoverReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 描述：公会相关的
 *
 * @创建时间： 2020/10/16 16:55
 * @作者： carl
 */
@Component
public class GuildTask {

    @Autowired
    private GuildDailyTurnoverReportService guildDailyTurnoverReportService;

    /**
     * 每日第一分钟更新前一天的公会相关流水
     */
    @Scheduled(cron = "0 1 0 * * ?")
    public void updateYesterdayTurnovers() {
        guildDailyTurnoverReportService.updateYesterdayTurnovers();
    }

    /**
     * 每隔5分钟更新当天的公会相关流水
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void updateTodayTurnovers() {
        guildDailyTurnoverReportService.updateTodayTurnovers();
    }
}
