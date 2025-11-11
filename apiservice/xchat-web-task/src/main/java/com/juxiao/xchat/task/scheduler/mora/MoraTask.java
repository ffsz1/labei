package com.juxiao.xchat.task.scheduler.mora;

import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.service.task.mora.MoraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author chris
 * @Title:
 * @date 2019-06-04
 * @time 17:44
 */
@Component
public class MoraTask {

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private MoraService moraService;


    /**
     * 每天凌晨零点1分清除发起次数
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void clearMoraNum() {
        redisManager.del(RedisKey.mora_lave_num.getKey());
        redisManager.del(RedisKey.mora_num.getKey());
    }

    /**
     * 检测过期 30S执行一次
     */
    @Scheduled(cron = "*/30 * * ? * *")
    public void checkExpired(){
        moraService.checkExpired();
    }

}
