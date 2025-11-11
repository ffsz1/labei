package com.erban.main.service.job;


import com.erban.main.service.activity.HalloweenActivityService;
import com.erban.main.service.RankService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class RefreshRankJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshRankJob.class);

    @Autowired
    private RankService rankService;

    @Autowired
    private HalloweenActivityService halloweenActivityService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            LOGGER.info("start to refresh rankdata.....");
            rankService.doAllKindRankHomeVoJob();
            LOGGER.info("refresh rankdata finish.....");

//            LOGGER.info("start to refresh doHalloweenActRank.....");
//            halloweenActivityService.doHalloweenActRank();
//            LOGGER.info("refresh doHalloweenActRank finish.....");

        } catch (Exception e) {
            LOGGER.error("RefreshRankJob  error...",e);
        }
    }
}
