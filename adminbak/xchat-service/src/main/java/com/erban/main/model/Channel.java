package com.erban.main.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Channel {
    private Integer id;

    private String channel;

    private Byte auditOption;

    private Integer leftLevel;

    private String auditVersion;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel == null ? null : channel.trim();
    }

    public Byte getAuditOption() {
        return auditOption;
    }

    public void setAuditOption(Byte auditOption) {
        this.auditOption = auditOption;
    }

    public Integer getLeftLevel() {
        return leftLevel;
    }

    public void setLeftLevel(Integer leftLevel) {
        this.leftLevel = leftLevel;
    }

    public String getAuditVersion() {
        return auditVersion;
    }

    public void setAuditVersion(String auditVersion) {
        this.auditVersion = auditVersion == null ? null : auditVersion.trim();
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
