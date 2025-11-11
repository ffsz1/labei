package com.juxiao.xchat.dao.sysconf.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HomeRoomFlowDTO  implements  Comparable<HomeRoomFlowDTO> {
    private Long flowSumTotal;
    private Long uid;
    private double personFlowSumSeqNoValue;
    private int onlineNum;
    private int seqNo;

    @Override
    public int compareTo(HomeRoomFlowDTO o) {
        return this.getPersonFlowSumSeqNoValue() > o.getPersonFlowSumSeqNoValue() ? -1 : 1;
    }
}
