package com.juxiao.xchat.task.scheduler.warning;

import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.service.task.warning.WarningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 预警定时任务
 */
@Slf4j
@Component
public class WarningTask {
    @Autowired
    private SystemConf systemConf;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private WarningService warningService;

    /**
     * 检查用户前一小时充值是否超过指定数目，如果有就发短信通知
     */
    @Scheduled(cron = "0 0 */1 * * ?")
    public void checkHourCharge() {
        if (!"prod".equalsIgnoreCase(systemConf.getEnv())) {
            return;
        }
        try {
            warningService.checkHourCharge();
        } catch (Exception e) {
            log.error("[ 用户充值预警 ] 检查用户前一小时充值金额异常:{}", e);
        }
    }

    @Scheduled(cron = "0 08 14 * * ?")
    public void checkDayCharge() {
        if (!"prod".equalsIgnoreCase(systemConf.getEnv())) {
            return;
        }
        try {
            warningService.checkDayCharge();
        } catch (Exception e) {
            log.error("[ 用户充值预警 ] 检查用户充值累计7天金额异常:{}", e);
        }
    }

    @Scheduled(cron = "0 15 */1 * * ?")
    public void checkIosCharge() {
        if (!"prod".equalsIgnoreCase(systemConf.getEnv())) {
            return;
        }
        warningService.checkRecvGift();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanDayCharge() {
        if (!"prod".equalsIgnoreCase(systemConf.getEnv())) {
            return;
        }
        redisManager.del(RedisKey.check_excess.getKey("charge_sum"));
        redisManager.del(RedisKey.check_excess.getKey("charge_count"));
        redisManager.del(RedisKey.check_excess.getKey("send_gift"));
    }


    /**
     * 检查用户当天送礼物/充值金额是否超过指定数目，如果有就发短信通知
     */
    @Scheduled(cron = "0 5 */1 * * ?")
    public void checkSendGift() {
        if (!"prod".equalsIgnoreCase(systemConf.getEnv())) {
            return;
        }
        warningService.checkSendGift();
    }

    @Scheduled(cron = "0 10 */1 * * ?")
    public void checkRecvGift() {
        if (!"prod".equalsIgnoreCase(systemConf.getEnv())) {
            return;
        }
        warningService.checkRecvGift();
    }


    /**
     * 检查tomcat是否存活
     */
    @Scheduled(cron = "0 */2 * * * ?")
    public void checkTomcat() {
        if (!"prod".equalsIgnoreCase(systemConf.getEnv())) {
            return;
        }
        warningService.checkTomcat();
    }
}
