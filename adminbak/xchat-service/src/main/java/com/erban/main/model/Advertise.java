package com.erban.main.model;

import java.util.Date;

public class Advertise {
    private Integer advId;

    private String advName;

    private String advIcon;

    private Byte skipType;

    private String skipUri;

    private Integer seqNo;

    private Byte advStatus;

    private Date startTime;

    private Date endTime;

    private Date createTime;

    public Integer getAdvId() {
        return advId;
    }

    public void setAdvId(Integer advId) {
        this.advId = advId;
    }

    public String getAdvName() {
        return advName;
    }

    public void setAdvName(String advName) {
        this.advName = advName == null ? null : advName.trim();
    }

    public String getAdvIcon() {
        return advIcon;
    }

    public void setAdvIcon(String advIcon) {
        this.advIcon = advIcon == null ? null : advIcon.trim();
    }

    public Byte getSkipType() {
        return skipType;
    }

    public void setSkipType(Byte skipType) {
        this.skipType = skipType;
    }

    public String getSkipUri() {
        return skipUri;
    }

    public void setSkipUri(String skipUri) {
        this.skipUri = skipUri == null ? null : skipUri.trim();
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Byte getAdvStatus() {
        return advStatus;
    }

    public void setAdvStatus(Byte advStatus) {
        this.advStatus = advStatus;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
