package com.erban.admin.main.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author laizhilong
 * @Title:
 * @Package com.erban.admin.main.model
 * @date 2018/8/8
 * @time 17:38
 */
public class AccountBanned implements Serializable {
    private Integer id;

    /**
     * UID
     */
    private Long uid;

    /**
     * 耳伴号
     */
    private Long erbanNo;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 设备号
     */
    private String deviceId;

    /**
     * 封禁类型,1.房间禁言,2.私聊禁言,3.广播禁言
     */
    private String bannedType;

    /**
     * 封禁ip
     */
    private String bannedIp;

    /**
     * 封禁状态1封禁中，2封禁结束（删除）
     */
    private Byte bannedStatus;

    /**
     * 封禁时长（单位：分）
     */
    private Integer bannedMinute;

    /**
     * 封禁开始时间
     */
    private Date bannedStartTime;

    /**
     * 封禁结束时间
     */
    private Date bannedEndTime;

    /**
     * 封禁原因描述
     */
    private String bannedDesc;

    /**
     * 操作管理员
     */
    private Integer adminId;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getBannedType() {
        return bannedType;
    }

    public void setBannedType(String bannedType) {
        this.bannedType = bannedType;
    }

    public String getBannedIp() {
        return bannedIp;
    }

    public void setBannedIp(String bannedIp) {
        this.bannedIp = bannedIp;
    }

    public Byte getBannedStatus() {
        return bannedStatus;
    }

    public void setBannedStatus(Byte bannedStatus) {
        this.bannedStatus = bannedStatus;
    }

    public Integer getBannedMinute() {
        return bannedMinute;
    }

    public void setBannedMinute(Integer bannedMinute) {
        this.bannedMinute = bannedMinute;
    }

    public Date getBannedStartTime() {
        return bannedStartTime;
    }

    public void setBannedStartTime(Date bannedStartTime) {
        this.bannedStartTime = bannedStartTime;
    }

    public Date getBannedEndTime() {
        return bannedEndTime;
    }

    public void setBannedEndTime(Date bannedEndTime) {
        this.bannedEndTime = bannedEndTime;
    }

    public String getBannedDesc() {
        return bannedDesc;
    }

    public void setBannedDesc(String bannedDesc) {
        this.bannedDesc = bannedDesc;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
