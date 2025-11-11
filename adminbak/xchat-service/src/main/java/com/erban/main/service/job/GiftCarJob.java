package com.erban.main.service.job;


import com.erban.main.service.gift.GiftCarService;
import com.erban.main.service.headwear.HeadwearService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class GiftCarJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(GiftCarJob.class);

    @Autowired
    private GiftCarService giftCarService;
    @Autowired
    private HeadwearService headwearService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            giftCarService.refreshAll();// 刷新座驾
            headwearService.refreshAll();// 刷新头饰
        } catch (Exception e) {
            LOGGER.error("giftCarJob  error...",e);
        }
    }
}
