package com.erban.main.model;

import java.util.Date;

public class UserBlacklist {
    private Integer blackId;

    private Long uid;

    private Long blacklistUid;

    private Byte status;

    private Date createTime;

    public Integer getBlackId() {
        return blackId;
    }

    public void setBlackId(Integer blackId) {
        this.blackId = blackId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getBlacklistUid() {
        return blacklistUid;
    }

    public void setBlacklistUid(Long blacklistUid) {
        this.blacklistUid = blacklistUid;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
