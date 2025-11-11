package com.erban.main.mybatismapper;

import com.erban.main.model.StatRoomFlowOnlinePeriod;

import java.util.List;
import java.util.Map;

public interface StatRoomCtrbSumPeriodMapperMgr {
    /**
     * 生成半小时内的房间流水
     */
    void genPriodDataHalfHour();

    /**
     * 生成半小时内的房间流水、在线人数、排序分数
     */
    void genRoomFlowOnlinePeriod(Long totalFlow, Long totalOnline);

    Long selectTotalOnline();

    Long selectTotalFlow();

    List<StatRoomFlowOnlinePeriod> selectRoomFlowOnlinePeriod(Map<String, Object> param);

    List<StatRoomFlowOnlinePeriod> getRoomForFirstCharge(Map<String, Object> param);

    List<StatRoomFlowOnlinePeriod> getGreenHome(Map<String, Object> param);

}
