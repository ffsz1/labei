package com.erban.main.model;

import java.util.Date;

public class UserRealName {
    private Long uid;

    private String realName;

    private String idCardNo;

    private String idCardFront;

    private String idCardOpposite;

    private String idCardHandheld;

    private String phone;

    private Byte auditStatus;

    private Date createDate;

    private Date updateDate;

    private Integer adminId;

    private Boolean isFirst;
    
    private String remark;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo == null ? null : idCardNo.trim();
    }

    public String getIdCardFront() {
        return idCardFront;
    }

    public void setIdCardFront(String idCardFront) {
        this.idCardFront = idCardFront == null ? null : idCardFront.trim();
    }

    public String getIdCardOpposite() {
        return idCardOpposite;
    }

    public void setIdCardOpposite(String idCardOpposite) {
        this.idCardOpposite = idCardOpposite == null ? null : idCardOpposite.trim();
    }

    public String getIdCardHandheld() {
        return idCardHandheld;
    }

    public void setIdCardHandheld(String idCardHandheld) {
        this.idCardHandheld = idCardHandheld == null ? null : idCardHandheld.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Byte getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Byte auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Boolean getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(Boolean isFirst) {
        this.isFirst = isFirst;
    }

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

    @Override
    public String toString() {
        return "UserRealName{" +
                "uid=" + uid +
                ", realName='" + realName + '\'' +
                ", idCardNo='" + idCardNo + '\'' +
                ", idCardFront='" + idCardFront + '\'' +
                ", idCardOpposite='" + idCardOpposite + '\'' +
                ", idCardHandheld='" + idCardHandheld + '\'' +
                ", phone='" + phone + '\'' +
                ", auditStatus=" + auditStatus +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", adminId=" + adminId +
                ", isFirst=" + isFirst +
                ", remark='" + remark + '\'' +
                '}';
    }
}
