package com.erban.main.model.beanmap;

import com.erban.main.model.Room;

/**
 * Created by liuguofu on 2017/10/6.
 */
public class HomeRoomFlow2 extends Room implements  Comparable<HomeRoomFlow2> {
    private Long flowSumTotal;

    private double roomPersonFlowSumSeqNoValue;

    public double getRoomPersonFlowSumSeqNoValue() {
        return roomPersonFlowSumSeqNoValue;
    }

    public void setRoomPersonFlowSumSeqNoValue(double roomPersonFlowSumSeqNoValue) {
        this.roomPersonFlowSumSeqNoValue = roomPersonFlowSumSeqNoValue;
    }

    public Long getFlowSumTotal() {
        return flowSumTotal;
    }

    public void setFlowSumTotal(Long flowSumTotal) {
        this.flowSumTotal = flowSumTotal;
    }

    @Override
    public int compareTo(HomeRoomFlow2 homeRoomFlow) {
        double roomPersonFlowSumSeqNoValueFrm = homeRoomFlow.getRoomPersonFlowSumSeqNoValue();
        double  roomPersonFlowSumSeqNoValueThis = this.getRoomPersonFlowSumSeqNoValue();
        if (roomPersonFlowSumSeqNoValueThis > roomPersonFlowSumSeqNoValueFrm) {
            return -1;
        } else if (roomPersonFlowSumSeqNoValueThis < roomPersonFlowSumSeqNoValueThis) {
            return 1;
        } else {
            return 0;
        }
    }

}
