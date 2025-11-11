package com.erban.main.model;

import java.util.Date;

public class AuctionRivalRecord {
    private String rivalId;

    private String auctId;

    private Long uid;

    private Long auctMoney;

    private Date createTime;

    public String getRivalId() {
        return rivalId;
    }

    public void setRivalId(String rivalId) {
        this.rivalId = rivalId == null ? null : rivalId.trim();
    }

    public String getAuctId() {
        return auctId;
    }

    public void setAuctId(String auctId) {
        this.auctId = auctId == null ? null : auctId.trim();
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getAuctMoney() {
        return auctMoney;
    }

    public void setAuctMoney(Long auctMoney) {
        this.auctMoney = auctMoney;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
