package com.erban.admin.main.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.util.Date;

public class HeadwearRecordDTO {
    private Long recordId;

    private Long uid;

    @Excel(name = "拉贝号", orderNum = "0")
    private Long erbanNo;

    private Long headwearId;

    @Excel(name = "头饰名称", orderNum = "1")
    private String headwearName;

    @Excel(name = "有效天数", orderNum = "3")
    private Integer headwearDate;

    private Byte type;

    @Excel(name = "发放时间", exportFormat = "yyyy-MM-dd HH:mm:ss", orderNum = "2")
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
        return "HeadwearRecord{" +
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
