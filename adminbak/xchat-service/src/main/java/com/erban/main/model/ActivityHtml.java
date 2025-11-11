package com.erban.main.model;

import java.util.Date;

public class ActivityHtml {
    private Long id;

    private String activityId;

    private String activityName;

    private String activityImage;

    private Boolean activityStatus;

    private String activityShareImage;

    private String activityShareTitle;

    private String activityShareContent;

    private String activityLink;

    private Date createTime;

    private Date updateTime;

    private Integer adminId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityImage() {
        return activityImage;
    }

    public void setActivityImage(String activityImage) {
        this.activityImage = activityImage;
    }

    public Boolean getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(Boolean activityStatus) {
        this.activityStatus = activityStatus;
    }

    public String getActivityShareImage() {
        return activityShareImage;
    }

    public void setActivityShareImage(String activityShareImage) {
        this.activityShareImage = activityShareImage;
    }

    public String getActivityShareTitle() {
        return activityShareTitle;
    }

    public void setActivityShareTitle(String activityShareTitle) {
        this.activityShareTitle = activityShareTitle;
    }

    public String getActivityShareContent() {
        return activityShareContent;
    }

    public void setActivityShareContent(String activityShareContent) {
        this.activityShareContent = activityShareContent;
    }

    public String getActivityLink() {
        return activityLink;
    }

    public void setActivityLink(String activityLink) {
        this.activityLink = activityLink;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    @Override
    public String toString() {
        return "ActivityShare{" +
                "id=" + id +
                ", activityId=" + activityId +
                ", activityName='" + activityName + '\'' +
                ", activityImage='" + activityImage + '\'' +
                ", activityStatus=" + activityStatus +
                ", activityShareImage='" + activityShareImage + '\'' +
                ", activityShareTitle='" + activityShareTitle + '\'' +
                ", activityShareContent='" + activityShareContent + '\'' +
                ", activityLink='" + activityLink + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", adminId=" + adminId +
                '}';
    }
}
