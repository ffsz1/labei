package com.erban.main.vo;

import java.util.Map;

/**
 * Created by liuguofu on 2017/9/19.
 */
public class StatPacketActRankVo {
    private Long uid;
    private String nick;
    private String avatar;
    private int seqNo;
    private double packetNum;


    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
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

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public double getPacketNum() {
        return packetNum;
    }

    public void setPacketNum(double packetNum) {
        this.packetNum = packetNum;
    }

}
