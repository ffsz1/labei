package com.erban.main.dto;

import java.util.Date;

/**
 * @author chris
 * @Title:
 * @date 2018/9/13
 * @time 上午11:31
 */
public class WarningSmsRecordDTO {

    private Integer recordId;

    private Long uid;

    private Long erbanNo;

    private String nick;

    private Byte warningType;

    private Integer warningValue;

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
