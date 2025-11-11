package com.erban.main.model;

import java.util.Date;

public class GiftSendRecord {
    private Long sendRecordId;

    private Long uid;

    private Long reciveUid;

    private Byte sendEnv;

    private Long roomUid;

    private Byte roomType;

    private Integer giftId;

    private Integer giftNum;

    private Long totalGoldNum;

    private Double totalDiamondNum;

    private Date createTime;

    public Long getSendRecordId() {
        return sendRecordId;
    }

    public void setSendRecordId(Long sendRecordId) {
        this.sendRecordId = sendRecordId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getReciveUid() {
        return reciveUid;
    }

    public void setReciveUid(Long reciveUid) {
        this.reciveUid = reciveUid;
    }

    public Byte getSendEnv() {
        return sendEnv;
    }

    public void setSendEnv(Byte sendEnv) {
        this.sendEnv = sendEnv;
    }

    public Long getRoomUid() {
        return roomUid;
    }

    public void setRoomUid(Long roomUid) {
        this.roomUid = roomUid;
    }

    public Byte getRoomType() {
        return roomType;
    }

    public void setRoomType(Byte roomType) {
        this.roomType = roomType;
    }

    public Integer getGiftId() {
        return giftId;
    }

    public void setGiftId(Integer giftId) {
        this.giftId = giftId;
    }

    public Integer getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(Integer giftNum) {
        this.giftNum = giftNum;
    }

    public Long getTotalGoldNum() {
        return totalGoldNum;
    }

    public void setTotalGoldNum(Long totalGoldNum) {
        this.totalGoldNum = totalGoldNum;
    }

    public Double getTotalDiamondNum() {
        return totalDiamondNum;
    }

    public void setTotalDiamondNum(Double totalDiamondNum) {
        this.totalDiamondNum = totalDiamondNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
