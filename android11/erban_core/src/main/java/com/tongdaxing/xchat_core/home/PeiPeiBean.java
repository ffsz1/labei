package com.tongdaxing.xchat_core.home;

import android.os.Parcel;
import android.os.Parcelable;

public class PeiPeiBean {
    private long uid;
    private String avatar;
    private int gender;
    private String nick;
    private int glamour;
    private int voiceDuration;
    private String userVoice;
    private long signature;
    private String userDescription;
    private boolean isFirstCharge;//false:新晋;true:非新晋
    private int experLevel;
    private int roomState;

    public PeiPeiBean() {
    }

    public boolean isFirstCharge() {
        return isFirstCharge;
    }

    public void setFirstCharge(boolean firstCharge) {
        isFirstCharge = firstCharge;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getGlamour() {
        return glamour;
    }

    public void setGlamour(int glamour) {
        this.glamour = glamour;
    }

    public int getVoiceDuration() {
        return voiceDuration;
    }

    public void setVoiceDuration(int voiceDuration) {
        this.voiceDuration = voiceDuration;
    }

    public String getUserVoice() {
        return userVoice;
    }

    public void setUserVoice(String userVoice) {
        this.userVoice = userVoice;
    }

    public long getSignature() {
        return signature;
    }

    public void setSignature(long signature) {
        this.signature = signature;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public int getExperLevel() {
        return experLevel;
    }

    public void setExperLevel(int experLevel) {
        this.experLevel = experLevel;
    }

    public int getRoomState() {
        return roomState;
    }

    public void setRoomState(int roomState) {
        this.roomState = roomState;
    }
}
