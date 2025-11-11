package com.erban.main.model;

import java.util.Date;

public class StatRoomCtrbSumTotal {
    private Long uid;

    private Long flowSumTotal;

    private Date createTime;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getFlowSumTotal() {
        return flowSumTotal;
    }

    public void setFlowSumTotal(Long flowSumTotal) {
        this.flowSumTotal = flowSumTotal;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
