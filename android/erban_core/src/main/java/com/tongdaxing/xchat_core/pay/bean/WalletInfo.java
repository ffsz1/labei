package com.tongdaxing.xchat_core.pay.bean;

import java.io.Serializable;

/**
 * @author Administrator
 * @date 2017/7/20 0020
 */

public class WalletInfo implements Serializable {
    /**
     * "uid": 900184,
     * "goldNum": 0,//金币数量
     * "diamondNum": 0, //钻石数量
     * "depositNum": 0//预扣款（押金）
     */
    public long uid;
    public double goldNum;
    public long conchNum;
    public double diamondNum;
    public int depositNum;
    public int amount;

    public double getDiamondNum() {
        return diamondNum;
    }

    public void setDiamondNum(double diamondNum) {
        this.diamondNum = diamondNum;
    }

    public long getConchNum() {
        return conchNum;
    }

    public void setConchNum(long conchNum) {
        this.conchNum = conchNum;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public double getGoldNum() {
        return goldNum;
    }

    public void setGoldNum(double goldNum) {
        this.goldNum = goldNum;
    }


    public int getDepositNum() {
        return depositNum;
    }

    public void setDepositNum(int depositNum) {
        this.depositNum = depositNum;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "WalletInfo{" +
                "uid=" + uid +
                ", goldNum=" + goldNum +
                ", conchNum=" + conchNum +
                ", diamondNum=" + diamondNum +
                ", depositNum=" + depositNum +
                ", amount=" + amount +
                '}';
    }
}
