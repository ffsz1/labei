package com.juxiao.xchat.task.scheduler.mcoin;

import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.room.StatBasicUsersDao;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class McoinTask {
    @Autowired
    private StatBasicUsersDao basicUsersDao;
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
        redisManager.del(RedisKey.mcoin_mission_list.getKey());
        redisManager.del(RedisKey.mcoin_mission_finish_hash.getKey());

        redisManager.del(RedisKey.gift_draw_free_hash.getKey());
        redisManager.del(RedisKey.gift_draw_free_limit_hash.getKey());
        redisManager.del(RedisKey.user_home_never_notify_zset.getKey());

        redisManager.del(RedisKey.level_user_charge.getKey());
        basicUsersDao.deleteUnuse();
    }
}
