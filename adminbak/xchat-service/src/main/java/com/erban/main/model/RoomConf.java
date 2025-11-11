package com.erban.main.model;

import java.util.Date;

public class RoomConf {
    private Long roomUid;

    private Byte roomType;

    private Byte drawType;

    private Date createTime;

    private Date updateTime;

    private Byte charmEnable;

    public Long getRoomUid() {
        return roomUid;
    }

    public void setRoomUid(Long roomUid) {
        this.roomUid = roomUid;
    }

    public Byte getRoomType() {
        return roomType;
    }

    public void setRoomType(Byte roomType) {
        this.roomType = roomType;
    }

    public Byte getDrawType() {
        return drawType;
    }

    public void setDrawType(Byte drawType) {
        this.drawType = drawType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Byte getCharmEnable() {
        return charmEnable;
    }

    public void setCharmEnable(Byte charmEnable) {
        this.charmEnable = charmEnable;
    }
}
