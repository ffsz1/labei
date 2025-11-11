package com.erban.main.model;

import java.util.Date;

public class GiftCarPurseRecord {
    private Long recordId;

    private Long uid;

    private Long carId;

    private Integer carDate;

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

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public Integer getCarDate() {
        return carDate;
    }

    public void setCarDate(Integer carDate) {
        this.carDate = carDate;
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
