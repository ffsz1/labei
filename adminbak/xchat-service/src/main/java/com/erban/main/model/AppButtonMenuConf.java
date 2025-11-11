package com.erban.main.model;

import java.util.Date;

public class AppButtonMenuConf {
    private Integer confId;

    private Byte confLoc;

    private Byte confType;

    private Byte confStatus;

    private Date createTime;

    public Integer getConfId() {
        return confId;
    }

    public void setConfId(Integer confId) {
        this.confId = confId;
    }

    public Byte getConfLoc() {
        return confLoc;
    }

    public void setConfLoc(Byte confLoc) {
        this.confLoc = confLoc;
    }

    public Byte getConfType() {
        return confType;
    }

    public void setConfType(Byte confType) {
        this.confType = confType;
    }

    public Byte getConfStatus() {
        return confStatus;
    }

    public void setConfStatus(Byte confStatus) {
        this.confStatus = confStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
