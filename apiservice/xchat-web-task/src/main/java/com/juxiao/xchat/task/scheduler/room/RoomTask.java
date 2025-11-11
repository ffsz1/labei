package com.juxiao.xchat.task.scheduler.room;

import com.juxiao.xchat.service.task.room.RoomTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 房间定时任务
 */
@Component
public class RoomTask {
    @Autowired
    private RoomTaskService roomTaskService;

    /**
     * 每30秒缓存标签数据
     */
    @Scheduled(cron = "*/30 * * ? * *")
    public void cacheTagRoom() {
        roomTaskService.cacheTagRoom();
    }

    /**
     * 每30秒缓存标签列表的数据
     */
    @Scheduled(cron = "*/30 * * * * ?")
    public void cacheTagIndexRooms() {
        roomTaskService.cacheTagIndexRooms();
    }

    /**
     * 每1分钟缓存首页房间数据
     */
    @Scheduled(cron = "*/30 * * * * ?")
    public void cacheHomeRooms() {
        roomTaskService.cacheHomeRooms();
    }

    // /**
    //  * 清除僵尸房，分为两种情况：
    //  * 1、普通房间，没人在线时直接关闭；
    //  * 2、牌照房间，没人在线或者只有机器人在房间时，不在页面上显示
    //  */
    // @Scheduled(cron = "0 */5 * ? * *")
    // public void clearInvalidRoom() {
    //     long startTime = System.currentTimeMillis();
    //     logger.info("[ 定时任务 ] 清除僵尸房 start");
    //     roomTaskService.clearInvalidRoom();
    //     logger.info("[ 定时任务 ] 清除僵尸房 finish，耗时:>{}", (System.currentTimeMillis() - startTime));
    // }

    /**
     * 每天凌晨零点零一分缓存推荐房间列表
     */
    @Scheduled(cron = "0 1 0 * * ?")
    public void cacheRecommendRoom() {
        roomTaskService.cacheRecommendRoom();
    }

    /**
     * 每10分钟刷新一次首页热门房间列表
     */
    // @Scheduled(cron = "0 0/10 * * * ?")
    // public void cacheHotRoom() {
    //     roomTaskService.cacheHotRoom();
    // }

    // /**
    //  * 刷新牌照房间人数
    //  */
    // @Scheduled(cron = "0 */1 * ? * *")
    // public void refreshPzPerson() {
    //     long startTime = System.currentTimeMillis();
    //     logger.info("[ 定时任务 ] 刷新牌照房间人数 start");
    //     roomTaskService.refreshPzPerson();
    //     logger.info("[ 定时任务 ] 刷新牌照房间人数 finish，耗时:>{}", (System.currentTimeMillis() - startTime));
    // }

    // /**
    //  * 刷新审核牌照房间人数
    //  */
    // @Scheduled(cron = "10 */1 * ? * *")
    // public void refreshShpzPerson() {
    //     long startTime = System.currentTimeMillis();
    //     logger.info("[ 定时任务 ] 刷新审核牌照房间人数 start");
    //     roomTaskService.refreshShpzPerson();
    //     logger.info("[ 定时任务 ] 刷新审核牌照房间人数 finish，耗时:>{}", (System.currentTimeMillis() - startTime));
    // }

    // /**
    //  * 刷新普通房间人数
    //  */
    // @Scheduled(cron = "20 */1 * ? * *")
    // public void refreshPtPerson() {
    //     long startTime = System.currentTimeMillis();
    //     logger.info("[ 定时任务 ] 刷新普通房间人数 start");
    //     roomTaskService.refreshPtPerson();
    //     logger.info("[ 定时任务 ] 刷新普通房间人数 finish，耗时:>{}", (System.currentTimeMillis() - startTime));
    // }

    /**
     * 每10秒刷新im推过来的房间人数
     */
    @Scheduled(cron = "*/10 * * ? * *")
    public void refreshPtPerson() {
        roomTaskService.refreshOnlineNum();
    }

    // /**
    //  * 刷新房间机器人
    //  */
    // @Scheduled(cron = "0 0 */10 * * ?")
    // public void refreshRobot() {
    //     long startTime = System.currentTimeMillis();
    //     logger.info("[ 定时任务 ] 刷新房间机器人 start");
    //     roomTaskService.refreshRobot();
    //     logger.info("[ 定时任务 ] 刷新房间机器人 finish，耗时:>{}", (System.currentTimeMillis() - startTime));
    // }

    /**
     * 每30分钟刷新推荐房间列表
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void refreshRoomRecommend() {
        roomTaskService.refreshRoomRecommend();
    }
}
