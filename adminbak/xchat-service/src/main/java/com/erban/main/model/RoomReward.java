package com.erban.main.model;

import java.util.Date;

public class RoomReward {
    private String rewardId;

    private Long uid;

    private Long rewardMoney;

    private Integer servDura;

    private Byte rewardStatus;

    private Date createTime;

    private Date finishTime;

    public String getRewardId() {
        return rewardId;
    }

    public void setRewardId(String rewardId) {
        this.rewardId = rewardId == null ? null : rewardId.trim();
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

    public Integer getServDura() {
        return servDura;
    }

    public void setServDura(Integer servDura) {
        this.servDura = servDura;
    }

    public Byte getRewardStatus() {
        return rewardStatus;
    }

    public void setRewardStatus(Byte rewardStatus) {
        this.rewardStatus = rewardStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }
}
