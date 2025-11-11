package com.juxiao.xchat.task.scheduler.item;

import com.juxiao.xchat.service.task.item.GiftTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GiftTask {
    @Autowired
    private GiftTaskService giftTaskService;

    /**
     * 保存每天礼物统计
     */
    @Scheduled(cron = "*/30 * * * * ?")
    public void saveOneDayHome() {
        giftTaskService.saveOneDayHome();
    }

    /**
     * 确保上一天贡献完全保存，每天0点3分再统计一次上一天的数据
     * 缓存男神/女神/娱乐/电台上一天流水第一的房间
     */
    @Scheduled(cron = "0 3 0 * * ?")
    public void saveLastDayHome() {
        giftTaskService.saveLastDayHome();
    }

    /**
     * 刷新牌照房的一周排行
     */
    @Scheduled(cron = "*/30 * * * * ?")
    public void refreshOneWeek() {
        giftTaskService.refreshOneWeek();
    }

    /**
     * 保存上一周的热门新秀排行榜到缓存，时间要在0点3分之后统计，不然有可能存在丢失数据
     */
    @Scheduled(cron = "0 10 0 ? * MON")
    public void refreshLast() {
        giftTaskService.refreshLast();
    }

    /**
     * 刷新活动排行榜
     */
    @Scheduled(cron = "*/30 * * * * ?")
    public void refreshAll() {
        giftTaskService.refreshAll();
    }

    /**
     * 刷新房间分类日排行
     */
    @Scheduled(cron = "*/30 * * * * ?")
    public void refreshRoomRank() {
        giftTaskService.refreshRoomRank();
    }

    /**
     * 保存礼物排行榜到缓存
     */
    @Scheduled(cron = "*/30 * * * * ?")
    public void saveGiftRankToCache() {
        giftTaskService.saveGiftRankToCache();
    }

    /**
     * 保存上一周的热门新秀排行榜到缓存，时间要在0点3分之后统计，不然有可能存在丢失数据
     */
    @Scheduled(cron = "0 10 0 ? * MON")
    public void saveLastRoomRankToCache() {
        giftTaskService.refreshLast();
    }

    /**
     * 生成半个小时内流水数据
     */
    @Scheduled(cron = "10 */1 * * * ?")
    public void savePeriodData() {
        giftTaskService.savePeriodData();
    }

    /**
     * 重新消费礼物队列的消息
     */
    @Scheduled(cron = "*/30 * * * * ?")
    public void retryGiftQueue() {
        giftTaskService.retryGiftQueue();
    }

    /**
     * 重新消费礼物队列的消息
     */
    @Scheduled(cron = "*/30 * * * * ?")
    public void retryGiftPropQueue() {
        giftTaskService.retryGiftPropQueue();
    }

    /**
     * 重新消费全服礼物队列的消息
     */
    @Scheduled(cron = "*/30 * * * * ?")
    public void retryBigGiftQueue() {
        giftTaskService.retryBigGiftQueue();
    }

    /**
     * 重新消费捡海螺全服
     */
    @Scheduled(cron = "*/30 * * * * ?")
    public void retryGiftFullQueue() {
        giftTaskService.retryGiftFullQueue();
    }

    /**
     * 重新消费礼物队列的消息
     */
    @Scheduled(cron = "*/30 * * * * ?")
    public void retryCallQueue() {
        giftTaskService.retryCallQueue();
    }

//    /**
//     * 重新消费神秘礼物队列的消息
//     */
//    @Scheduled(cron = "*/30 * * * * ?")
//    public void retryMysticGiftQueue() {
//        logger.info("[ 定时任务 ] 重新消费神秘礼物队列 start");
//        giftTaskService.retryMysticGiftQueue();
//        logger.info("[ 定时任务 ] 重新消费神秘礼物队列 finish");
//    }
}
