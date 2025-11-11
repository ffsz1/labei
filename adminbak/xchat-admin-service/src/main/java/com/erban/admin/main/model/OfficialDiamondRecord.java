package com.erban.admin.main.model;

import java.util.Date;

public class OfficialDiamondRecord {
    private Integer recordId;

    private Long uid;

    private Double diamondNum;

    private Byte type;

    private Date createTime;

    private Integer adminId;

    private String remark;

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

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "OfficialDiamondRecordExample{" +
                "recordId=" + recordId +
                ", uid=" + uid +
                ", diamondNum=" + diamondNum +
                ", type=" + type +
                ", createTime=" + createTime +
                ", adminId=" + adminId +
                ", remark='" + remark + '\'' +
                '}';
    }
}
