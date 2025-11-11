package com.tongdaxing.xchat_core.redpacket.bean;

import java.io.Serializable;

/**
 * Created by ${Seven} on 2017/9/27.
 */

public class WithdrawRedSucceedInfo implements Serializable {
    private long uid;
    private double packetNum;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public double getPacketNum() {
        return packetNum;
    }

    public void setPacketNum(int packetNum) {
        this.packetNum = packetNum;
    }
}
