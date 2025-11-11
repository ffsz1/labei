package com.erban.main.model;

import java.util.Date;

public class ChargeRecord {
    private String chargeRecordId;

    private Long uid;

    private Long roomUid;

    private String pingxxChargeId;

    private String chargeProdId;

    private String channel;

    private Byte bussType;

    private Byte chargeStatus;

    private String chargeStatusDesc;

    private Long amount;

    private Long totalGold;

    private String clientIp;

    private String wxPubOpenid;

    private String subject;

    private String body;

    private String extra;

    private String metadata;

    private String chargeDesc;

    private Date createTime;

    private Date updateTime;

    public String getChargeRecordId() {
        return chargeRecordId;
    }

    public void setChargeRecordId(String chargeRecordId) {
        this.chargeRecordId = chargeRecordId == null ? null : chargeRecordId.trim();
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getRoomUid() {
        return roomUid;
    }

    public void setRoomUid(Long roomUid) {
        this.roomUid = roomUid;
    }

    public String getPingxxChargeId() {
        return pingxxChargeId;
    }

    public void setPingxxChargeId(String pingxxChargeId) {
        this.pingxxChargeId = pingxxChargeId == null ? null : pingxxChargeId.trim();
    }

    public String getChargeProdId() {
        return chargeProdId;
    }

    public void setChargeProdId(String chargeProdId) {
        this.chargeProdId = chargeProdId == null ? null : chargeProdId.trim();
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel == null ? null : channel.trim();
    }

    public Byte getBussType() {
        return bussType;
    }

    public void setBussType(Byte bussType) {
        this.bussType = bussType;
    }

    public Byte getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(Byte chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public String getChargeStatusDesc() {
        return chargeStatusDesc;
    }

    public void setChargeStatusDesc(String chargeStatusDesc) {
        this.chargeStatusDesc = chargeStatusDesc == null ? null : chargeStatusDesc.trim();
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getTotalGold() {
        return totalGold;
    }

    public void setTotalGold(Long totalGold) {
        this.totalGold = totalGold;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp == null ? null : clientIp.trim();
    }

    public String getWxPubOpenid() {
        return wxPubOpenid;
    }

    public void setWxPubOpenid(String wxPubOpenid) {
        this.wxPubOpenid = wxPubOpenid == null ? null : wxPubOpenid.trim();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject == null ? null : subject.trim();
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body == null ? null : body.trim();
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra == null ? null : extra.trim();
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata == null ? null : metadata.trim();
    }

    public String getChargeDesc() {
        return chargeDesc;
    }

    public void setChargeDesc(String chargeDesc) {
        this.chargeDesc = chargeDesc == null ? null : chargeDesc.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
