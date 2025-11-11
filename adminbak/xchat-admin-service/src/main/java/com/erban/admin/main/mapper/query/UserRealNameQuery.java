package com.erban.admin.main.mapper.query;

/**
 * @Description:
 * @Author: alwyn
 * @Date: 2018/12/30 13:29
 */
public class UserRealNameQuery {

    private Long erbanNo;
    private String idCardNo;
    private Byte auditStatus;
    private String startDate;
    private String endDate;

    public Long getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(Long erbanNo) {
        this.erbanNo = erbanNo;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public Byte getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Byte auditStatus) {
        this.auditStatus = auditStatus;
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

    @Override
    public String toString() {
        return "UserRealNameQuery{" +
                "erbanNo=" + erbanNo +
                ", idCardNo='" + idCardNo + '\'' +
                ", auditStatus=" + auditStatus +
                '}';
    }
}
