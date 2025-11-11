package com.tongdaxing.xchat_core.activity.bean;

import java.io.Serializable;

/**
 * Created by chenran on 2017/12/26.
 */

public class LotteryInfo implements Serializable{
    private long uid;
    private int leftDrawNum;
    private int totalDrawNum;
    private int totalWinDrawNum;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getLeftDrawNum() {
        return leftDrawNum;
    }

    public void setLeftDrawNum(int leftDrawNum) {
        this.leftDrawNum = leftDrawNum;
    }

    public int getTotalDrawNum() {
        return totalDrawNum;
    }

    public void setTotalDrawNum(int totalDrawNum) {
        this.totalDrawNum = totalDrawNum;
    }

    public int getTotalWinDrawNum() {
        return totalWinDrawNum;
    }

    public void setTotalWinDrawNum(int totalWinDrawNum) {
        this.totalWinDrawNum = totalWinDrawNum;
    }
}
