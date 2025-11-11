package com.erban.main.model;

import java.util.Date;

public class UserGiftWall {
    private Long giftWallId;

    private Long uid;

    private Integer giftId;

    private String giftName;

    private String picUrl;

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    private Integer reciveCount;

    private Date createTime;

    public Long getGiftWallId() {
        return giftWallId;
    }

    public void setGiftWallId(Long giftWallId) {
        this.giftWallId = giftWallId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getGiftId() {
        return giftId;
    }

    public void setGiftId(Integer giftId) {
        this.giftId = giftId;
    }

    public Integer getReciveCount() {
        return reciveCount;
    }

    public void setReciveCount(Integer reciveCount) {
        this.reciveCount = reciveCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
