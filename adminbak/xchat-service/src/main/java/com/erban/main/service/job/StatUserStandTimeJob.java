package com.erban.main.service.job;

import com.erban.main.model.StatBasicUsers;
import com.erban.main.model.StatBasicUsersExample;
import com.erban.main.model.StatUserStandTime;
import com.erban.main.mybatismapper.StatBasicUsersMapper;
import com.erban.main.service.statis.StatBasicRoomService;
import com.erban.main.service.statis.StatBasicUsersService;
import com.erban.main.service.statis.StatUserStandTimeService;
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

public class StatUserStandTimeJob implements Job {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(StatUserStandTimeJob.class);
    @Autowired
    private StatBasicUsersService statBasicUsersService;
    @Autowired
    private StatBasicUsersMapper statBasicUsersMapper;
    @Autowired
    private StatBasicRoomService statBasicRoomService;
    @Autowired
    private StatUserStandTimeService statUserStandTimeService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("执行定时任务：写入用户每个房间停留时间.");
        Date date = new Date(System.currentTimeMillis() - 87400000);
        StatBasicUsersExample example = new StatBasicUsersExample();
        Map<Long, Map<Long, Integer>> map = new HashMap<>();
        example.createCriteria().andCreateTimeBetween(GetTimeUtils.getTimesnights(date, 0), GetTimeUtils.getTimesnight(0));
        List<StatBasicUsers> statBasicUsersList = statBasicUsersMapper.selectByExample(example);
        for (StatBasicUsers statBasicUsers : statBasicUsersList) {
            if (map.size() == 0) {
                Map<Long, Integer> map2 = new HashMap<>();
                map2.put(statBasicUsers.getRoomUid(), 1);
                map.put(statBasicUsers.getUid(), map2);
            } else {
                boolean flag = true;
                lop:for (Map.Entry<Long, Map<Long, Integer>> mapEntry : map.entrySet()) {
                    if (mapEntry.getKey().equals(statBasicUsers.getUid())) {
                        Map<Long, Integer> value = mapEntry.getValue();
                        for (Map.Entry<Long, Integer> map2Entry : value.entrySet()) {
                            if (map2Entry.getKey().equals(statBasicUsers.getRoomUid())) {
                                //同一个人同一个房间
                                value.put(map2Entry.getKey(), map2Entry.getValue() + 1);
                                map.put(statBasicUsers.getUid(), value);
                                flag = false;
                                break lop;
                            } else {
                                //同一个人不同房间
                                value.put(statBasicUsers.getRoomUid(), 1);
                                map.put(statBasicUsers.getUid(), value);
                                flag = false;
                                break lop;
                            }
                        }
                    }
                }
                if (flag) {
                    Map<Long, Integer> map2 = new HashMap<>();
                    map2.put(statBasicUsers.getRoomUid(), 1);
                    map.put(statBasicUsers.getUid(), map2);
                }
            }
        }
        for (Map.Entry<Long, Map<Long, Integer>> mapEntry : map.entrySet()) {
            StatUserStandTime statUserStandTime = new StatUserStandTime();
            statUserStandTime.setUid(mapEntry.getKey());
            for (Map.Entry<Long, Integer> map2Entry : mapEntry.getValue().entrySet()) {
                statUserStandTime.setRoomUid(map2Entry.getKey());
                statUserStandTime.setStandTime((long) map2Entry.getValue());
                statUserStandTime.setCreateTime(new Date());
                statUserStandTimeService.insertStatStandTime(statUserStandTime);
            }
        }
    }
}
