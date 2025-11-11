package com.juxiao.xchat.service.task.room;

public interface RoomTaskService {
    void cacheTagRoom();

    void cacheTagIndexRooms();

    void cacheHomeRooms();

    void clearInvalidRoom();

    void refreshPzPerson();

    void refreshShpzPerson();

    void refreshPtPerson();

    void refreshOnlineNum();

    void refreshRobot();

    void refreshWeekRoomFlowCache();

    /**
     * 缓存推荐房间列表
     */
    void cacheRecommendRoom();

    /**
     * 缓存首页热门房间列表
     */
    void cacheHotRoom();

    /**
     * 没30分钟刷新推荐房间列表
     */
    void refreshRoomRecommend();
}
