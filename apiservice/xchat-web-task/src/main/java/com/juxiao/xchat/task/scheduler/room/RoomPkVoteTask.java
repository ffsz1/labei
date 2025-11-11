package com.juxiao.xchat.task.scheduler.room;

import com.juxiao.xchat.service.task.room.RoomPkVoteTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RoomPkVoteTask {

    @Autowired
    private RoomPkVoteTaskService voteService;

//    /**
//     * 检查结束的pk
//     */
//    @Scheduled(cron = "*/1 * * * * ?")
//    public void checkFinish() {
//        try {
//            voteService.checkFinish();
//        } catch (Exception e) {
//            log.error("[ 房间PK ]处理异常,异常信息:{}",  e);
//        }
//    }
}
