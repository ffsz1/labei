package com.erban.main.model;

import java.util.Date;

public class HomeHotManualRecomm {
    private Integer recommId;

    private Long uid;

    private Integer seqNo;

    private Byte status;

    private Date startValidTime;

    private Date endValidTime;

    private Date createTime;

    private Byte viewType;

    public Integer getRecommId() {
        return recommId;
    }

    public void setRecommId(Integer recommId) {
        this.recommId = recommId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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

    public Byte getViewType() {
        return viewType;
    }

    public void setViewType(Byte viewType) {
        this.viewType = viewType;
    }
}
