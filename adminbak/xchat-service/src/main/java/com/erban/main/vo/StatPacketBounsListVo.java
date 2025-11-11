package com.erban.main.vo;

import java.util.Date;
import java.util.List;

public class StatPacketBounsListVo extends UserBasicVo{

    private String bounsId;
    private Long amount;
    private String lowerNick;
    private Double packetNum;
    private Date createTime;

    public String getLowerNick() {
        return lowerNick;
    }

    public void setLowerNick(String lowerNick) {
        this.lowerNick = lowerNick;
    }

    public String getBounsId() {
        return bounsId;
    }

    public void setBounsId(String bounsId) {
        this.bounsId = bounsId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Double getPacketNum() {
        return packetNum;
    }

    public void setPacketNum(Double packetNum) {
        this.packetNum = packetNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
