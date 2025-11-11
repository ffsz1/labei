package com.tongdaxing.xchat_core.withdraw.bean;

import java.io.Serializable;

/**
 * 钻石实体
 * Created by Administrator on 2017/7/24.
 */

public class WithdrwaListInfo implements Serializable{
    /*
    cashProdId：1 //id
    cashProdName: 10000钻=￥100 //列表名称
    diamondNum:10000 //钻石数量
    cashNum:100 //对应金额
    seqNo:1 //排序
     */
    public int cashProdId;
    public String cashProdName;
    private boolean isWd = false;//是否可以提现

    public double getDiamondNum() {
        return diamondNum;
    }

    public void setDiamondNum(double diamondNum) {
        this.diamondNum = diamondNum;
    }

    public double diamondNum;
    public int cashNum;

    public boolean isSelected;


    public int getCashProdId() {
        return cashProdId;
    }

    public void setCashProdId(int cashProdId) {
        this.cashProdId = cashProdId;
    }

    public String getCashProdName() {
        return cashProdName;
    }

    public void setCashProdName(String cashProdName) {
        this.cashProdName = cashProdName;
    }



    public int getCashNum() {
        return cashNum;
    }

    public void setCashNum(int cashNum) {
        this.cashNum = cashNum;
    }

    public boolean isWd() {
        return isWd;
    }

    public void setWd(boolean wd) {
        isWd = wd;
    }
}
