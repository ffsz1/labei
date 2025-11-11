package com.erban.admin.main.vo;

import java.util.Date;

public class HomeHotManualRecommVo {

    private Integer recommId;

    private String erbanNo;

    private Integer seqNo;

    private Byte status;

    private Date startValidTime;

    private Date endValidTime;

    private Date createTime;

    private Byte isPurse;

    private String title;

    private Byte isPermitRoom;

    private Integer viewType;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Byte getIsPermitRoom() {
        return isPermitRoom;
    }

    public void setIsPermitRoom(Byte isPermitRoom) {
        this.isPermitRoom = isPermitRoom;
    }

    public Byte getIsPurse() {
        return isPurse;
    }

    public void setIsPurse(Byte isPurse) {
        this.isPurse = isPurse;
    }

    public Integer getRecommId() {
        return recommId;
    }

    public void setRecommId(Integer recommId) {
        this.recommId = recommId;
    }

    public String getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(String erbanNo) {
        this.erbanNo = erbanNo;
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

    public Integer getViewType() {
        return viewType;
    }

    public void setViewType(Integer viewType) {
        this.viewType = viewType;
    }

    @Override
    public String toString() {
        return "HomeHotManualRecommVo{" +
                "recommId=" + recommId +
                ", erbanNo='" + erbanNo + '\'' +
                ", seqNo=" + seqNo +
                ", status=" + status +
                ", startValidTime=" + startValidTime +
                ", endValidTime=" + endValidTime +
                ", createTime=" + createTime +
                ", isPurse=" + isPurse +
                ", title='" + title + '\'' +
                ", isPermitRoom=" + isPermitRoom +
                ", viewType=" + viewType +
                '}';
    }
}
