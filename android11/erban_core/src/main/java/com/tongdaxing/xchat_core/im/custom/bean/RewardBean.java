package com.tongdaxing.xchat_core.im.custom.bean;

import java.io.Serializable;

/**
 * Created by zhouxiangfeng on 2017/5/28.
 */

public class RewardBean implements Serializable {

    private String rewardId;

    private long uid;

    private String rewardMoney;

    private int servDura;

    public String getRewardId() {
        return rewardId;
    }

    public void setRewardId(String rewardId) {
        this.rewardId = rewardId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getRewardMoney() {
        return rewardMoney;
    }

    public void setRewardMoney(String rewardMoney) {
        this.rewardMoney = rewardMoney;
    }

    public int getServDura() {
        return servDura;
    }

    public void setServDura(int servDura) {
        this.servDura = servDura;
    }
}
