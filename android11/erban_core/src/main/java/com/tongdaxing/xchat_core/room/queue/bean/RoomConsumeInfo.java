package com.tongdaxing.xchat_core.room.queue.bean;

import java.io.Serializable;

/**
 * Created by chenran on 2017/10/2.
 */

public class RoomConsumeInfo implements Serializable{
    private long uid;
    private String nick;
    private String avatar;
    private int gender;
    private long ctrbUid;
    private long sumGold;
    //财富等级
    private int experLevel;
    //魅力等级

    public int getCharmLevel() {
        return charmLevel;
    }

    public void setCharmLevel(int charmLevel) {
        this.charmLevel = charmLevel;
    }

    private int charmLevel;

    public int getExperLevel() {
        return experLevel;
    }

    public void setExperLevel(int experLevel) {
        this.experLevel = experLevel;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public long getCtrbUid() {
        return ctrbUid;
    }

    public void setCtrbUid(long ctrbUid) {
        this.ctrbUid = ctrbUid;
    }

    public long getSumGold() {
        return sumGold;
    }

    public void setSumGold(long sumGold) {
        this.sumGold = sumGold;
    }

    @Override
    public String toString() {
        return "RoomConsumeInfo{" +
                "uid=" + uid +
                ", nick='" + nick + '\'' +
                ", avatar='" + avatar + '\'' +
                ", gender=" + gender +
                ", ctrbUid=" + ctrbUid +
                ", sumGold=" + sumGold +
                '}';
    }
}
