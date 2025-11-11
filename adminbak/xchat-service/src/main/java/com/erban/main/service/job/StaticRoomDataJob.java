package com.erban.main.service.job;

import com.erban.main.model.StatBasicRoom;
import com.erban.main.model.StatSumRoom;
import com.erban.main.mybatismapper.StatSumRoomMapper;
import com.erban.main.service.statis.StatBasicRoomService;
import com.erban.main.service.statis.StatBasicUsersService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class StaticRoomDataJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(StaticRoomDataJob.class);

    @Autowired
    private StatBasicRoomService statBasicRoomService;
    @Autowired
    private StatBasicUsersService statBasicUsersService;
    @Autowired
    private StatSumRoomMapper statSumRoomMapper;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("定时任务：将每天的人气数据统计到房间数据中.");
        List<StatBasicRoom> basicRoomList = statBasicUsersService.queryBasicUsers();
        basicRoomList = statBasicRoomService.updateRooms(basicRoomList);
        for (StatBasicRoom statBasicRoom : basicRoomList) {
            logger.info("当前房间一天的详细统计数据,roomUid" + statBasicRoom.getRoomUid() + ",Moods:" + statBasicRoom.getMoods() +
                    ",IntoPeoples:" + statBasicRoom.getRoomIntoPeoples() + ",sumLive:" + statBasicRoom.getSumLiveTime());
            StatSumRoom statSumRoom = statSumRoomMapper.selectByPrimaryKey(statBasicRoom.getRoomUid());
            if (statSumRoom == null) {
                statSumRoom = new StatSumRoom();
                statSumRoom.setMoods(statBasicRoom.getMoods());
                statSumRoom.setRoomIntoPeoples(statBasicRoom.getRoomIntoPeoples());
                statSumRoom.setRoomUid(statBasicRoom.getRoomUid());
                statSumRoom.setSumLiveTime(statBasicRoom.getSumLiveTime());
                statSumRoomMapper.insertSelective(statSumRoom);
            } else {
                Long intoPeoples = statSumRoom.getRoomIntoPeoples();
                if (intoPeoples == null) {
                    statSumRoom.setRoomIntoPeoples(statBasicRoom.getRoomIntoPeoples());
                } else {
                    Long intoPeople = statBasicRoom.getRoomIntoPeoples();
                    if (intoPeople != null) {
                        statSumRoom.setRoomIntoPeoples(intoPeople + statSumRoom.getRoomIntoPeoples());
                    }
                }
                Long moods = statSumRoom.getMoods();
                if (moods == null) {
                    statSumRoom.setMoods(statBasicRoom.getMoods());
                } else {
                    Long mood = statBasicRoom.getMoods();
                    if (mood != null) {
                        statSumRoom.setMoods(mood + statSumRoom.getMoods());
                    }
                }
                statSumRoom.setSumLiveTime(statBasicRoom.getSumLiveTime() + statSumRoom.getSumLiveTime());
                statSumRoomMapper.updateByPrimaryKey(statSumRoom);
            }
            logger.info("统计房间数据总和成功。");
        }
    }
}
