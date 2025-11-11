package com.tongdaxing.xchat_core.room.auction.bean;

import java.io.Serializable;

/**
 * Created by zhouxiangfeng on 2017/5/28.
 */

public class AuctionUser implements Serializable {

    private String rivalId;
    private String auctId;
    private long uid;
    private int auctMoney;

    public AuctionUser(String rivalId, String auctId, long uid, int auctMoney) {
        this.rivalId = rivalId;
        this.auctId = auctId;
        this.uid = uid;
        this.auctMoney = auctMoney;
    }

    public String getRivalId() {
        return rivalId;
    }

    public void setRivalId(String rivalId) {
        this.rivalId = rivalId;
    }

    public String getAuctId() {
        return auctId;
    }

    public void setAuctId(String auctId) {
        this.auctId = auctId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getAuctMoney() {
        return auctMoney;
    }

    public void setAuctMoney(int auctMoney) {
        this.auctMoney = auctMoney;
    }
}
