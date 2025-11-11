package com.erban.main.model;

import java.util.Date;

public class GiftCarGetRecord {
    private Long recordId;

    private Long uid;

    private Long erbanNo;

    private Long carId;

    private String carName;

    private Integer carDate;

    private Byte type;

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

    public Long getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(Long erbanNo) {
        this.erbanNo = erbanNo;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public Integer getCarDate() {
        return carDate;
    }

    public void setCarDate(Integer carDate) {
        this.carDate = carDate;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "GiftCarGetRecord{" +
                "recordId=" + recordId +
                ", uid=" + uid +
                ", erbanNo=" + erbanNo +
                ", carId=" + carId +
                ", carName='" + carName + '\'' +
                ", carDate=" + carDate +
                ", type=" + type +
                ", createTime=" + createTime +
                '}';
    }
}
