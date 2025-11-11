package com.erban.main.service.statis;

import com.erban.main.model.*;
import com.erban.main.mybatismapper.StatBasicRoomMapper;
import com.erban.main.vo.StatBasicRoomVo;
import com.erban.main.vo.StatUserStandTimeVo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.GetTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
public class StatBasicRoomService {
    private static final Logger logger = LoggerFactory.getLogger(StatBasicRoomService.class);

    @Autowired
    private StatBasicRoomMapper statBasicRoomMapper;

    @Autowired
    private StatUserStandTimeService statUserStandTimeService;


    public void updateBasicRoom(Room room) throws Exception {
        if (room == null) {
            logger.info("录入房间基础数据出错.");
        }
        saveOrUpdateRoom(room);
    }

    private void saveOrUpdateRoom(Room room) {
        StatBasicRoomExample example = new StatBasicRoomExample();
        example.createCriteria().andRoomUidEqualTo(room.getUid()).andCreateTimeBetween(GetTimeUtils.getTimesnight(0), GetTimeUtils.getTimesnight(0));
        List<StatBasicRoom> statBasicRoomList = statBasicRoomMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(statBasicRoomList)) {
            return;
        }
        StatBasicRoom statBasicRoom = statBasicRoomList.get(0);
        Long nowTime = statBasicRoom.getSumLiveTime();
        Long sumTime = System.currentTimeMillis() - room.getOpenTime().getTime();
        if (nowTime == null) {
            statBasicRoom.setSumLiveTime(sumTime);
        } else {
            statBasicRoom.setSumLiveTime(statBasicRoom.getSumLiveTime() + sumTime);
        }
        statBasicRoomMapper.updateByPrimaryKeySelective(statBasicRoom);
    }

    private StatBasicRoom addBasicRoom(Room room) {
        StatBasicRoom statBasicRoom = new StatBasicRoom();
        statBasicRoom.setRoomUid(room.getUid());
        Long sumTime = System.currentTimeMillis() - room.getOpenTime().getTime();
        statBasicRoom.setSumLiveTime(sumTime);
        return statBasicRoom;
    }


    public List<StatBasicRoom> updateRoom(List<StatBasicUsers> statBasicUsersList) {
        Date date = new Date(System.currentTimeMillis() - 87400000);
        StatBasicRoomExample example = new StatBasicRoomExample();
        example.createCriteria().andCreateTimeBetween(GetTimeUtils.getTimesnights(date, 0), GetTimeUtils.getTimesnight(0));
        List<StatBasicRoom> statBasicRoomList = statBasicRoomMapper.selectByExample(example);
        for (StatBasicUsers statBasicUsers : statBasicUsersList) {
            logger.info("将当前房间进入人数写入到统计房间表中，roomUid:" + statBasicUsers.getRoomUid() + ",uid:" + statBasicUsers.getUid());
            for (StatBasicRoom statBasicRoom : statBasicRoomList) {
                if (statBasicUsers.getRoomUid().equals(statBasicRoom.getRoomUid())) {
                    Long peopleNumber = statBasicRoom.getRoomIntoPeoples();
                    if (peopleNumber != null) {
                        statBasicRoom.setRoomIntoPeoples(peopleNumber + 1);
                    } else {
                        statBasicRoom.setRoomIntoPeoples(1L);
                    }
                }
            }
        }
        return statBasicRoomList;
    }

    public List<StatBasicRoom> updateRooms(List<StatBasicRoom> statBasicRoomList) {
        statBasicRoomList = statUserStandTimeService.queryRoomMoods(statBasicRoomList);
        for (StatBasicRoom statBasicRoom : statBasicRoomList) {
            statBasicRoomMapper.updateByPrimaryKey(statBasicRoom);
        }
        return statBasicRoomList;
    }

    public void saveOrUpdate(Room room) throws Exception {
        StatBasicRoomExample example = new StatBasicRoomExample();
        example.createCriteria().andRoomUidEqualTo(room.getUid()).andCreateTimeBetween(GetTimeUtils.getTimesnight(0), GetTimeUtils.getTimesnight(0));
        List<StatBasicRoom> statBasicRoomList = statBasicRoomMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(statBasicRoomList)) {
            StatBasicRoom statBasicRoom = new StatBasicRoom();
            statBasicRoom.setRoomUid(room.getUid());
            statBasicRoom.setCreateTime(new Date());
            statBasicRoomMapper.insertSelective(statBasicRoom);
        }
    }

    public BusiResult getMoods(Long roomUid, Date time) throws Exception {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        StatBasicRoomExample example = new StatBasicRoomExample();
        example.createCriteria().andRoomUidEqualTo(roomUid).andCreateTimeBetween(GetTimeUtils.getTimesnights(time, 0), GetTimeUtils.getTimesnights(time, 0));
        List<StatBasicRoom> statBasicRoomList = statBasicRoomMapper.selectByExample(example);
        if (statBasicRoomList == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        StatBasicRoom statBasicRoom = statBasicRoomList.get(0);
        StatBasicRoomVo statBasicRoomVo = convertSumRoomMoodsToVo(statBasicRoom);
        busiResult.setData(statBasicRoomVo);
        return busiResult;
    }

    private StatBasicRoomVo convertSumRoomMoodsToVo(StatBasicRoom statBasicRoom) {
        StatBasicRoomVo statBasicRoomVo = new StatBasicRoomVo();
        statBasicRoomVo.setRoomUid(statBasicRoom.getRoomUid());
        statBasicRoomVo.setMoods(statBasicRoom.getMoods());
        statBasicRoomVo.setRoomIntoPeoples(statBasicRoom.getRoomIntoPeoples());
        return statBasicRoomVo;
    }

    public BusiResult getOpenTime(Long roomUid, Date time) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        StatBasicRoomExample example = new StatBasicRoomExample();
        example.createCriteria().andRoomUidEqualTo(roomUid).andCreateTimeBetween(GetTimeUtils.getTimesnights(time, 0), GetTimeUtils.getTimesnights(time, 0));
        List<StatBasicRoom> statBasicRoomList = statBasicRoomMapper.selectByExample(example);
        if (statBasicRoomList == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        StatBasicRoom statBasicRoom = statBasicRoomList.get(0);
        StatBasicRoomVo statBasicRoomVo = convertSumOpenTimeToVo(statBasicRoom);
        busiResult.setData(statBasicRoomVo);
        return busiResult;
    }

    private StatBasicRoomVo convertSumOpenTimeToVo(StatBasicRoom statBasicRoom) {
        StatBasicRoomVo statBasicRoomVo = new StatBasicRoomVo();
        statBasicRoomVo.setRoomUid(statBasicRoom.getRoomUid());
        long sumLiveTime = statBasicRoom.getSumLiveTime() / 60000;
        statBasicRoomVo.setSumLiveTime(sumLiveTime);
        return statBasicRoomVo;
    }

    public BusiResult getRooms(Date time) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        StatBasicRoomExample example = new StatBasicRoomExample();
        example.createCriteria().andCreateTimeBetween(GetTimeUtils.getTimesnights(time, 0), GetTimeUtils.getTimesnights(time, 0));
        List<StatBasicRoom> statBasicRoomList = statBasicRoomMapper.selectByExample(example);
        if (statBasicRoomList == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        busiResult.setData(statBasicRoomList);
        return busiResult;
    }

    public BusiResult getAvgTime(Long roomUid, Date time) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        List<StatUserStandTime> statUserStandTimeList = statUserStandTimeService.getAvgTime(roomUid, time);
        if (statUserStandTimeList.size() == 0) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        long stand = 0;
        for (StatUserStandTime statUserStandTime : statUserStandTimeList) {
            stand += statUserStandTime.getStandTime().longValue();
        }
        StatUserStandTimeVo statUserStandTimeVo = new StatUserStandTimeVo();
        double avgTime = stand / statUserStandTimeList.size();
        statUserStandTimeVo.setAvgTime(avgTime);
        busiResult.setData(statUserStandTimeVo);
        return busiResult;
    }

    public void insertBasicRoom(StatBasicRoom statBasicRoom) {
        statBasicRoomMapper.insertSelective(statBasicRoom);
    }
}
