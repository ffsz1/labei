package com.juxiao.xchat.service.task.guild;

public interface GuildDailyTurnoverReportService {

    /**
     * 每日第一分钟更新前一天的流水
     */
    void updateYesterdayTurnovers();

    /**
     * 每隔几分钟更新当天的流水
     */
    void updateTodayTurnovers();
}
