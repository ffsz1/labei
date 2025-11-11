package com.tongdaxing.xchat_core.pay.bean;

import java.io.Serializable;

/**
 * Created by zhouxiangfeng on 2017/6/19.
 */

public class ChargeBean implements Serializable{

    /*
    channelType:1支付宝微信公众号充值3苹果充值
    "chargeProdId": 1,  //充值产品ID
            "prodName": "600金币",  //产品显示名称
            "money": 6,   //产品价格（单位：元）
            "giftGoldNum": 20,  //赠送金币数量
            "channel": "wx"  //充值渠道

     */
    public int chargeProdId;
    public String prodName;
    public int money;
    public int giftGoldNum;
    public int channel;
    public String prodDesc;

    public boolean isSelected;


    public ChargeBean(int money) {
        this.money = money;
    }


    public int getChargeProdId() {
        return chargeProdId;
    }

    public void setChargeProdId(int chargeProdId) {
        this.chargeProdId = chargeProdId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getGiftGoldNum() {
        return giftGoldNum;
    }

    public void setGiftGoldNum(int giftGoldNum) {
        this.giftGoldNum = giftGoldNum;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "ChargeBean{" +
                "chargeProdId=" + chargeProdId +
                ", prodName='" + prodName + '\'' +
                ", money=" + money +
                ", giftGoldNum=" + giftGoldNum +
                ", channel=" + channel +
                ", prodDesc='" + prodDesc + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}
