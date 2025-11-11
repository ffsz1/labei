package com.erban.main.service.statis;

import com.erban.main.model.StatBasicRoom;
import com.erban.main.model.StatUserStandTime;
import com.erban.main.model.StatUserStandTimeExample;
import com.erban.main.mybatismapper.StatUserStandTimeMapper;
import com.xchat.common.utils.GetTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class StatUserStandTimeService {
    private static final Logger logger = LoggerFactory.getLogger(StatUserStandTimeService.class);

    @Autowired
    private StatUserStandTimeMapper statUserStandTimeMapper;

    @Autowired
    private StatBasicRoomService statBasicRoomService;

    public void insertStandTimeData(Long uid, Long roomUid) {
        if (uid != null && roomUid != null) {
            saveOrUpdate(uid, roomUid);
        }
    }

    private void saveOrUpdate(Long uid, Long roomUid) {
        Date date = new Date(System.currentTimeMillis() - 87400000);
        StatUserStandTime statUserStandTime = null;
        StatUserStandTimeExample example = new StatUserStandTimeExample();
        example.createCriteria().andUidEqualTo(uid).andRoomUidEqualTo(roomUid).andCreateTimeBetween(GetTimeUtils.getTimesnights(date, 0), GetTimeUtils.getTimesnight(0));
        List<StatUserStandTime> statUserStandTimeList = statUserStandTimeMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(statUserStandTimeList)) {
            statUserStandTime = new StatUserStandTime();
            statUserStandTime.setRoomUid(roomUid);
            statUserStandTime.setUid(uid);
            statUserStandTime.setStandTime(1L);
            statUserStandTime.setCreateTime(new Date());
            statUserStandTimeMapper.insert(statUserStandTime);
        } else {
            statUserStandTime = statUserStandTimeList.get(0);
            statUserStandTime.setStandTime(statUserStandTime.getStandTime() + 1);
            statUserStandTime.setCreateTime(new Date());
            statUserStandTimeMapper.updateByPrimaryKeySelective(statUserStandTime);
        }
    }

    public List<StatBasicRoom> queryRoomMoods(List<StatBasicRoom> statBasicRoomList) {
        Date date = new Date(System.currentTimeMillis() - 87400000);
        StatUserStandTimeExample example = new StatUserStandTimeExample();
        example.createCriteria().andCreateTimeBetween(GetTimeUtils.getTimesnights(date, 0), GetTimeUtils.getTimesnight(0));
        example.setOrderByClause("uid asc");
        List<StatUserStandTime> statUserStandTimeList = statUserStandTimeMapper.selectByExample(example);
        Map<Long, Long> map = new LinkedHashMap<>();
        long x = 0;
        Long roomUid = null;
        for (int i = 0; i < statUserStandTimeList.size(); i++) {
            if (i != 0) {
                if (statUserStandTimeList.get(i).getUid().equals(statUserStandTimeList.get(i - 1).getUid())) {
                    map.put(statUserStandTimeList.get(i).getRoomUid(), statUserStandTimeList.get(i).getStandTime());
                    if (i + 1 == statUserStandTimeList.size()) {
                        for (Map.Entry<Long, Long> entry : map.entrySet()) {
                            long y = entry.getValue();
                            if (y > x ? true : false) {
                                x = y;
                                roomUid = entry.getKey();
                            }
                        }
                        iterationList(statBasicRoomList, roomUid);
                    }
                } else {
                    for (Map.Entry<Long, Long> entry : map.entrySet()) {
                        long y = entry.getValue();
                        if (y > x ? true : false) {
                            x = y;
                            roomUid = entry.getKey();
                        }
                    }
                    iterationList(statBasicRoomList, roomUid);
                    map.clear();
                    x = 0;
                    roomUid = null;
                    map.put(statUserStandTimeList.get(i).getRoomUid(), statUserStandTimeList.get(i).getStandTime());
                }
            } else {
                map.put(statUserStandTimeList.get(i).getRoomUid(), statUserStandTimeList.get(i).getStandTime());
            }
        }
        return statBasicRoomList;
    }

    private void iterationList(List<StatBasicRoom> statBasicRoomList, Long roomUid) {
        for (StatBasicRoom statBasicRoom : statBasicRoomList) {
            if (statBasicRoom.getRoomUid().longValue() == roomUid.longValue()) {
                Long moods = statBasicRoom.getMoods();
                if (moods == null) {
                    statBasicRoom.setMoods(1L);
                } else {
                    statBasicRoom.setMoods(statBasicRoom.getMoods() + 1);
                }
            }
        }
    }

    public void query() {
        List<StatBasicRoom> statBasicRoomList = new ArrayList<>();
        StatBasicRoom statBasicRoom = new StatBasicRoom();
        statBasicRoom.setRoomUid(1L);
        statBasicRoom.setRoomIntoPeoples(10L);
        statBasicRoom.setSumLiveTime(50L);
        StatBasicRoom statBasicRoom2 = new StatBasicRoom();
        statBasicRoom2.setRoomUid(2L);
        statBasicRoom2.setRoomIntoPeoples(10L);
        statBasicRoom2.setSumLiveTime(50L);
        statBasicRoomList.add(statBasicRoom);
        statBasicRoomList.add(statBasicRoom2);
        queryRoomMoods(statBasicRoomList);
    }

    public List<StatUserStandTime> getAvgTime(Long roomUid, Date time) {
        StatUserStandTimeExample example = new StatUserStandTimeExample();
        example.createCriteria().andRoomUidEqualTo(roomUid).andCreateTimeBetween(GetTimeUtils.getTimesnights(time, 0), GetTimeUtils.getTimesnights(time, 0));
        List<StatUserStandTime> statUserStandTimeList = statUserStandTimeMapper.selectByExample(example);
        return statUserStandTimeList;
    }

    public void insertStatStandTime(StatUserStandTime statUserStandTime) {
        statUserStandTimeMapper.insertSelective(statUserStandTime);
    }
}
