package com.juxiao.xchat.service.task.event;

/**
 * 人气榜单
 */
public interface TaskPopularityListService {
    /**
     * 每30秒缓存排名前20的女神/男神榜
     */
    void top20List();

    /**
     * 每周一0点缓存一次上周人气榜单
     */
    void lastWeekRank();
}
