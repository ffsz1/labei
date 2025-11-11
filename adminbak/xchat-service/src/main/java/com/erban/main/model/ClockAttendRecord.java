package com.erban.main.model;

import java.util.Date;

public class ClockAttendRecord {
    private Integer clockRecordId;

    private Date clockDate;

    private Integer uid;

    private Date createTime;

    public Integer getClockRecordId() {
        return clockRecordId;
    }

    public void setClockRecordId(Integer clockRecordId) {
        this.clockRecordId = clockRecordId;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
