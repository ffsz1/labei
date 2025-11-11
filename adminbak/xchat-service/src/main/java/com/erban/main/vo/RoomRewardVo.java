package com.erban.main.vo;

import java.util.Date;

/**
 * Created by liuguofu on 2017/5/26.
 */
public class RoomRewardVo {
    private String rewardId;

    private Long uid;

    private Long rewardMoney;

    private Integer servDura;

    public Integer getServDura() {
        return servDura;
    }

    public void setServDura(Integer servDura) {
        this.servDura = servDura;
    }

    public String getRewardId() {
        return rewardId;
    }

    public void setRewardId(String rewardId) {
        this.rewardId = rewardId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getRewardMoney() {
        return rewardMoney;
    }

    public void setRewardMoney(Long rewardMoney) {
        this.rewardMoney = rewardMoney;
    }
}
