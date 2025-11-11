package com.erban.main.vo;

/**
 * Created by liuguofu on 2017/9/19.
 */
public class StatPacketDataVo {
    private Long uid;

    private Integer shareCount;

    private Double packetNum;

    private Integer packetCount;

    private Integer registerCout;

    private Double chargeBonus;

    public Long getUid() {
        return uid;
    }

    public Double getPacketNum() {
        return packetNum;
    }

    public void setPacketNum(Double packetNum) {
        this.packetNum = packetNum;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    public Integer getPacketCount() {
        return packetCount;
    }

    public void setPacketCount(Integer packetCount) {
        this.packetCount = packetCount;
    }

    public Integer getRegisterCout() {
        return registerCout;
    }

    public void setRegisterCout(Integer registerCout) {
        this.registerCout = registerCout;
    }

    public Double getChargeBonus() {
        return chargeBonus;
    }

    public void setChargeBonus(Double chargeBonus) {
        this.chargeBonus = chargeBonus;
    }
}
