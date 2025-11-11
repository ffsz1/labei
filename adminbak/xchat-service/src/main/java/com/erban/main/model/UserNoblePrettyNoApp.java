package com.erban.main.model;

import java.util.Date;

public class UserNoblePrettyNoApp {
    private Integer recordId;

    private Long uid;

    private Long erbanNo;

    private Integer nobleId;

    private String nobleName;

    private Date createTime;

    private Long approveErbanNo;

    private Byte approveResult;

    private String approveDesc;

    private Date approveTime;

    private Integer adminId;

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

    public Long getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(Long erbanNo) {
        this.erbanNo = erbanNo;
    }

    public Integer getNobleId() {
        return nobleId;
    }

    public void setNobleId(Integer nobleId) {
        this.nobleId = nobleId;
    }

    public String getNobleName() {
        return nobleName;
    }

    public void setNobleName(String nobleName) {
        this.nobleName = nobleName == null ? null : nobleName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getApproveErbanNo() {
        return approveErbanNo;
    }

    public void setApproveErbanNo(Long approveErbanNo) {
        this.approveErbanNo = approveErbanNo;
    }

    public Byte getApproveResult() {
        return approveResult;
    }

    public void setApproveResult(Byte approveResult) {
        this.approveResult = approveResult;
    }

    public String getApproveDesc() {
        return approveDesc;
    }

    public void setApproveDesc(String approveDesc) {
        this.approveDesc = approveDesc == null ? null : approveDesc.trim();
    }

    public Date getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(Date approveTime) {
        this.approveTime = approveTime;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }
}
