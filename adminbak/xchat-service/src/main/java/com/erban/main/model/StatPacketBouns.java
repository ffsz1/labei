package com.erban.main.model;

import java.util.Date;

public class StatPacketBouns {
    private String bounsId;

    private Long uid;

    private String chargeRecordId;

    private Long amount;

    private Double packetNum;

    private Date createTime;

    public String getBounsId() {
        return bounsId;
    }

    public void setBounsId(String bounsId) {
        this.bounsId = bounsId == null ? null : bounsId.trim();
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getChargeRecordId() {
        return chargeRecordId;
    }

    public void setChargeRecordId(String chargeRecordId) {
        this.chargeRecordId = chargeRecordId == null ? null : chargeRecordId.trim();
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Double getPacketNum() {
        return packetNum;
    }

    public void setPacketNum(Double packetNum) {
        this.packetNum = packetNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
