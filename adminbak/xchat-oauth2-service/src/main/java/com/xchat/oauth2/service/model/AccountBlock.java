package com.xchat.oauth2.service.model;

import java.util.Date;

public class AccountBlock {
    private Integer blockId;

    private Long uid;

    private Long erbanNo;

    private String phone;

    private String deviceId;

    private Byte blockType;

    private String blockIp;

    private Byte blockStatus;

    private Integer blockMinute;

    private Date blockStartTime;

    private Date blockEndTime;

    private String blockDesc;

    private Integer adminId;

    private Date createTime;

    public Integer getBlockId() {
        return blockId;
    }

    public void setBlockId(Integer blockId) {
        this.blockId = blockId;
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
        this.phone = phone == null ? null : phone.trim();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public Byte getBlockType() {
        return blockType;
    }

    public void setBlockType(Byte blockType) {
        this.blockType = blockType;
    }

    public String getBlockIp() {
        return blockIp;
    }

    public void setBlockIp(String blockIp) {
        this.blockIp = blockIp == null ? null : blockIp.trim();
    }

    public Byte getBlockStatus() {
        return blockStatus;
    }

    public void setBlockStatus(Byte blockStatus) {
        this.blockStatus = blockStatus;
    }

    public Integer getBlockMinute() {
        return blockMinute;
    }

    public void setBlockMinute(Integer blockMinute) {
        this.blockMinute = blockMinute;
    }

    public Date getBlockStartTime() {
        return blockStartTime;
    }

    public void setBlockStartTime(Date blockStartTime) {
        this.blockStartTime = blockStartTime;
    }

    public Date getBlockEndTime() {
        return blockEndTime;
    }

    public void setBlockEndTime(Date blockEndTime) {
        this.blockEndTime = blockEndTime;
    }

    public String getBlockDesc() {
        return blockDesc;
    }

    public void setBlockDesc(String blockDesc) {
        this.blockDesc = blockDesc == null ? null : blockDesc.trim();
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
