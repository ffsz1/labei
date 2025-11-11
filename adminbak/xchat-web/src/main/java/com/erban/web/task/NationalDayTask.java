package com.erban.web.task;

import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.service.JedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author: alwyn
 * @Description: 国庆活动
 * @Date: 2018/9/28 14:58
 */
@Component
public class NationalDayTask {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JedisService jedisService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void trimCache () {
        logger.info("[国庆任务] 清除缓存信息");
        // 删除缓存
        jedisService.del(RedisKey.national_day_task.getKey());
        jedisService.del(RedisKey.national_gift_num.getKey());
    }
}
