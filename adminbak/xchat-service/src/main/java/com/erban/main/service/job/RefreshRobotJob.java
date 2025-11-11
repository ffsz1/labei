package com.erban.main.service.job;


import com.erban.main.service.RobotService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class RefreshRobotJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshRobotJob.class);

    @Autowired
    private RobotService robotService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        try {
            robotService.addRobotToPermitRoom();
        } catch (Exception e) {
            LOGGER.error("RefreshRobotJob  error...",e);
        }
    }
}
