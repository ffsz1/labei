package com.tongdaxing.xchat_core.bills.bean;

import com.google.gson.annotations.SerializedName;
import com.netease.nim.uikit.common.ui.recyclerview.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * 礼物收入支出实体
 * Created by Seven on 2017/9/11.
 */
public class IncomeInfo implements Serializable, MultiItemEntity {
    public static final int INCOME_TITLE = 0;
    public static final int INCOME_CONTENT = 1;
    @SerializedName(value = "srcAvatar", alternate = "userAvatar")
    private String userAvatar;
    @SerializedName(value = "srcNick", alternate = "userNick")
    private String userNick;
    private String targetAvatar;
    private String targetNick;
    private double diamondNum;
    private long recordTime;
    private int gainType;
    private int pageCount;
    private int itemType;
    private String time;
    @SerializedName("giftPict")
    private String giftPic;
    private int giftNum;
    private String giftName;
    private int money;

    public int getExpendType() {
        return expendType;
    }

    public void setExpendType(int expendType) {
        this.expendType = expendType;
    }

    private int expendType;

    public int getGoldNum() {
        return goldNum;
    }

    public void setGoldNum(int goldNum) {
        this.goldNum = goldNum;
    }

    private int goldNum;

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }


    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public int getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(int giftNum) {
        this.giftNum = giftNum;
    }

    public String getGiftPic() {
        return giftPic;
    }

    public void setGiftPic(String giftPic) {
        this.giftPic = giftPic;
    }


    public void setItemType(int itemType) {
        this.itemType = itemType;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public double getDiamondNum() {
        return diamondNum;
    }

    public void setDiamondNum(double diamondNum) {
        this.diamondNum = diamondNum;
    }

    public long getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(long recordTime) {
        this.recordTime = recordTime;
    }

    public int getGainType() {
        return gainType;
    }

    public void setGainType(int gainType) {
        this.gainType = gainType;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    @Override
    public int getItemType() {
//        Log.i("测试", "" + diamondNum);
        return time == null ? BillItemEntity.ITEM_NORMAL : BillItemEntity.ITEM_DATE;
    }
}
