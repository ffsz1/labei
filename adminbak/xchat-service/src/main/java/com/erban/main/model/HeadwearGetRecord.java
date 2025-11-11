package com.erban.main.model;

import java.util.Date;

public class HeadwearGetRecord {
    private Long recordId;

    private Long uid;

    private Long erbanNo;

    private Long headwearId;

    private String headwearName;

    private Integer headwearDate;

    private Byte type;

    private Date createTime;

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
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

    public Long getHeadwearId() {
        return headwearId;
    }

    public void setHeadwearId(Long headwearId) {
        this.headwearId = headwearId;
    }

    public String getHeadwearName() {
        return headwearName;
    }

    public void setHeadwearName(String headwearName) {
        this.headwearName = headwearName;
    }

    public Integer getHeadwearDate() {
        return headwearDate;
    }

    public void setHeadwearDate(Integer headwearDate) {
        this.headwearDate = headwearDate;
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

    @Override
    public String toString() {
        return "HeadwearGetRecord{" +
                "recordId=" + recordId +
                ", uid=" + uid +
                ", erbanNo=" + erbanNo +
                ", headwearId=" + headwearId +
                ", headwearName='" + headwearName + '\'' +
                ", headwearDate=" + headwearDate +
                ", type=" + type +
                ", createTime=" + createTime +
                '}';
    }
}
