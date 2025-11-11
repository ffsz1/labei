package com.erban.main.model;

import java.util.Date;

public class ChargeAppleRecord {
    private Integer appleId;

    private String chargeRecordId;

    private Long uid;

    private String receip;

    private Date createTime;

    public Integer getAppleId() {
        return appleId;
    }

    public void setAppleId(Integer appleId) {
        this.appleId = appleId;
    }

    public String getChargeRecordId() {
        return chargeRecordId;
    }

    public void setChargeRecordId(String chargeRecordId) {
        this.chargeRecordId = chargeRecordId == null ? null : chargeRecordId.trim();
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getReceip() {
        return receip;
    }

    public void setReceip(String receip) {
        this.receip = receip == null ? null : receip.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
