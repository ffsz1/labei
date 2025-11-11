package com.erban.main.model;

import java.util.Date;

public class UserReportRecord {
    private Long id;
    private Long uid;
    private Long reportUid;
    private Integer reportType;
    private Integer type;
    private String deviceId;
    private String phoneNo;
    private String ip;
    private Date createDate;
    private String remark;

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

    public Long getReportUid() {
        return reportUid;
    }

    public void setReportUid(Long reportUid) {
        this.reportUid = reportUid;
    }

    public Integer getReportType() {
        return reportType;
    }

    public void setReportType(Integer reportType) {
        this.reportType = reportType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "UserReportRecord{" +
                "id=" + id +
                ", uid=" + uid +
                ", reportUid=" + reportUid +
                ", reportType=" + reportType +
                ", type=" + type +
                ", deviceId='" + deviceId + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", ip='" + ip + '\'' +
                ", createDate=" + createDate +
                ", remark='" + remark + '\'' +
                '}';
    }
}
