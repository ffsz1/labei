package com.erban.main.model;

import java.util.Date;

public class UserPacketRecord {
    private String packetId;

    private Long uid;

    private Double packetNum;

    private Long srcUid;

    private Byte type;

    private String objId;

    private Boolean hasUnpack;

    private Byte packetStatus;

    private Date createTime;

    private Date updateTime;

    public String getPacketId() {
        return packetId;
    }

    public void setPacketId(String packetId) {
        this.packetId = packetId == null ? null : packetId.trim();
    }

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

    public Long getSrcUid() {
        return srcUid;
    }

    public void setSrcUid(Long srcUid) {
        this.srcUid = srcUid;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId == null ? null : objId.trim();
    }

    public Boolean getHasUnpack() {
        return hasUnpack;
    }

    public void setHasUnpack(Boolean hasUnpack) {
        this.hasUnpack = hasUnpack;
    }

    public Byte getPacketStatus() {
        return packetStatus;
    }

    public void setPacketStatus(Byte packetStatus) {
        this.packetStatus = packetStatus;
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
