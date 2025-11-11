package com.tongdaxing.xchat_core.redpacket.bean;

import java.io.Serializable;

/**
 * Created by ${Seven} on 2017/9/20.
 */

public class RedPacketInfo implements Serializable {
 /*   uid	90019
    shareCount	21  分享个数
    packetNum	30.23 红包金额
    packetCount	10 红包个数
    registerCout	12  邀请人数
    chargeBonus	87.21 分成奖励
    */
    private long uid;
    private int shareCount;
    private double packetNum;
    private int packetCount;
    private int registerCout;
    private double chargeBonus;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public double getPacketNum() {
        return packetNum;
    }

    public void setPacketNum(double packetNum) {
        this.packetNum = packetNum;
    }

    public int getPacketCount() {
        return packetCount;
    }

    public void setPacketCount(int packetCount) {
        this.packetCount = packetCount;
    }

    public int getRegisterCout() {
        return registerCout;
    }

    public void setRegisterCout(int registerCout) {
        this.registerCout = registerCout;
    }

    public double getChargeBonus() {
        return chargeBonus;
    }

    public void setChargeBonus(double chargeBonus) {
        this.chargeBonus = chargeBonus;
    }
}
