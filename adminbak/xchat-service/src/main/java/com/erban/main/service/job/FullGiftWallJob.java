package com.erban.main.service.job;


import com.erban.main.service.user.UserGiftWallService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class FullGiftWallJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(FullGiftWallJob.class);

    @Autowired
    private UserGiftWallService userGiftWallService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            userGiftWallService.refreshFullGiftWallUser();
        } catch (Exception e) {
            LOGGER.error("fullGiftWallJob  error...",e);
        }
    }
}
