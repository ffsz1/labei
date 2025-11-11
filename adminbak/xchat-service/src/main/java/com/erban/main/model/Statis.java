package com.erban.main.model;

import java.util.Date;

public class Statis {
    private String statisId;

    private Long uid;

    private String fromLoginUrl;

    private Date createTime;

    public String getStatisId() {
        return statisId;
    }

    public void setStatisId(String statisId) {
        this.statisId = statisId == null ? null : statisId.trim();
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getFromLoginUrl() {
        return fromLoginUrl;
    }

    public void setFromLoginUrl(String fromLoginUrl) {
        this.fromLoginUrl = fromLoginUrl == null ? null : fromLoginUrl.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
