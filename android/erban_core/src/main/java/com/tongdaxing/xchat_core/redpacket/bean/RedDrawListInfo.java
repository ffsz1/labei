package com.tongdaxing.xchat_core.redpacket.bean;

import java.io.Serializable;

/**
 * Created by chenran on 2017/12/4.
 */

public class RedDrawListInfo implements Serializable{
    private long uid;
    private String nick;
    private String avatar;
    private int packetNum;
    private long createTime;

    @Override
    public String toString() {
        return "RedDrawListInfo{" +
                "uid=" + uid +
                ", nick='" + nick + '\'' +
                ", avatar='" + avatar + '\'' +
                ", packetNum=" + packetNum +
                ", createTime=" + createTime +
                '}';
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

    public int getPacketNum() {
        return packetNum;
    }

    public void setPacketNum(int packetNum) {
        this.packetNum = packetNum;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
