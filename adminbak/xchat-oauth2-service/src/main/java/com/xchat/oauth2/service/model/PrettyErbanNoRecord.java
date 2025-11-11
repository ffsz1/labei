package com.xchat.oauth2.service.model;

import java.util.Date;

public class PrettyErbanNoRecord {
    private Integer recordId;

    private Integer prettyId;

    private Long prettyErbanNo;

    private Long userErbanNo;

    private String prettyDesc;

    private Date createTime;

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Integer getPrettyId() {
        return prettyId;
    }

    public void setPrettyId(Integer prettyId) {
        this.prettyId = prettyId;
    }

    public Long getPrettyErbanNo() {
        return prettyErbanNo;
    }

    public void setPrettyErbanNo(Long prettyErbanNo) {
        this.prettyErbanNo = prettyErbanNo;
    }

    public Long getUserErbanNo() {
        return userErbanNo;
    }

    public void setUserErbanNo(Long userErbanNo) {
        this.userErbanNo = userErbanNo;
    }

    public String getPrettyDesc() {
        return prettyDesc;
    }

    public void setPrettyDesc(String prettyDesc) {
        this.prettyDesc = prettyDesc == null ? null : prettyDesc.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
