package com.erban.main.model;

import java.util.Date;

public class OrderServ {
    private Long orderId;

    private Long uid;

    private Long prodUid;

    private Long roomOwnerUid;

    private Byte orderType;

    private String objId;

    private Byte curStatus;

    private String channelId;

    private Date beginServTime;

    private Long totalMoney;

    private Integer servScore;

    private String servSocreDesc;

    private Date createTime;

    private Date finishTime;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getProdUid() {
        return prodUid;
    }

    public void setProdUid(Long prodUid) {
        this.prodUid = prodUid;
    }

    public Long getRoomOwnerUid() {
        return roomOwnerUid;
    }

    public void setRoomOwnerUid(Long roomOwnerUid) {
        this.roomOwnerUid = roomOwnerUid;
    }

    public Byte getOrderType() {
        return orderType;
    }

    public void setOrderType(Byte orderType) {
        this.orderType = orderType;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId == null ? null : objId.trim();
    }

    public Byte getCurStatus() {
        return curStatus;
    }

    public void setCurStatus(Byte curStatus) {
        this.curStatus = curStatus;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public Date getBeginServTime() {
        return beginServTime;
    }

    public void setBeginServTime(Date beginServTime) {
        this.beginServTime = beginServTime;
    }

    public Long getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Long totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Integer getServScore() {
        return servScore;
    }

    public void setServScore(Integer servScore) {
        this.servScore = servScore;
    }

    public String getServSocreDesc() {
        return servSocreDesc;
    }

    public void setServSocreDesc(String servSocreDesc) {
        this.servSocreDesc = servSocreDesc == null ? null : servSocreDesc.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }
}
