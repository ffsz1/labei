package com.tongdaxing.xchat_core.room.bean;

/**
 * Function:
 * Author: Edward on 2019/6/20
 */
public class FingerGuessingGameInfo {

    /**
     * recordId : 15
     * uid : 10241
     * erbanNo : 3862990
     * nick : Edward
     * avatar : https://pic.hnyueqiang.com/FtPDUcf119fbwCYsKiollKTspkTR?imageslim
     * subject : 发起猜拳
     * experienceLevel : 27
     * charmLevel : 1
     * giftId : 131
     * giftNum : 29
     * giftName : 口红
     * giftUrl : https://pic.hnyueqiang.com/Fgg6QVtoRJRuh1NKQeIQ1zCT1_3A?imageslim
     */

    private String recordId;
    private int uid;
    private int erbanNo;
    private String nick;
    private String avatar;
    private String subject;
    private int experienceLevel;
    private int charmLevel;
    private int giftId;
    private int giftNum;
    private String giftName;
    private String giftUrl;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(int erbanNo) {
        this.erbanNo = erbanNo;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(int experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public int getCharmLevel() {
        return charmLevel;
    }

    public void setCharmLevel(int charmLevel) {
        this.charmLevel = charmLevel;
    }

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }

    public int getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(int giftNum) {
        this.giftNum = giftNum;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getGiftUrl() {
        return giftUrl;
    }

    public void setGiftUrl(String giftUrl) {
        this.giftUrl = giftUrl;
    }

    @Override
    public String toString() {
        return "FingerGuessingGameInfo{" +
                "recordId='" + recordId + '\'' +
                ", uid=" + uid +
                ", erbanNo=" + erbanNo +
                ", nick='" + nick + '\'' +
                ", avatar='" + avatar + '\'' +
                ", subject='" + subject + '\'' +
                ", experienceLevel=" + experienceLevel +
                ", charmLevel=" + charmLevel +
                ", giftId=" + giftId +
                ", giftNum=" + giftNum +
                ", giftName='" + giftName + '\'' +
                ", giftUrl='" + giftUrl + '\'' +
                '}';
    }
}
