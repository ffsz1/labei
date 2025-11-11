package com.juxiao.xchat.dao.room.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HomeRoomFlowPeriod implements Comparable<HomeRoomFlowPeriod> {
    private Long flowSumTotal;
    private Long uid;
    private double personFlowSumSeqNoValue;
    private int onlineNum;
    private int seqNo;

    @Override
    public int compareTo(HomeRoomFlowPeriod homeRoomFlow) {
        if (this.getPersonFlowSumSeqNoValue() > homeRoomFlow.getPersonFlowSumSeqNoValue()) {
            return -1;
        } else if (this.getPersonFlowSumSeqNoValue() < homeRoomFlow.getPersonFlowSumSeqNoValue()) {
            return 1;
        } else {
            return 0;
        }
    }
}
