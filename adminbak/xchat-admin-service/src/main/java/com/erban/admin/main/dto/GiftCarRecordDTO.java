package com.erban.admin.main.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.util.Date;

public class GiftCarRecordDTO {
    private Long recordId;

    private Long uid;

    @Excel(name = "拉贝号", orderNum = "0")
    private Long erbanNo;

    private Long carId;

    @Excel(name = "座驾名称", orderNum = "1")
    private String carName;

    @Excel(name = "有效天数", orderNum = "3")
    private Integer carDate;

    private Byte type;

    @Excel(name = "发放时间", exportFormat = "yyyy-MM-dd HH:mm:ss", orderNum = "2")
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
        return "GiftCarRecordDTO{" +
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
