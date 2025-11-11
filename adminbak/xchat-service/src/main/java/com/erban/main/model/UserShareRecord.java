package com.erban.main.model;

import java.util.Date;

public class UserShareRecord {
    private String shareId;

    private Long uid;

    private Byte shareType;

    private Integer sharePageId;

    private Long targetUid;

    private Date createTime;

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId == null ? null : shareId.trim();
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Byte getShareType() {
        return shareType;
    }

    public void setShareType(Byte shareType) {
        this.shareType = shareType;
    }

    public Integer getSharePageId() {
        return sharePageId;
    }

    public void setSharePageId(Integer sharePageId) {
        this.sharePageId = sharePageId;
    }

    public Long getTargetUid() {
        return targetUid;
    }

    public void setTargetUid(Long targetUid) {
        this.targetUid = targetUid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
