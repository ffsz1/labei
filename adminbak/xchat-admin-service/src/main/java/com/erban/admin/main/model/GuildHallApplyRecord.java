package com.erban.admin.main.model;
import java.util.Date;

public class GuildHallApplyRecord {
    private Long id;
    //厅id
    private Long hallId;
    //用户uid
    private Long uid;
    //申请理由
    private String reason;
    //类型：0（申请加入），1（申请退出），2（逐出）
    private Integer type;
    //审核状态：0（待审核），1（不通过），2（通过），3（已完成），4（已失效）
    private Integer status;
    //审核人
    private Long approverUid;

    private Date createTime;

    private Date updateTime;
    //审核时间
    private Date approveTime;

    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHallId() {
        return hallId;
    }

    public void setHallId(Long hallId) {
        this.hallId = hallId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getApproverUid() {
        return approverUid;
    }

    public void setApproverUid(Long approverUid) {
        this.approverUid = approverUid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(Date approveTime) {
        this.approveTime = approveTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}