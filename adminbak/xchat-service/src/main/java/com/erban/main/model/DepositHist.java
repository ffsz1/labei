package com.erban.main.model;

import java.util.Date;

public class DepositHist {
    private String did;

    private Long uid;

    private Long money;

    private Byte useType;

    private Byte curStatus;

    private Date createTime;

    private Date histTime;

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did == null ? null : did.trim();
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Byte getUseType() {
        return useType;
    }

    public void setUseType(Byte useType) {
        this.useType = useType;
    }

    public Byte getCurStatus() {
        return curStatus;
    }

    public void setCurStatus(Byte curStatus) {
        this.curStatus = curStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getHistTime() {
        return histTime;
    }

    public void setHistTime(Date histTime) {
        this.histTime = histTime;
    }
}
