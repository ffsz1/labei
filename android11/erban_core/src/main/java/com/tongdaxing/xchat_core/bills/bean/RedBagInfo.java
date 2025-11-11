package com.tongdaxing.xchat_core.bills.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ${Seven} on 2017/9/25.
 */

public class RedBagInfo implements Serializable {
    public static final int INCOME_TITLE = 0;
    public static final int INCOME_CONTENT = 1;
    private int type;
    private long uid;
    private double packetNum;
    @SerializedName("recordTime")
    private long createTime;
    private String time;
    /** 红包类型 */
    private String typeStr;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public double getPacketNum() {
        return packetNum;
    }

    public void setPacketNum(double packetNum) {
        this.packetNum = packetNum;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getItemType() {
        return time == null ? INCOME_CONTENT : INCOME_TITLE;
    }


    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }
}
