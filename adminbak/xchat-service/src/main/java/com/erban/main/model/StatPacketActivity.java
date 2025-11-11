package com.erban.main.model;

import java.util.Date;

public class StatPacketActivity {
    private Long uid;

    private Integer shareCount;

    private Integer sharePacketCount;

    private Date latestShareDate;

    private Integer packetCount;

    private Integer registerCout;

    private Integer todayRegisterCount;

    private Date latestRegisterDate;

    private Double chargeBonus;

    private Double todayChargeBonus;

    private Date latestChargeBonusDate;

    private Date createTime;

    private Date updateTime;

    public Long getUid() {
        return uid;
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

    public Integer getSharePacketCount() {
        return sharePacketCount;
    }

    public void setSharePacketCount(Integer sharePacketCount) {
        this.sharePacketCount = sharePacketCount;
    }

    public Date getLatestShareDate() {
        return latestShareDate;
    }

    public void setLatestShareDate(Date latestShareDate) {
        this.latestShareDate = latestShareDate;
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

    public Integer getTodayRegisterCount() {
        return todayRegisterCount;
    }

    public void setTodayRegisterCount(Integer todayRegisterCount) {
        this.todayRegisterCount = todayRegisterCount;
    }

    public Date getLatestRegisterDate() {
        return latestRegisterDate;
    }

    public void setLatestRegisterDate(Date latestRegisterDate) {
        this.latestRegisterDate = latestRegisterDate;
    }

    public Double getChargeBonus() {
        return chargeBonus;
    }

    public void setChargeBonus(Double chargeBonus) {
        this.chargeBonus = chargeBonus;
    }

    public Double getTodayChargeBonus() {
        return todayChargeBonus;
    }

    public void setTodayChargeBonus(Double todayChargeBonus) {
        this.todayChargeBonus = todayChargeBonus;
    }

    public Date getLatestChargeBonusDate() {
        return latestChargeBonusDate;
    }

    public void setLatestChargeBonusDate(Date latestChargeBonusDate) {
        this.latestChargeBonusDate = latestChargeBonusDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
