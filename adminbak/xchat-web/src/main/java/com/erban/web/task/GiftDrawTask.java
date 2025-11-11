package com.erban.web.task;

import com.erban.main.service.user.UserGiftPurseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class GiftDrawTask extends BaseTask{

    private static final Logger logger = LoggerFactory.getLogger(GiftDrawTask.class);
    @Autowired
    private UserGiftPurseService userGiftPurseService;

    /**
     * 刷新今天拉贝排行到缓存
     */
    @Scheduled(cron = "0 */2 * * * ?")
    public void refreshRankNow(){
        long startTime = System.currentTimeMillis();
        logger.info("[ 刷新今天拉贝排行 start ]");
        userGiftPurseService.refreshRank(1);
        logger.info("[ 刷新今天拉贝排行 finish，耗时:>{} ]", (System.currentTimeMillis() - startTime));
    }

    /**
     * 刷新昨天拉贝排行到缓存
     */
    @Scheduled(cron = "0 10 0 ? * *")
    public void refreshRankLast(){
        long startTime = System.currentTimeMillis();
        logger.info("[ 刷新昨天拉贝排行 start ]");
        userGiftPurseService.refreshRank(2);
        logger.info("[ 刷新昨天拉贝排行 finish，耗时:>{} ]", (System.currentTimeMillis() - startTime));
    }

    /**
     * 清除拉贝活动礼物的数量</br>
     * 每天更新
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteDrawGiftNum(){
        long startTime = System.currentTimeMillis();
        logger.info("[ 清除活动的神秘礼物数量 start ]");
        userGiftPurseService.deleteDrawGiftNum();
        logger.info("[ 清除活动的神秘礼物数量 finish，耗时:>{} ]", (System.currentTimeMillis() - startTime));
    }

}
