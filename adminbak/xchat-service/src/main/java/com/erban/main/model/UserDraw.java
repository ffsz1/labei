package com.erban.main.model;

import java.util.Date;

public class UserDraw {
    private Long uid;

    private Integer leftDrawNum;

    private Integer totalDrawNum;

    private Integer totalWinDrawNum;

    private Boolean isFirstShare;

    private Date createTime;

    private Date updateTime;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getLeftDrawNum() {
        return leftDrawNum;
    }

    public void setLeftDrawNum(Integer leftDrawNum) {
        this.leftDrawNum = leftDrawNum;
    }

    public Integer getTotalDrawNum() {
        return totalDrawNum;
    }

    public void setTotalDrawNum(Integer totalDrawNum) {
        this.totalDrawNum = totalDrawNum;
    }

    public Integer getTotalWinDrawNum() {
        return totalWinDrawNum;
    }

    public void setTotalWinDrawNum(Integer totalWinDrawNum) {
        this.totalWinDrawNum = totalWinDrawNum;
    }

    public Boolean getIsFirstShare() {
        return isFirstShare;
    }

    public void setIsFirstShare(Boolean isFirstShare) {
        this.isFirstShare = isFirstShare;
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
