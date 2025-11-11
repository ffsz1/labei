package com.erban.admin.web.bo;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.util.Date;

/**
 * @author chris
 * @Title:
 * @date 2018/9/13
 * @time 下午7:55
 */
public class WarningSmsRecordExportBO {

    @Excel(name = "编号")
    private Integer recordId;
    @Excel(name = "uid")
    private Long uid;
    @Excel(name = "拉贝号")
    private Long erbanNo;
    @Excel(name = "昵称")
    private String nick;
    @Excel(name = "预警类型", replace = { "送礼物金额_1", "充值金额_2","充值次数_3" }, isImportField = "true_st")
    private Byte warningType;
    @Excel(name = "预警值")
    private Integer warningValue;
    @Excel(name = "创建时间", format = "yyyy-MM-dd hh:mm:ss", width = 20)
    private Date createTime;

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

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Byte getWarningType() {
        return warningType;
    }

    public void setWarningType(Byte warningType) {
        this.warningType = warningType;
    }

    public Integer getWarningValue() {
        return warningValue;
    }

    public void setWarningValue(Integer warningValue) {
        this.warningValue = warningValue;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "WarningSmsRecordDTO{" +
                "recordId=" + recordId +
                ", uid=" + uid +
                ", erbanNo=" + erbanNo +
                ", nick='" + nick + '\'' +
                ", warningType=" + warningType +
                ", warningValue=" + warningValue +
                ", createTime=" + createTime +
                '}';
    }
}
