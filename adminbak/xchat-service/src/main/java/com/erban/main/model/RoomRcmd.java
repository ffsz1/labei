package com.erban.main.model;

import java.util.Date;

public class RoomRcmd {
    private Integer rcmdId;

    private Integer minOnline;

    private Byte rcmdType;

    private Date startDate;

    private Date endDate;

    public Integer getRcmdId() {
        return rcmdId;
    }

    public void setRcmdId(Integer rcmdId) {
        this.rcmdId = rcmdId;
    }

    public Integer getMinOnline() {
        return minOnline;
    }

    public void setMinOnline(Integer minOnline) {
        this.minOnline = minOnline;
    }

    public Byte getRcmdType() {
        return rcmdType;
    }

    public void setRcmdType(Byte rcmdType) {
        this.rcmdType = rcmdType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
