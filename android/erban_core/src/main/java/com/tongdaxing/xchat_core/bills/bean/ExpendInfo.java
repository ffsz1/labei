package com.tongdaxing.xchat_core.bills.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Seven on 2017/9/9.
 */

public class ExpendInfo implements Serializable {
    public static final int TITLE = 0;
    public static final int CONTENT = 1;
    @SerializedName("srcAvatar")
    private String userAvatar;
    @SerializedName("srcNick")
    private String userNick;
    private String targetAvatar;
    private String targetNick;
    private String goldNum;
    private long recordTime;
    private int expendType;
    private int pageCount;
    private int itemType;
    private int giftNum;
    private String time;
    private long money;
    @SerializedName("giftPict")
    private String giftPic;
    @SerializedName("giftName")
    private String GiftName;
    private double diamondNum;
    private String showStr;

    public double getDiamondNum() {
        return diamondNum;
    }

    public void setDiamondNum(double diamondNum) {
        this.diamondNum = diamondNum;
    }


    public String getGiftName() {
        return GiftName;
    }

    public void setGiftName(String giftName) {
        GiftName = giftName;
    }


    public int getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(int giftNum) {
        this.giftNum = giftNum;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGiftPic() {
        return giftPic;
    }

    public void setGiftPic(String giftPic) {
        this.giftPic = giftPic;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getTargetAvatar() {
        return targetAvatar;
    }

    public void setTargetAvatar(String targetAvatar) {
        this.targetAvatar = targetAvatar;
    }

    public String getTargetNick() {
        return targetNick;
    }

    public void setTargetNick(String targetNick) {
        this.targetNick = targetNick;
    }

    public String getGoldNum() {
        return goldNum;
    }

    public void setGoldNum(String goldNum) {
        this.goldNum = goldNum;
    }

    public long getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(long recordTime) {
        this.recordTime = recordTime;
    }

    public int getExpendType() {
        return expendType;
    }

    public void setExpendType(int expendType) {
        this.expendType = expendType;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getItemType() {
        return time == null ? CONTENT : TITLE;
    }

    public String getShowStr() {
        return showStr;
    }

    public void setShowStr(String showStr) {
        this.showStr = showStr;
    }

    @Override
    public String toString() {
        return "ExpendInfo{" +
                "userAvatar='" + userAvatar + '\'' +
                ", userNick='" + userNick + '\'' +
                ", targetAvatar='" + targetAvatar + '\'' +
                ", targetNick='" + targetNick + '\'' +
                ", goldNum='" + goldNum + '\'' +
                ", recordTime=" + recordTime +
                ", expendType=" + expendType +
                ", pageCount=" + pageCount +
                ", itemType=" + itemType +
                ", giftNum=" + giftNum +
                ", time='" + time + '\'' +
                ", money=" + money +
                ", giftPic='" + giftPic + '\'' +
                ", GiftName='" + GiftName + '\'' +
                ", diamondNum=" + diamondNum +
                ", showStr='" + showStr + '\'' +
                '}';
    }
}
