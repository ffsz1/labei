package com.erban.main.model;

import java.util.Date;

public class ExchangeDiamondGoldRecord {
    private String recordId;

    private Long uid;

    private Double exDiamondNum;

    private Long exGoldNum;

    private Date createTime;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId == null ? null : recordId.trim();
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Double getExDiamondNum() {
        return exDiamondNum;
    }

    public void setExDiamondNum(Double exDiamondNum) {
        this.exDiamondNum = exDiamondNum;
    }

    public Long getExGoldNum() {
        return exGoldNum;
    }

    public void setExGoldNum(Long exGoldNum) {
        this.exGoldNum = exGoldNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
