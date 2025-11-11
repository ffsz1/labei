package com.erban.main.model;

import java.util.Date;

public class RoomOpenHist {
    private String histId;

    private Long uid;

    private Long roomId;

    private String meetingName;

    private Byte type;

    private Long rewardMoney;

    private Integer servDura;

    private Byte closeType;

    private Date openTime;

    private Date closeTime;

    private Double dura;

    public String getHistId() {
        return histId;
    }

    public void setHistId(String histId) {
        this.histId = histId == null ? null : histId.trim();
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName == null ? null : meetingName.trim();
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Long getRewardMoney() {
        return rewardMoney;
    }

    public void setRewardMoney(Long rewardMoney) {
        this.rewardMoney = rewardMoney;
    }

    public Integer getServDura() {
        return servDura;
    }

    public void setServDura(Integer servDura) {
        this.servDura = servDura;
    }

    public Byte getCloseType() {
        return closeType;
    }

    public void setCloseType(Byte closeType) {
        this.closeType = closeType;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public Double getDura() {
        return dura;
    }

    public void setDura(Double dura) {
        this.dura = dura;
    }
}
