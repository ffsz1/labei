package com.erban.main.vo;

public class RedPacketVo {

    private Long uid;

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public void setPacketNum(Double packetNum) {
        this.packetNum = packetNum;
    }

    public Long getUid() {

        return uid;
    }

    public Double getPacketNum() {

        return packetNum;
    }

    private Double packetNum;

}
