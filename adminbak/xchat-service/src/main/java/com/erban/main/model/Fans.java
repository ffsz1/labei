package com.erban.main.model;

import java.util.Date;

public class Fans {
    private Long fanId;

    private Long likeUid;

    private Long likedUid;

    private Date createTime;

    public Long getFanId() {
        return fanId;
    }

    public void setFanId(Long fanId) {
        this.fanId = fanId;
    }

    public Long getLikeUid() {
        return likeUid;
    }

    public void setLikeUid(Long likeUid) {
        this.likeUid = likeUid;
    }

    public Long getLikedUid() {
        return likedUid;
    }

    public void setLikedUid(Long likedUid) {
        this.likedUid = likedUid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
