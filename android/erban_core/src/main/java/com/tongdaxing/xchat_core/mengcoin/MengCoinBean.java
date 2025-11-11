package com.tongdaxing.xchat_core.mengcoin;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 文件描述：
 * 萌币基础bean类
 *
 * @auther：zwk
 * @data：2019/1/15
 */
public class MengCoinBean implements MultiItemEntity {
    public static final int ITEM_DAILY = 0;
    public static final int ITEM_TITLE = 1;

    private float mcoinAmount;
    private int missionId;
    private String missionName;
    private int missionStatus;//2表示已经签到
    private String scheme;
    private int intentType;
    private String picUrl;
    private int itemType;

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public float getMcoinAmount() {
        return mcoinAmount;
    }

    public void setMcoinAmount(float mcoinAmount) {
        this.mcoinAmount = mcoinAmount;
    }

    public int getMissionId() {
        return missionId;
    }

    public void setMissionId(int missionId) {
        this.missionId = missionId;
    }

    public String getMissionName() {
        return missionName;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    public int getMissionStatus() {
        return missionStatus;
    }

    public void setMissionStatus(int missionStatus) {
        this.missionStatus = missionStatus;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public int getIntentType() {
        return intentType;
    }

    public void setIntentType(int intentType) {
        this.intentType = intentType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
