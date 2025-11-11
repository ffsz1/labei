package com.erban.main.service.statis;

import com.erban.main.model.StatSumRoom;
import com.erban.main.mybatismapper.StatSumRoomMapper;
import com.erban.main.vo.StatSumRoomVo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatSumRoomService {

    @Autowired
    private StatSumRoomMapper statSumRoomMapper;

    public BusiResult queryMoods(Long roomUid) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        StatSumRoom statSumRoom = statSumRoomMapper.selectByPrimaryKey(roomUid);
        if (statSumRoom == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        StatSumRoomVo statSumRoomVo = convertSumRoomMoodsToVo(statSumRoom);
        busiResult.setData(statSumRoomVo);
        return busiResult;
    }

    private StatSumRoomVo convertSumRoomMoodsToVo(StatSumRoom statSumRoom) {
        StatSumRoomVo statSumRoomVo = new StatSumRoomVo();
        statSumRoomVo.setRoomUid(statSumRoom.getRoomUid());
        statSumRoomVo.setMoods(statSumRoom.getMoods());
        statSumRoomVo.setRoomIntoPeoples(statSumRoom.getRoomIntoPeoples());
        return statSumRoomVo;
    }

    public BusiResult getOpenTime(Long roomUid) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        StatSumRoom statSumRoom = statSumRoomMapper.selectByPrimaryKey(roomUid);
        if (statSumRoom == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        StatSumRoomVo statSumRoomVo = convertSumOpenTimeToVo(statSumRoom);
        busiResult.setData(statSumRoomVo);
        return busiResult;
    }

    private StatSumRoomVo convertSumOpenTimeToVo(StatSumRoom statSumRoom) {
        StatSumRoomVo statSumRoomVo = new StatSumRoomVo();
        statSumRoomVo.setRoomUid(statSumRoom.getRoomUid());
        long sumLiveTime = statSumRoom.getSumLiveTime() / 60000;
        statSumRoomVo.setSumLiveTime(sumLiveTime);
        return statSumRoomVo;
    }
}
