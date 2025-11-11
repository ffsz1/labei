package com.juxiao.xchat.task.scheduler.sysconf;

import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.service.task.sysconf.SysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SysTask {
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private SysService sysService;


//    /**
//     * 检查用户当天送礼物/充值金额是否超过指定数目，如果有就发短信通知
//     */
//    @Scheduled(cron = "0 */30 * * * ?")
//    public void checkSendGift() {
//        logger.info("[ 定时任务 ] 检查用户当天送礼物/充值金额 start");
//        if ("prod".equalsIgnoreCase(systemConf.getEnv())) {
//            sysService.checkSendGift();
//        }
//        logger.info("[ 定时任务 ] 检查用户当天送礼物/充值金额 finish");
//    }
//
//    /**
//     * 检查用户前一小时充值是否超过指定数目，如果有就发短信通知
//     */
//    @Scheduled(cron = "0 0 */1 * * ?")
//    public void checkExcess() {
//        logger.info("[ 定时任务 ] 检查用户前一小时充值金额 start");
//        if ("prod".equalsIgnoreCase(systemConf.getEnv())) {
//            sysService.checkCharge();
//        }
//        logger.info("[ 定时任务 ] 检查用户前一小时充值金额 finish");
//    }
//
//    /**
//     * 检查tomcat是否存活
//     */
//    @Scheduled(cron = "0 */2 * * * ?")
//    public void checkTomcat() {
//        logger.info("[ 定时任务 ] 检查tomcat start");
//        sysService.checkTomcat();
//        logger.info("[ 定时任务 ] 检查tomcat finish");
//    }

    /**
     * 清空分成等级
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void cleanBonusLevel() {
        redisManager.del(RedisKey.bonus_level.getKey());
    }

    /**
     * 刷新座驾时间
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void refreshGiftCar() {
        sysService.refreshGiftCar();
    }

    /**
     * 刷新头饰时间
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void refreshHeadwear() {
        sysService.refreshHeadwear();
    }

    /**
     * 检查座驾信息
     */
    @Scheduled(cron = "0 0 7 * * ? ")
    public void checkGiftCar() {
        sysService.checkGiftCar();
    }

    /**
     * 检查头饰信息
     */
    @Scheduled(cron = "0 0 8 * * ? ")
    public void checkHeadwear() {
        sysService.checkHeadwear();
    }

//    /**
//     * 每小时消费金额达三万金币（服务端不要写死）以上进入推荐位
//     */
//    @Scheduled(cron = "0 0 */1 * * ?")
//    public void saveLastHourRecom() {
//        sysService.checkSaveLastHourRecom();
//    }


//    /**
//     * 每小时房间活人大于等于50人以上进入推荐位
//     */
//    @Scheduled(cron = "0 0 */1 * * ?")
//    public void saveQuickLastHourRecom() {
//        sysService.checkSaveQuickLastHourRecom();
//    }

}
