package com.erban.web.task;

// 表白墙功能暂时不用
import com.xchat.common.redis.RedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
//public class ExpressWallTask extends BaseTask{
//
//    private static final Logger logger = LoggerFactory.getLogger(ExpressWallTask.class);
//    /** 表白墙中最大条数 */
//    public static final int EXPRESS_WALL_MAX_COUNT = 200;
//
//    /**
//     * 整理表白墙列表数据,
//     */
//    @Scheduled(cron = "0 */20 * * * ?")
//    public void clearList() {
//        logger.info("清理表白墙记录====> key: {} maxCount: {}", "express_wall_list", EXPRESS_WALL_MAX_COUNT);
//        jedisService.ltrim(RedisKey.express_wall_list.getKey(), 0, EXPRESS_WALL_MAX_COUNT);
//    }
//}
