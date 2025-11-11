package com.erban.main.model;

import java.util.Date;

public class ClockResultRecord {
    private Integer clockResultId;

    private Date clockDate;

    private Integer uid;

    private Integer goldAmount;

    private Date createTime;

    public Integer getClockResultId() {
        return clockResultId;
    }

    public void setClockResultId(Integer clockResultId) {
        this.clockResultId = clockResultId;
    }

    public Date getClockDate() {
        return clockDate;
    }

    public void setClockDate(Date clockDate) {
        this.clockDate = clockDate;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getGoldAmount() {
        return goldAmount;
    }

    public void setGoldAmount(Integer goldAmount) {
        this.goldAmount = goldAmount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
