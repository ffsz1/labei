package com.juxiao.xchat.task.scheduler.event;

import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DutyTask {
    @Autowired
    private RedisManager redisManager;

    /**
     * 每天凌晨零点1分
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void clearDailyDuty() {
        redisManager.del(RedisKey.daily_room_time.getKey());
        redisManager.del(RedisKey.duty_daily_record.getKey());
        redisManager.del(RedisKey.duty_dailytime_record.getKey());
        redisManager.del(RedisKey.duty_record.getKey());
    }
}
