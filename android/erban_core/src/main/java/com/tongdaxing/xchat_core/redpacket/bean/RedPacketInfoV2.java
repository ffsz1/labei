package com.tongdaxing.xchat_core.redpacket.bean;

import java.io.Serializable;

/**
 * Created by chenran on 2017/10/4.
 */

public class RedPacketInfoV2 implements Serializable{
    //1：新人红包 2分享红包  3 邀请红包  4 分成红包 5提现
    private int type;

    private long uid;

    private double packetNum;

    private boolean needAlert;

    private String packetName;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public double getPacketNum() {
        return packetNum;
    }

    public void setPacketNum(double packetNum) {
        this.packetNum = packetNum;
    }

    public boolean isNeedAlert() {
        return needAlert;
    }

    public void setNeedAlert(boolean needAlert) {
        this.needAlert = needAlert;
    }

    public String getPacketName() {
        return packetName;
    }

    public void setPacketName(String packetName) {
        this.packetName = packetName;
    }
}
