package com.erban.main.param;

import java.util.Date;

public class AuctionCurParam {

    private Long uid;

    private Long auctUid;

    private Long auctMoney;

    private Integer servDura;

    private Long minRaiseMoney;

    private String auctDesc;

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

    public String getAuctDesc() {
        return auctDesc;
    }

    public void setAuctDesc(String auctDesc) {
        this.auctDesc = auctDesc;
    }
}
