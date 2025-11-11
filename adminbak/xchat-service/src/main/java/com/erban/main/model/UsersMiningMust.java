package com.erban.main.model;

import java.util.Date;

public class UsersMiningMust {
    private Long id;

    private Long uid;

    private Integer status;

    private Date createTime;

    private Integer giftId;

    private Long inputGold;

    private Long outputGold;

    private Double rate;

    private Integer num;

    private Integer adminId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getGiftId() {
        return giftId;
    }

    public void setGiftId(Integer giftId) {
        this.giftId = giftId;
    }

    public Long getInputGold() {
        return inputGold;
    }

    public void setInputGold(Long inputGold) {
        this.inputGold = inputGold;
    }

    public Long getOutputGold() {
        return outputGold;
    }

    public void setOutputGold(Long outputGold) {
        this.outputGold = outputGold;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    @Override
    public String toString() {
        return "UsersMiningMust{" +
                "id=" + id +
                ", uid=" + uid +
                ", status=" + status +
                ", createTime=" + createTime +
                ", giftId=" + giftId +
                ", inputGold=" + inputGold +
                ", outputGold=" + outputGold +
                ", rate=" + rate +
                ", num=" + num +
                ", adminId=" + adminId +
                '}';
    }
}
