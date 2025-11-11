package com.erban.admin.main.service.room.bo;

public class HotManualRuleBO {

    private Long id;
    private Long erbanNo;
    private String startDate;
    private String endDate;
    private String weekDays;
    private String status;
    private Integer seqNo;
    private Integer viewType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return "HotManualRuleBO{" +
                "id=" + id +
                ", erbanNo=" + erbanNo +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", weekDays='" + weekDays + '\'' +
                ", status='" + status + '\'' +
                ", seqNo=" + seqNo +
                ", viewType=" + viewType +
                '}';
    }
}
