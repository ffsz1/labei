package com.erban.web.task;


import com.erban.main.service.activity.FeedbackActivityService;
import com.xchat.common.utils.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class FeedbackTask extends BaseTask{
    private static final Logger logger = LoggerFactory.getLogger(FeedbackTask.class);

    @Autowired
    private FeedbackActivityService feedbackActivityService;

    @Scheduled(cron = "0 */5 * * * ?")
    public void updateTotalDiamondNum(){
        try{
            logger.info("start to updateTotalDiamondNum =======");
            feedbackActivityService.updateUserGiftBonusPerPay(DateTimeUtil.getTodayStr());
            logger.info("update totalDiamondNum finish ========");
        }catch (Exception e){
            logger.error("update totalDiamondNum error...",e);
        }
    }
//
//
    @Scheduled(cron = "0 10 0 * * ?")
    public void computeDiamond(){
        try{
            logger.info("start to computeDiamond =========");
//            feedbackActivityService.computeDiamond();
            logger.info("compute diamond finish ==========");
        }catch (Exception e){
            logger.error("compute diamond error...",e);
        }
    }
}
