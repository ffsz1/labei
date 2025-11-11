package com.erban.main.param;

/**
 * 用户举报
 */
public class UserReportParam {

    private Long uid;
    private Long reportUid;
    private Integer reportType;
    private Integer type;
    private String deviceId;
    private String phoneNo;
    private String ip;

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

    @Override
    public String toString() {
        return "UserReportParam{" +
                "uid=" + uid +
                ", reportUid=" + reportUid +
                ", reportType=" + reportType +
                ", type=" + type +
                ", deviceId='" + deviceId + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }
}
