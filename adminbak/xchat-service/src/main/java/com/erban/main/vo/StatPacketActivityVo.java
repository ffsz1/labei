package com.erban.main.vo;

public class StatPacketActivityVo {
    private Long uid;

    private Integer shareCount;

    private Double packetNum;

    private Integer packetCount;

    private Integer registerCout;

    private Double chargeBonus;

    private Integer todayRegisterCount;

    public Integer getTodayRegisterCount() {
        return todayRegisterCount;
    }

    public void setTodayRegisterCount(Integer todayRegisterCount) {
        this.todayRegisterCount = todayRegisterCount;
    }

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
