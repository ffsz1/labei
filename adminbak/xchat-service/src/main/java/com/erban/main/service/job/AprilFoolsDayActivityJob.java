package com.erban.main.service.job;


import com.erban.main.service.activity.AprilFoolsDayActivityService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class AprilFoolsDayActivityJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(AprilFoolsDayActivityJob.class);

    @Autowired
    private AprilFoolsDayActivityService aprilFoolsDayActivityService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            aprilFoolsDayActivityService.refreshAll();
        } catch (Exception e) {
            LOGGER.error("aprilFoolsDayActivityJob  error...",e);
        }
    }
}
