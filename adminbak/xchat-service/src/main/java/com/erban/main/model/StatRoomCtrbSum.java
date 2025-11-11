package com.erban.main.model;

import java.util.Date;

public class StatRoomCtrbSum {
    private Integer ctrbId;

    private Long uid;

    private Long ctrbUid;

    private Long sumGold;

    private Date createTime;

    private Date updateTime;

    public Integer getCtrbId() {
        return ctrbId;
    }

    public void setCtrbId(Integer ctrbId) {
        this.ctrbId = ctrbId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getCtrbUid() {
        return ctrbUid;
    }

    public void setCtrbUid(Long ctrbUid) {
        this.ctrbUid = ctrbUid;
    }

    public Long getSumGold() {
        return sumGold;
    }

    public void setSumGold(Long sumGold) {
        this.sumGold = sumGold;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
