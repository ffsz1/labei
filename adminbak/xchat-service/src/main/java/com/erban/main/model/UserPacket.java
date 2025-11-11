package com.erban.main.model;

import java.util.Date;

public class UserPacket {
    private Long uid;

    private Double packetNum;

    private Double histPacketNum;

    private Date firstGetTime;

    private Date createTime;

    private Date updateTime;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Double getPacketNum() {
        return packetNum;
    }

    public void setPacketNum(Double packetNum) {
        this.packetNum = packetNum;
    }

    public Double getHistPacketNum() {
        return histPacketNum;
    }

    public void setHistPacketNum(Double histPacketNum) {
        this.histPacketNum = histPacketNum;
    }

    public Date getFirstGetTime() {
        return firstGetTime;
    }

    public void setFirstGetTime(Date firstGetTime) {
        this.firstGetTime = firstGetTime;
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
}
