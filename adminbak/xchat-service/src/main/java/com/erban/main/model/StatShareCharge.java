package com.erban.main.model;

import java.util.Date;

public class StatShareCharge {
    private Integer shareChargeId;

    private Long uid;

    private Integer erbanNo;

    private String nick;

    private Byte shareChannel;

    private Integer registerCount;

    private Integer morethanChargeCount;

    private Integer chargeCount;

    private Date statDate;

    public Integer getShareChargeId() {
        return shareChargeId;
    }

    public void setShareChargeId(Integer shareChargeId) {
        this.shareChargeId = shareChargeId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(Integer erbanNo) {
        this.erbanNo = erbanNo;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick == null ? null : nick.trim();
    }

    public Byte getShareChannel() {
        return shareChannel;
    }

    public void setShareChannel(Byte shareChannel) {
        this.shareChannel = shareChannel;
    }

    public Integer getRegisterCount() {
        return registerCount;
    }

    public void setRegisterCount(Integer registerCount) {
        this.registerCount = registerCount;
    }

    public Integer getMorethanChargeCount() {
        return morethanChargeCount;
    }

    public void setMorethanChargeCount(Integer morethanChargeCount) {
        this.morethanChargeCount = morethanChargeCount;
    }

    public Integer getChargeCount() {
        return chargeCount;
    }

    public void setChargeCount(Integer chargeCount) {
        this.chargeCount = chargeCount;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }
}
