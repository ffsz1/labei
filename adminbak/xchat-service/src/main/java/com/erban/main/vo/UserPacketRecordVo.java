package com.erban.main.vo;

import java.util.Date;

public class UserPacketRecordVo {
    private String packetName;
    private Byte type;

    private Long uid;

    private Double packetNum;

    private Date createTime;

    private boolean needAlert;

    public void setType(Byte type) {
        this.type = type;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public void setPacketNum(Double packetNum) {
        this.packetNum = packetNum;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Byte getType() {

        return type;
    }

    public String getPacketName() {
        return packetName;
    }

    public void setPacketName(String packetName) {
        this.packetName = packetName;
    }

    public boolean isNeedAlert() {
        return needAlert;
    }

    public void setNeedAlert(boolean needAlert) {
        this.needAlert = needAlert;
    }

    public Long getUid() {
        return uid;
    }

    public Double getPacketNum() {
        return packetNum;
    }

    public Date getCreateTime() {
        return createTime;
    }
}
