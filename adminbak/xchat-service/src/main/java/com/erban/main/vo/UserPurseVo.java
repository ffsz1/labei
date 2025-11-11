package com.erban.main.vo;

import java.util.Date;

public class UserPurseVo {
    private Long uid;
    private Long goldNum;
    private Long chargeGoldNum;
    private Long nobleGoldNum;
    private Double diamondNum;
    private Long depositNum;
    private Date firstRechargeTime;
    private String drawMsg;
    private String drawUrl;

    public String getDrawMsg() {
        return drawMsg;
    }

    public void setDrawMsg(String drawMsg) {
        this.drawMsg = drawMsg;
    }

    public String getDrawUrl() {
        return drawUrl;
    }

    public void setDrawUrl(String drawUrl) {
        this.drawUrl = drawUrl;
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

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getDepositNum() {
        return depositNum;
    }

    public void setDepositNum(Long depositNum) {
        this.depositNum = depositNum;
    }

    public Long getGoldNum() {
        return goldNum;
    }

    public void setGoldNum(Long goldNum) {
        this.goldNum = goldNum;
    }

    public Double getDiamondNum() {
        return diamondNum;
    }

    public void setDiamondNum(Double diamondNum) {
        this.diamondNum = diamondNum;
    }

    public Date getFirstRechargeTime() {
        return firstRechargeTime;
    }

    public void setFirstRechargeTime(Date firstRechargeTime) {
        this.firstRechargeTime = firstRechargeTime;
    }
}
