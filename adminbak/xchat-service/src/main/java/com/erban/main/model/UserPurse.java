package com.erban.main.model;

import java.util.Date;

public class UserPurse {
    private Long uid;

    private Long chargeGoldNum;

    private Long nobleGoldNum;

    private Long goldNum;

    private Long conchNum;

    private Double diamondNum;

    private Long depositNum;

    private Boolean isFirstCharge;

    private Date firstRechargeTime;

    private Date updateTime;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getChargeGoldNum() {
        return chargeGoldNum;
    }

    public void setChargeGoldNum(Long chargeGoldNum) {
        this.chargeGoldNum = chargeGoldNum;
    }

    public Long getNobleGoldNum() {
        return nobleGoldNum;
    }

    public void setNobleGoldNum(Long nobleGoldNum) {
        this.nobleGoldNum = nobleGoldNum;
    }

    public Long getGoldNum() {
        return goldNum;
    }

    public void setGoldNum(Long goldNum) {
        this.goldNum = goldNum;
    }

    public Long getConchNum() {
        return conchNum;
    }

    public void setConchNum(Long conchNum) {
        this.conchNum = conchNum;
    }

    public Double getDiamondNum() {
        return diamondNum;
    }

    public void setDiamondNum(Double diamondNum) {
        this.diamondNum = diamondNum;
    }

    public Long getDepositNum() {
        return depositNum;
    }

    public void setDepositNum(Long depositNum) {
        this.depositNum = depositNum;
    }

    public Boolean getIsFirstCharge() {
        return isFirstCharge;
    }

    public void setIsFirstCharge(Boolean isFirstCharge) {
        this.isFirstCharge = isFirstCharge;
    }

    public Date getFirstRechargeTime() {
        return firstRechargeTime;
    }

    public void setFirstRechargeTime(Date firstRechargeTime) {
        this.firstRechargeTime = firstRechargeTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
