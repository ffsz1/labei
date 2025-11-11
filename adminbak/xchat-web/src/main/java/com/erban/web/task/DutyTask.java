package com.erban.web.task;

import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.service.JedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DutyTask {

    private final Logger logger = LoggerFactory.getLogger(DutyTask.class);
    @Autowired
    private JedisService jedisService;

    /**
     * 每天凌晨零点1分
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void clearDailyDuty() {
        logger.info("清理每日任务开始");
        jedisService.del(RedisKey.daily_room_time.getKey());
        jedisService.del(RedisKey.duty_fresh_record.getKey());
        jedisService.del(RedisKey.duty_daily_record.getKey());
        jedisService.del(RedisKey.duty_dailytime_record.getKey());
        jedisService.del(RedisKey.duty_record.getKey());
        logger.info("清理每日任务结束");
    }
}
