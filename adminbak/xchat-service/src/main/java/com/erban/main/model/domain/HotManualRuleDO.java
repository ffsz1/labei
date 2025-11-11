package com.erban.main.model.domain;

import java.util.Date;

public class HotManualRuleDO {

    private Long id;
    private Long uid;
    private String startDate;
    private String endDate;
    private String weekDays;
    private String status;
    private Date createDate;
    private Integer seqNo;
    private Integer viewType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(String weekDays) {
        this.weekDays = weekDays;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Integer getViewType() {
        return viewType;
    }

    public void setViewType(Integer viewType) {
        this.viewType = viewType;
    }

    @Override
    public String toString() {
        return "HotManualRuleDO{" +
                "id=" + id +
                ", uid=" + uid +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", weekDays='" + weekDays + '\'' +
                ", status='" + status + '\'' +
                ", createDate=" + createDate +
                ", seqNo=" + seqNo +
                ", viewType=" + viewType +
                '}';
    }
}
