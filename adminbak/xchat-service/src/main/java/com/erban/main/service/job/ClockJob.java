package com.erban.main.service.job;


import com.erban.main.service.clock.ClockResultService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class ClockJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClockJob.class);

    @Autowired
    private ClockResultService clockResultService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            clockResultService.doResult();
        } catch (Exception e) {
            LOGGER.error("clockJob  error...",e);
        }
    }
}
