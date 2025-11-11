package com.erban.admin.main.dto;

import java.util.Date;

/**
 * 闪屏DTO
 */
public class SplashScreenDTO {
    private Integer picId;

    private String picName;

    private Byte userType;

    private Byte picStatus;

    private String picImage;

    private String picUrl;

    private Byte picType;

    private Date createTime;

    private Date startTime;

    private String startDate;

    private Date endTime;

    private String endDate;

    public Integer getPicId() {
        return picId;
    }

    public void setPicId(Integer picId) {
        this.picId = picId;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public Byte getUserType() {
        return userType;
    }

    public void setUserType(Byte userType) {
        this.userType = userType;
    }

    public Byte getPicStatus() {
        return picStatus;
    }

    public void setPicStatus(Byte picStatus) {
        this.picStatus = picStatus;
    }

    public String getPicImage() {
        return picImage;
    }

    public void setPicImage(String picImage) {
        this.picImage = picImage;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Byte getPicType() {
        return picType;
    }

    public void setPicType(Byte picType) {
        this.picType = picType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "SplashScreenDTO{" +
                "picId=" + picId +
                ", picName='" + picName + '\'' +
                ", userType=" + userType +
                ", picStatus=" + picStatus +
                ", picImage='" + picImage + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", picType=" + picType +
                ", createTime=" + createTime +
                ", startTime=" + startTime +
                ", startDate='" + startDate + '\'' +
                ", endTime=" + endTime +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
