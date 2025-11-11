package com.erban.main.model.dto;

import java.util.Date;

public class HotManualRuleDTO {

    private Long id;
    private Long uid;
    private Long erbanNo;
    private String startDate;
    private String endDate;
    private String weekDays;
    private String status;
    private Date createDate;
    private Boolean isPermitRoom;
    private String title;
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

    public Long getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(Long erbanNo) {
        this.erbanNo = erbanNo;
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

    public Boolean getIsPermitRoom() {
        return isPermitRoom;
    }

    public void setIsPermitRoom(Boolean isPermitRoom) {
        this.isPermitRoom = isPermitRoom;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Boolean getPermitRoom() {
        return isPermitRoom;
    }

    public void setPermitRoom(Boolean permitRoom) {
        isPermitRoom = permitRoom;
    }

    public Integer getViewType() {
        return viewType;
    }

    public void setViewType(Integer viewType) {
        this.viewType = viewType;
    }

    @Override
    public String toString() {
        return "HotManualRuleDTO{" +
                "id=" + id +
                ", uid=" + uid +
                ", erbanNo=" + erbanNo +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", weekDays='" + weekDays + '\'' +
                ", status='" + status + '\'' +
                ", createDate=" + createDate +
                ", isPermitRoom=" + isPermitRoom +
                ", title='" + title + '\'' +
                ", seqNo=" + seqNo +
                ", viewType=" + viewType +
                '}';
    }
}
