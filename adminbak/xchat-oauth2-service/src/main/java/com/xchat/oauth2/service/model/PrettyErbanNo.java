package com.xchat.oauth2.service.model;

import java.util.Date;

public class PrettyErbanNo {
    private Integer prettyId;

    private Long prettyErbanNo;

    private Byte status;

    private Byte useFor;

    private Long userUid;

    private Long userErbanNo;

    private String prettyDesc;

    private Date startValidTime;

    private Date endValidTime;

    private Date createTime;

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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getUseFor() {
        return useFor;
    }

    public void setUseFor(Byte useFor) {
        this.useFor = useFor;
    }

    public Long getUserUid() {
        return userUid;
    }

    public void setUserUid(Long userUid) {
        this.userUid = userUid;
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

    public Date getStartValidTime() {
        return startValidTime;
    }

    public void setStartValidTime(Date startValidTime) {
        this.startValidTime = startValidTime;
    }

    public Date getEndValidTime() {
        return endValidTime;
    }

    public void setEndValidTime(Date endValidTime) {
        this.endValidTime = endValidTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
