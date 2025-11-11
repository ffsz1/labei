package com.erban.main.model;

import java.util.Date;

public class StatAkiraAuct {
    private Long akiraAuctId;

    private Long uid;

    private Integer totalAuctMoney;

    private Integer totalAuctCount;

    private java.sql.Date statDate;

    private Date createTime;

    public Long getAkiraAuctId() {
        return akiraAuctId;
    }

    public void setAkiraAuctId(Long akiraAuctId) {
        this.akiraAuctId = akiraAuctId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getTotalAuctMoney() {
        return totalAuctMoney;
    }

    public void setTotalAuctMoney(Integer totalAuctMoney) {
        this.totalAuctMoney = totalAuctMoney;
    }

    public Integer getTotalAuctCount() {
        return totalAuctCount;
    }

    public void setTotalAuctCount(Integer totalAuctCount) {
        this.totalAuctCount = totalAuctCount;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(java.sql.Date statDate) {
        this.statDate = statDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
