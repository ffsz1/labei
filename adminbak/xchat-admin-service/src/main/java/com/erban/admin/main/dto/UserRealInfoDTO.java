package com.erban.admin.main.dto;

import java.util.Date;

/**
 * @Description:
 * @Author: alwyn
 * @Date: 2018/12/30 13:28
 */
public class UserRealInfoDTO {
    private Long uid;

    private Long erbanNo;

    private String realName;

    private String idCardNo;

    private String idCardFront;

    private String idCardOpposite;

    private String idCardHandheld;

    private Byte auditStatus;

    private Date createDate;

    private Date updateDate;

    private String phone;

    private String optionName;

    private Integer adminId;
    
    private Integer isFirst;
    
    private String remark;

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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getIdCardFront() {
        return idCardFront;
    }

    public void setIdCardFront(String idCardFront) {
        this.idCardFront = idCardFront;
    }

    public String getIdCardOpposite() {
        return idCardOpposite;
    }

    public void setIdCardOpposite(String idCardOpposite) {
        this.idCardOpposite = idCardOpposite;
    }

    public String getIdCardHandheld() {
        return idCardHandheld;
    }

    public void setIdCardHandheld(String idCardHandheld) {
        this.idCardHandheld = idCardHandheld;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }
    

    public Integer getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(Integer isFirst) {
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
        return "UserRealInfoDTO{" +
                "uid=" + uid +
                ", realName='" + realName + '\'' +
                ", idCardNo='" + idCardNo + '\'' +
                ", idCardFront='" + idCardFront + '\'' +
                ", idCardOpposite='" + idCardOpposite + '\'' +
                ", idCardHandheld='" + idCardHandheld + '\'' +
                ", auditStatus=" + auditStatus +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", phone='" + phone + '\'' +
                ", isFirst='" + isFirst + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
