package com.juxiao.xchat.service.task.item;

public interface GiftTaskService {

    void saveOneDayHome();

    void saveLastDayHome();

    void refreshOneWeek();

    void refreshAll();

    void refreshRoomRank();

    void saveGiftRankToCache();

    void refreshLast();

    void savePeriodData();

    void retryGiftQueue();

    void retryGiftPropQueue();

    void retryBigGiftQueue();

    void retryGiftFullQueue();

    void retryCallQueue();

//    /**
//     * 尝试消费神秘礼物队列
//     */
//    void retryMysticGiftQueue();
}
