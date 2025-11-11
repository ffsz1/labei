package com.erban.main.model;

import java.util.Date;

public class AuctionDeal {
    private String auctId;

    private Long uid;

    private Long auctUid;

    private Long auctMoney;

    private Integer servDura;

    private Long minRaiseMoney;

    private Long dealMoney;

    private Long dealUid;

    private String auctDesc;

    private Long roomId;

    private Byte curStatus;

    private Date createTime;

    private Date dealTime;

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

    public Long getAuctUid() {
        return auctUid;
    }

    public void setAuctUid(Long auctUid) {
        this.auctUid = auctUid;
    }

    public Long getAuctMoney() {
        return auctMoney;
    }

    public void setAuctMoney(Long auctMoney) {
        this.auctMoney = auctMoney;
    }

    public Integer getServDura() {
        return servDura;
    }

    public void setServDura(Integer servDura) {
        this.servDura = servDura;
    }

    public Long getMinRaiseMoney() {
        return minRaiseMoney;
    }

    public void setMinRaiseMoney(Long minRaiseMoney) {
        this.minRaiseMoney = minRaiseMoney;
    }

    public Long getDealMoney() {
        return dealMoney;
    }

    public void setDealMoney(Long dealMoney) {
        this.dealMoney = dealMoney;
    }

    public Long getDealUid() {
        return dealUid;
    }

    public void setDealUid(Long dealUid) {
        this.dealUid = dealUid;
    }

    public String getAuctDesc() {
        return auctDesc;
    }

    public void setAuctDesc(String auctDesc) {
        this.auctDesc = auctDesc == null ? null : auctDesc.trim();
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Byte getCurStatus() {
        return curStatus;
    }

    public void setCurStatus(Byte curStatus) {
        this.curStatus = curStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }
}
