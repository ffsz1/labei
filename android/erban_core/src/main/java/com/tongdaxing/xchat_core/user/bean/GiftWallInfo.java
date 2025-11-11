package com.tongdaxing.xchat_core.user.bean;

import java.io.Serializable;

/**
 * Created by chenran on 2017/10/17.
 */

public class GiftWallInfo implements Serializable {
    private long uid;
    private int giftId;
    private int reciveCount;
    private String giftName;
    private String picUrl;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }

    public int getReciveCount() {
        return reciveCount;
    }

    public void setReciveCount(int reciveCount) {
        this.reciveCount = reciveCount;
    }

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
}
