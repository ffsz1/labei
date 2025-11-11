package com.erban.admin.main.vo;

import java.util.Date;

public class SplashScreenVO {
    private Integer picId;

    private String picName;

    private Byte userType;

    private Byte picStatus;

    private String picImage;

    private String picUrl;

    private Byte picType;

    private Date createTime;

    private Date startTime;

    private Date endTime;

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

    @Override
    public String toString() {
        return "SplashScreenVO{" +
                "picId=" + picId +
                ", picName='" + picName + '\'' +
                ", userType=" + userType +
                ", picStatus=" + picStatus +
                ", picImage='" + picImage + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", picType=" + picType +
                ", createTime=" + createTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
