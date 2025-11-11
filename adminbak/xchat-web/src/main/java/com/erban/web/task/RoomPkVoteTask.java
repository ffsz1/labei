package com.erban.web.task;

import com.erban.main.service.room.RoomPkVoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RoomPkVoteTask {

    private final Logger logger = LoggerFactory.getLogger(RoomPkVoteTask.class);
    @Autowired
    private RoomPkVoteService voteService;

    /**
     * 检查结束的pk
     */
    @Scheduled(cron = "*/2 * * * * ?")
    public void checkFinish() {
        long startTime = System.currentTimeMillis();
        try {
            int handleCount = voteService.checkFinish();
            logger.info("[ 房间PK ]处理{}条结束记录，耗时:>{}", handleCount, (System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            logger.error("[ 房间PK ]处理异常，耗时:>{}，异常信息：", (System.currentTimeMillis() - startTime), e);
        }
    }
}
