package com.erban.main.model;

import java.util.Date;

public class NobleUsers {
    private Long uid;

    private Integer nobleId;

    private String nobleName;

    private Date expire;

    private Integer badgeId;

    private String badge;

    private Integer cardbgId;

    private String cardbg;

    private Integer zonebgId;

    private String zonebg;

    private Integer roomBackgroundId;

    private String roomBackground;

    private Integer micDecorateId;

    private String micDecorate;

    private Integer chatBubbleId;

    private String chatBubble;

    private Integer micHaloId;

    private String micHalo;

    private Byte enterHide;

    private Byte rankHide;

    private Long goodNum;

    private Byte recomCount;

    private Byte status;

    private Date openTime;

    private Date renewTime;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getNobleId() {
        return nobleId;
    }

    public void setNobleId(Integer nobleId) {
        this.nobleId = nobleId;
    }

    public String getNobleName() {
        return nobleName;
    }

    public void setNobleName(String nobleName) {
        this.nobleName = nobleName == null ? null : nobleName.trim();
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    public Integer getBadgeId() {
        return badgeId;
    }

    public void setBadgeId(Integer badgeId) {
        this.badgeId = badgeId;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge == null ? null : badge.trim();
    }

    public Integer getCardbgId() {
        return cardbgId;
    }

    public void setCardbgId(Integer cardbgId) {
        this.cardbgId = cardbgId;
    }

    public String getCardbg() {
        return cardbg;
    }

    public void setCardbg(String cardbg) {
        this.cardbg = cardbg == null ? null : cardbg.trim();
    }

    public Integer getZonebgId() {
        return zonebgId;
    }

    public void setZonebgId(Integer zonebgId) {
        this.zonebgId = zonebgId;
    }

    public String getZonebg() {
        return zonebg;
    }

    public void setZonebg(String zonebg) {
        this.zonebg = zonebg == null ? null : zonebg.trim();
    }

    public Integer getRoomBackgroundId() {
        return roomBackgroundId;
    }

    public void setRoomBackgroundId(Integer roomBackgroundId) {
        this.roomBackgroundId = roomBackgroundId;
    }

    public String getRoomBackground() {
        return roomBackground;
    }

    public void setRoomBackground(String roomBackground) {
        this.roomBackground = roomBackground == null ? null : roomBackground.trim();
    }

    public Integer getMicDecorateId() {
        return micDecorateId;
    }

    public void setMicDecorateId(Integer micDecorateId) {
        this.micDecorateId = micDecorateId;
    }

    public String getMicDecorate() {
        return micDecorate;
    }

    public void setMicDecorate(String micDecorate) {
        this.micDecorate = micDecorate == null ? null : micDecorate.trim();
    }

    public Integer getChatBubbleId() {
        return chatBubbleId;
    }

    public void setChatBubbleId(Integer chatBubbleId) {
        this.chatBubbleId = chatBubbleId;
    }

    public String getChatBubble() {
        return chatBubble;
    }

    public void setChatBubble(String chatBubble) {
        this.chatBubble = chatBubble == null ? null : chatBubble.trim();
    }

    public Integer getMicHaloId() {
        return micHaloId;
    }

    public void setMicHaloId(Integer micHaloId) {
        this.micHaloId = micHaloId;
    }

    public String getMicHalo() {
        return micHalo;
    }

    public void setMicHalo(String micHalo) {
        this.micHalo = micHalo == null ? null : micHalo.trim();
    }

    public Byte getEnterHide() {
        return enterHide;
    }

    public void setEnterHide(Byte enterHide) {
        this.enterHide = enterHide;
    }

    public Byte getRankHide() {
        return rankHide;
    }

    public void setRankHide(Byte rankHide) {
        this.rankHide = rankHide;
    }

    public Long getGoodNum() {
        return goodNum;
    }

    public void setGoodNum(Long goodNum) {
        this.goodNum = goodNum;
    }

    public Byte getRecomCount() {
        return recomCount;
    }

    public void setRecomCount(Byte recomCount) {
        this.recomCount = recomCount;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public Date getRenewTime() {
        return renewTime;
    }

    public void setRenewTime(Date renewTime) {
        this.renewTime = renewTime;
    }
}
