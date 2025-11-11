package com.erban.main.model;

import java.util.Date;

public class NobleZip {
    private Integer id;

    private String zipUrl;

    private Integer version;

    private Byte status;

    private Date createTime;

    private String resConf;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getZipUrl() {
        return zipUrl;
    }

    public void setZipUrl(String zipUrl) {
        this.zipUrl = zipUrl == null ? null : zipUrl.trim();
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getResConf() {
        return resConf;
    }

    public void setResConf(String resConf) {
        this.resConf = resConf == null ? null : resConf.trim();
    }
}
