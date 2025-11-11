package com.tongdaxing.xchat_core.bean;

public class RankingInfo {

    /**
     * uid : 1001818
     * erbanNo : 1111111
     * avatar : https://pic.hulelive.com/FnMziuem9egbI5JOzqWLUtKaPojp?imageslim
     * nick : My.赌神白
     * gender : 1
     * totalNum : 56761
     * experLevel : 36
     * charmLevel : 31
     */

    private long uid;
    private String erbanNo;
    private String avatar;
    private String nick;
    private int gender;
    private double totalNum;
    private double distance;//距离上一级差值
    private int experLevel;
    private int charmLevel;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(String erbanNo) {
        this.erbanNo = erbanNo;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public double getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(long totalNum) {
        this.totalNum = totalNum;
    }

    public int getExperLevel() {
        return experLevel;
    }

    public void setExperLevel(int experLevel) {
        this.experLevel = experLevel;
    }

    public int getCharmLevel() {
        return charmLevel;
    }

    public void setCharmLevel(int charmLevel) {
        this.charmLevel = charmLevel;
    }

    @Override
    public String toString() {
        return "RankingInfo{" +
                "uid=" + uid +
                ", erbanNo='" + erbanNo + '\'' +
                ", avatar='" + avatar + '\'' +
                ", nick='" + nick + '\'' +
                ", gender=" + gender +
                ", totalNum=" + totalNum +
                ", distance=" + distance +
                ", experLevel=" + experLevel +
                ", charmLevel=" + charmLevel +
                '}';
    }
}
