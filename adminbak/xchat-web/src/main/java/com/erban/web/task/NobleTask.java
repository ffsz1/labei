package com.erban.web.task;

import com.erban.main.service.noble.NobleUsersService;
import com.xchat.common.redis.RedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NobleTask extends BaseTask {
    private static final Logger logger = LoggerFactory.getLogger(NobleTask.class);

    @Autowired
    private NobleUsersService nobleUsersService;


    /**
     * 清除过期的贵族，凌晨10分和15分
     */
    @Scheduled(cron = "0 10,15 0 * * ?")
    public void clearExpireNoble() {
        logger.info("clearExpireNoble start ===============");
        nobleUsersService.releaseAllExpireNoble();
        logger.info("clearExpireNoble finish ===============");
    }

    /**
     * 提醒快过期贵族用户， 凌晨20分
     */
    @Scheduled(cron = "0 20 0 * * ?")
    public void sendNoticeNoble() {
        logger.info("sendNoticeNoble start ===============");
        nobleUsersService.sendWillExpireNotice();
        logger.info("sendNoticeNoble finish ===============");
    }


    public static void main(String[] args) {
        String version = "2.3.2";
        String replace = version.replace(".", "");
        int versionInt = Integer.parseInt(replace);
        if(versionInt<230){
            System.out.println("版本过低，请升级版本号");
        }
    }
}
