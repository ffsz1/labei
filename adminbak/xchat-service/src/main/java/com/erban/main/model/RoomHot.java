package com.erban.main.model;

import java.util.Date;

public class RoomHot {
    private Long uid;

    private Integer roomSeq;

    private Date startLiveTime;

    private Date endLiveTime;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getRoomSeq() {
        return roomSeq;
    }

    public void setRoomSeq(Integer roomSeq) {
        this.roomSeq = roomSeq;
    }

    public Date getStartLiveTime() {
        return startLiveTime;
    }

    public void setStartLiveTime(Date startLiveTime) {
        this.startLiveTime = startLiveTime;
    }

    public Date getEndLiveTime() {
        return endLiveTime;
    }

    public void setEndLiveTime(Date endLiveTime) {
        this.endLiveTime = endLiveTime;
    }
}
