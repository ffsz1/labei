package com.erban.main.service.job;


import com.erban.main.service.activity.ActivityHtmlService;
import com.erban.main.service.activity.ValentineActivityListService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class ActivityHtmlJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityHtmlJob.class);

    @Autowired
    private ActivityHtmlService activityHtmlService;
    @Autowired
    private ValentineActivityListService valentineActivityListService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            LOGGER.info("activityHtmlJob start----------------- ");
            activityHtmlService.refreshAll();
//            valentineActivityListService.refreshAll();
        } catch (Exception e) {
            LOGGER.error("ActivityHtmlJob  error...",e);
        }
    }
}
