package com.juxiao.xchat.task.scheduler.user;

import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateTimeUtils;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.service.task.user.UsersTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Slf4j
public class UserTask {
    @Autowired
    private UsersTaskService usersTaskService;

    @Autowired
    private RedisManager redisManager;

    @Scheduled(cron = "0 0 6 * * ?")
    public void clearUserLoginRecord() {
        redisManager.del(RedisKey.account_login_record.getKey());
    }

    /**
     * 缓存首页偶遇列表（最近一周注册的人，男女分开）
     */
    @Scheduled(cron = "0 */10 * ? * *")
    public void cacheOppositeSex() {
        Date endDate = new Date();
        Date startDate = DateTimeUtils.getLastDay(endDate, 7);
        usersTaskService.saveOppositeSex("1", startDate, endDate);
        usersTaskService.saveOppositeSex("2", startDate, endDate);
    }

    @Scheduled(cron = "0 */5 * * * ?")
    public void refreshSoundPoll() {
        usersTaskService.refreshSoundPool();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void refreshLikeSoundNum() {
        redisManager.del(RedisKey.user_like_num.getKey());
    }

    /**
     * 清空礼盒
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void clearBoxDrawCache() {
        redisManager.del(RedisKey.day_first_charge.getKey());// 开礼盒机会当天有效
        redisManager.del(RedisKey.box_big_prize.getKey()); // 每天仅限10个
        redisManager.del(RedisKey.day_app_charge.getKey()); // 清空每日app内充值
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        log.info("清空礼盒每日充值 = " + df.format(new Date()));
    }
}
