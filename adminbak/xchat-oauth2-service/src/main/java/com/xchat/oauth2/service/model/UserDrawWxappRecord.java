package com.xchat.oauth2.service.model;


import java.util.Date;

public class UserDrawWxappRecord {
    private Long uid;
    private Long targetUid;
    private Integer shareType;
    private Date shareDate;


    public UserDrawWxappRecord() {
    }

    public UserDrawWxappRecord(Long uid, Long targetUid, Integer shareType, Date shareDate) {
        this.uid = uid;
        this.targetUid = targetUid;
        this.shareType = shareType;
        this.shareDate = shareDate;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getTargetUid() {
        return targetUid;
    }

    public void setTargetUid(Long targetUid) {
        this.targetUid = targetUid;
    }

    public Integer getShareType() {
        return shareType;
    }

    public void setShareType(Integer shareType) {
        this.shareType = shareType;
    }

    public Date getShareDate() {
        return shareDate;
    }

    public void setShareDate(Date shareDate) {
        this.shareDate = shareDate;
    }
}
