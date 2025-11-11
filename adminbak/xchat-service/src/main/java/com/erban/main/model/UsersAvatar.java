package com.erban.main.model;

import java.util.Date;

public class UsersAvatar {
    private Long uid;

    private String avatar;

    private Byte avatarStatus;

    private Date createTime;

    private Date updateTime;

    private Integer adminId;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar == null ? null : avatar.trim();
    }

    public Byte getAvatarStatus() {
        return avatarStatus;
    }

    public void setAvatarStatus(Byte avatarStatus) {
        this.avatarStatus = avatarStatus;
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

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }
}
