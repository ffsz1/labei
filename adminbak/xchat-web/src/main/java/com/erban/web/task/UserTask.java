package com.erban.web.task;

import com.erban.main.service.user.UsersService;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UserTask extends BaseTask {
    private static final Logger logger = LoggerFactory.getLogger(UserTask.class);
    @Autowired
    private UsersService usersService;

    @Scheduled(cron = "0 0 6 * * ?")
    public void clearUserLoginRecord(){
        // 每天 6点 定时清理这个key, 用于每天记录一次用户登录记录
        jedisService.del(RedisKey.account_login_record.getKey());
        logger.info("定时任务执行========清理 账号登录的记录的key [account_login_record]");
    }

    /**
     * 缓存首页偶遇列表（最近一周注册的人，男女分开）
     */
    @Scheduled(cron = "0 */10 * ? * *")
    public void cacheOppositeSex() {
        logger.info("cacheOppositeSex start==============");
        Date endDate = new Date();
        Date startDate = DateTimeUtil.getLastDay(endDate, 7);
        usersService.saveOppositeSex("1", startDate, endDate);
        usersService.saveOppositeSex("2", startDate, endDate);
        logger.info("cacheOppositeSex finish=============");
    }

}
