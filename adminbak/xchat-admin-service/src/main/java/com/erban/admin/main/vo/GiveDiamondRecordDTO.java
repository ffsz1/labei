package com.erban.admin.main.vo;

import java.util.Date;

public class GiveDiamondRecordDTO {
    private Integer recordId;

    private Long uid;

    private Double diamondNum;

    private Byte type;

    private Date createTime;

    private String remark;

    private String nick;

    private Long erbanNo;

    private String phone;

    private String avatar;

    private Integer adminId;

    private String adminName;

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Double getDiamondNum() {
        return diamondNum;
    }

    public void setDiamondNum(Double diamondNum) {
        this.diamondNum = diamondNum;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    @Override
    public String toString() {
        return "GiveDiamondRecordDTO{" +
                "recordId=" + recordId +
                ", uid=" + uid +
                ", diamondNum=" + diamondNum +
                ", type=" + type +
                ", createTime=" + createTime +
                ", remark='" + remark + '\'' +
                ", nick='" + nick + '\'' +
                ", erbanNo=" + erbanNo +
                ", phone='" + phone + '\'' +
                ", avatar='" + avatar + '\'' +
                ", adminId=" + adminId +
                ", adminName='" + adminName + '\'' +
                '}';
    }
}
