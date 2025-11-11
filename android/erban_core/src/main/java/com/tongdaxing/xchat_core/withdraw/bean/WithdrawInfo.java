package com.tongdaxing.xchat_core.withdraw.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/25.
 */

public class WithdrawInfo implements Serializable {

    public long uid;
    //    public String alipayAccount;
    public double diamondNum;
    //    public String alipayAccountName;
    public boolean isNotBoundPhone;
    public int withDrawType;//1微信2支付宝
    //    public int payment;
    public boolean hasWx;
//    public String wxNickName;
    //    public String wxPhone;
//    public String wxRealName;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

   /* public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }*/

    public double getDiamondNum() {
        return diamondNum;
    }

    public void setDiamondNum(double diamondNum) {
        this.diamondNum = diamondNum;
    }

    /*public String getAlipayAccountName() {
        return alipayAccountName;
    }

    public void setAlipayAccountName(String alipayAccountName) {
        this.alipayAccountName = alipayAccountName;
    }*/

    public boolean isNotBoundPhone() {
        return isNotBoundPhone;
    }

    public void setNotBoundPhone(boolean notBoundPhone) {
        isNotBoundPhone = notBoundPhone;
    }

//    public String getWxNickName() {
//        return wxNickName;
//    }
//
//    public void setWxNickName(String wxNickName) {
//        this.wxNickName = wxNickName;
//    }
//
//    public String getWxRealName() {
//        return wxRealName;
//    }
//
//    public void setWxRealName(String wxRealName) {
//        this.wxRealName = wxRealName;
//    }
    /*public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public String getWxPhone() {
        return wxPhone;
    }

    public void setWxPhone(String wxPhone) {
        this.wxPhone = wxPhone;
    }

    public boolean hasAlipay() {
        return StringUtils.isNotEmpty(alipayAccount) && !"null".equals(alipayAccount);
    }

    public boolean hasWx() {
       return StringUtils.isNotEmpty(wxPhone) && !"null".equals(wxRealName);
    }*/

    public int getWithDrawType() {
        return withDrawType;
    }

    public void setWithDrawType(int withDrawType) {
        this.withDrawType = withDrawType;
    }

    public boolean isHasWx() {
        return hasWx;
    }

    public void setHasWx(boolean hasWx) {
        this.hasWx = hasWx;
    }

    @Override
    public String toString() {
        return "WithdrawInfo{" +
                "uid=" + uid +
                ", diamondNum=" + diamondNum +
                ", isNotBoundPhone=" + isNotBoundPhone +
                ", withDrawType=" + withDrawType +
                ", hasWx=" + hasWx +
                '}';
    }

}
