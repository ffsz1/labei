package com.erban.main.model.beanmap;

public class HomeRoomFlowPeriod implements  Comparable<HomeRoomFlowPeriod> {
    private Long flowSumTotal;
    private Long uid;
    private double personFlowSumSeqNoValue;
    private int onlineNum;
    private int seqNo;

    public Long getFlowSumTotal() {
        return flowSumTotal;
    }

    public void setFlowSumTotal(Long flowSumTotal) {
        this.flowSumTotal = flowSumTotal;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public double getPersonFlowSumSeqNoValue() {
        return personFlowSumSeqNoValue;
    }

    public void setPersonFlowSumSeqNoValue(double personFlowSumSeqNoValue) {
        this.personFlowSumSeqNoValue = personFlowSumSeqNoValue;
    }

    public int getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(int onlineNum) {
        this.onlineNum = onlineNum;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    @Override
    public int compareTo(HomeRoomFlowPeriod homeRoomFlow) {
        double roomPersonFlowSumSeqNoValueFrm = homeRoomFlow.getPersonFlowSumSeqNoValue();
        double  roomPersonFlowSumSeqNoValueThis = this.getPersonFlowSumSeqNoValue();
        if (roomPersonFlowSumSeqNoValueThis > roomPersonFlowSumSeqNoValueFrm) {
            return -1;
        } else if (roomPersonFlowSumSeqNoValueThis < roomPersonFlowSumSeqNoValueThis) {
            return 1;
        } else {
            return 0;
        }
    }
}
