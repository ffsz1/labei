package com.erban.main.mybatismapper;


import com.erban.main.model.beanmap.HomeRoomFlowPeriod;

import java.util.List;

public interface HomeRoomFlowPeriodMapperMgr {

    List<HomeRoomFlowPeriod> getHotHomePermitRoomList();
//    List<HomeRoomFlowPeriod> getHotHomeRoomForRecomOneTwoList();
//    List<HomeRoomFlowPeriod> getHotHomeRoomForRecomThirdForthList(List<Long> uids);
//    List<HomeRoomFlowPeriod> getHotHomeRoomForGameList(List<Long> uids);
//    List<HomeRoomFlowPeriod> getHotHomeRoomForRadioList(List<Long> uids);
//
//    List<HomeRoomFlowPeriod> getGameHomeRoomList();
//    List<HomeRoomFlowPeriod> getRadioHomeRoomList();

    List<HomeRoomFlowPeriod> getGameHomeRoomPeriodList();
    List<HomeRoomFlowPeriod> getGameHomeRoomPeriodListOthers();
    List<HomeRoomFlowPeriod> getRadioHomeRoomPrriodList();

    List<HomeRoomFlowPeriod> getHomeHotManualRecommList();

}
