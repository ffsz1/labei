package com.erban.main.service.job;

import com.erban.main.model.RoomOpenHist;
import com.erban.main.model.RoomOpenHistExample;
import com.erban.main.model.StatBasicRoom;
import com.erban.main.model.Users;
import com.erban.main.mybatismapper.RoomOpenHistMapper;
import com.erban.main.service.statis.StatBasicRoomService;
import com.erban.main.service.user.UsersService;
import com.xchat.common.utils.GetTimeUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatRoomLiveDataJob implements Job {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(StatRoomLiveDataJob.class);

    @Autowired
    private RoomOpenHistMapper roomOpenHistMapper;
    @Autowired
    private StatBasicRoomService statBasicRoomService;
    @Autowired
    private UsersService usersService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("定时任务：将今日开房时长数据写入到数据库中");
        Date date = new Date(System.currentTimeMillis() - 87400000);
        RoomOpenHistExample example = new RoomOpenHistExample();
        example.createCriteria().andCloseTimeBetween(GetTimeUtils.getTimesnights(date, 0), GetTimeUtils.getTimesnight(0));
        List<RoomOpenHist> openHists = roomOpenHistMapper.selectByExample(example);
        Map<Long, Double> map = new HashMap<>();
        for (RoomOpenHist openHist : openHists) {
            Double dura = map.put(openHist.getRoomId(), openHist.getDura());
            if (dura != null) {
                map.put(openHist.getRoomId(), dura + openHist.getDura());
                dura = null;
            }
        }
        for (Map.Entry<Long, Double> entry : map.entrySet()) {
            StatBasicRoom statBasicRoom = new StatBasicRoom();
            statBasicRoom.setRoomUid(entry.getKey());
            statBasicRoom.setSumLiveTime(entry.getValue().longValue());
            statBasicRoom.setCreateTime(new Date());
            Users users = usersService.getUsersByUid(entry.getKey());
            if (users == null) {
                logger.info("当前开播房主uid：" + entry.getKey());
            } else {
                statBasicRoom.setNick(users.getNick());
                statBasicRoom.setErbanNo(users.getErbanNo());
            }
            logger.info("更新房间开播时长到数据库,roomUid:" + entry.getKey());
            statBasicRoomService.insertBasicRoom(statBasicRoom);
        }
    }
}
