package com.erban.main.model;

import java.util.Date;

public class StatUserStandTime {
    private Long id;

    private Long uid;

    private Long roomUid;

    private Long standTime;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getRoomUid() {
        return roomUid;
    }

    public void setRoomUid(Long roomUid) {
        this.roomUid = roomUid;
    }

    public Long getStandTime() {
        return standTime;
    }

    public void setStandTime(Long standTime) {
        this.standTime = standTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
