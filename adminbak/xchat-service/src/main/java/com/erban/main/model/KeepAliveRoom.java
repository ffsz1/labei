package com.erban.main.model;

import java.util.Date;

public class KeepAliveRoom {
    private Integer keepId;

    private Long uid;

    private Long roomId;

    private Date createTime;

    public Integer getKeepId() {
        return keepId;
    }

    public void setKeepId(Integer keepId) {
        this.keepId = keepId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
