package com.erban.web.task;

import com.erban.main.message.BigGiftMessage;
import com.erban.main.message.GiftMessage;
import com.erban.main.service.HomeService;
import com.erban.main.service.RankService;
import com.erban.main.service.activity.ActivityHtmlService;
import com.erban.main.service.gift.GiftMessageService;
import com.erban.main.service.gift.GiftSendService;
import com.erban.main.service.home.CheckExcessService;
import com.xchat.common.redis.RedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;


/**
 * 礼物排行榜，定时生成榜单
 */
@Component
public class GiftTask extends BaseTask{

    private static final Logger logger = LoggerFactory.getLogger(GiftTask.class);
    @Autowired
    private RankService rankService;
    @Autowired
    private HomeService homeService;
    @Autowired
    private GiftMessageService giftMessageService;
    @Autowired
    private CheckExcessService checkExcessService;
    @Autowired
    private ActivityHtmlService activityHtmlService;
    @Autowired
    private GiftSendService giftSendService;
    public static volatile boolean isDoHomeDataJob = false;

    /**
     * 检查单个用户当天送礼物金额是否超过指定数目，如果有就发短信通知
     */
    @Scheduled(cron = "0 */30 * * * ?")
    public void checkExcess(){
        try {
            logger.info("start to checkExcess ==================");
            checkExcessService.check();
            logger.info("checkExcess finish. ==================");
        } catch (Exception e) {
            logger.error("checkExcess error...",e.getMessage());
        }
    }

    /**
     * 保存礼物排行榜到缓存
     */
    @Scheduled(cron = "5 */2 * * * ?")
    public void saveGiftRankToCache(){
        try {
            logger.info("start to refresh rankdata ==================");
            rankService.doAllKindRankHomeVoJob();
            logger.info("refresh rankdata finish. ==================");
        } catch (Exception e) {
            logger.error("RefreshRankJob  error...",e.getMessage());
        }
    }

    /**
     * 保存上一周的土豪排行榜到缓存
     */
    @Scheduled(cron = "0 59 23 ? * SUN")
    public void saveLastRankToCache(){
        try {
            logger.info("start to refresh lastrankdata ==================");
            rankService.doLastRankJob();
            logger.info("refresh lastrankdata finish. ==================");
        } catch (Exception e) {
            logger.error("RefreshRankJob  error...",e.getMessage());
        }
    }

    /**
     * 保存上一周的热门新秀排行榜到缓存，时间要在0点3分之后统计，不然有可能存在丢失数据
     */
    @Scheduled(cron = "0 10 0 ? * MON")
    public void saveLastRoomRankToCache(){
        try {
            logger.info("start to refresh lastRoomRankData ==================");
            activityHtmlService.refreshLast();
            logger.info("refresh lastRoomRankData finish. ==================");
        } catch (Exception e) {
            logger.error("saveLastRoomRankToCache  error...",e.getMessage());
        }
    }

    /**
     * 生成半个小时内流水数据, 5min
     */
    @Scheduled(cron = "0 */2 * * * ?")
    public void genPeriodData(){
        try {
            isDoHomeDataJob = true;
            logger.info("genPeriodData start ==================");
            homeService.genPeriodData();
            logger.info("genPeriodData finish ==================");
        } catch (Exception e) {
            logger.error("doHomeDataJob error,", e.getMessage());
        }finally {
            isDoHomeDataJob = false;
        }
    }

    /**
     * 重新消费队列的消息
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void retryGiftQueue(){
        Map<String,String> map = jedisService.hgetAll(RedisKey.mq_gift_status.getKey());
        if (map == null || map.size() == 0) {
            return;
        }
        Set<String> keySet = map.keySet();
        long curTime = System.currentTimeMillis();
        long gapTime = 1000 * 60 * 1;  // 一分钟内没被消费

        for (String key : keySet) {
            try {
                String val = map.get(key);
                GiftMessage giftMessage = gson.fromJson(val, GiftMessage.class);
                if(curTime - giftMessage.getMessTime() > gapTime) {
                    giftMessageService.handleGiftMessage(giftMessage);
                }
            } catch (Exception e) {
                logger.error("retryGiftQueue error", e.getMessage());
            }
        }
    }

    /**
     * 重新消费队列的消息
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void retryBigGiftQueue(){
        Map<String,String> map = jedisService.hgetAll(RedisKey.mq_big_gift_status.getKey());
        if (map == null || map.size() == 0) {
            return;
        }
        Set<String> keySet = map.keySet();
        long curTime = System.currentTimeMillis();
        long gapTime = 1000 * 60 * 1;  // 一分钟内没被消费

        for (String key : keySet) {
            try {
                String val = map.get(key);
                BigGiftMessage giftMessage = gson.fromJson(val, BigGiftMessage.class);
                if(curTime - giftMessage.getMessTime() > gapTime) {
                    giftSendService.handleBigGiftMessage(giftMessage);
                }
            } catch (Exception e) {
                logger.error("retryBigGiftQueue error", e.getMessage());
            }
        }
    }

}
