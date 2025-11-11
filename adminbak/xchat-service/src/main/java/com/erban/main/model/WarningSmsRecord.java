package com.erban.main.model;

import java.util.Date;

public class WarningSmsRecord {
    private Integer recordId;

    private Long uid;

    private Byte warningType;

    private Integer warningValue;

    private Date createTime;

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Byte getWarningType() {
        return warningType;
    }

    public void setWarningType(Byte warningType) {
        this.warningType = warningType;
    }

    public Integer getWarningValue() {
        return warningValue;
    }

    public void setWarningValue(Integer warningValue) {
        this.warningValue = warningValue;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
