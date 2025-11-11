package com.erban.main.model;

import java.util.Date;

public class HeadwearPurseRecord {
    private Long recordId;

    private Long uid;

    private Long headwearId;

    private Integer headwearDate;

    private Long totalGoldNum;

    private Byte isUse;

    private Date createTime;

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getHeadwearId() {
        return headwearId;
    }

    public void setHeadwearId(Long headwearId) {
        this.headwearId = headwearId;
    }

    public Integer getHeadwearDate() {
        return headwearDate;
    }

    public void setHeadwearDate(Integer headwearDate) {
        this.headwearDate = headwearDate;
    }

    public Long getTotalGoldNum() {
        return totalGoldNum;
    }

    public void setTotalGoldNum(Long totalGoldNum) {
        this.totalGoldNum = totalGoldNum;
    }

    public Byte getIsUse() {
        return isUse;
    }

    public void setIsUse(Byte isUse) {
        this.isUse = isUse;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
