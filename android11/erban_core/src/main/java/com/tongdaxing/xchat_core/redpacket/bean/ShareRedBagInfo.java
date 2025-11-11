package com.tongdaxing.xchat_core.redpacket.bean;

import java.io.Serializable;

/**
 * Created by Seven on 2017/9/26.
 */

public class ShareRedBagInfo implements Serializable {
    private double packetNum;
    private long createTime;
    private boolean needAlert;

    public boolean isNeedAlert() {
        return needAlert;
    }

    public void setNeedAlert(boolean needAlert) {
        this.needAlert = needAlert;
    }

    public double getPacketNum() {
        return packetNum;
    }

    public void setPacketNum(double packetNum) {
        this.packetNum = packetNum;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ShareRedBagInfo{" +
                "packetNum=" + packetNum +
                ", createTime=" + createTime +
                ", needAlert=" + needAlert +
                '}';
    }
}
