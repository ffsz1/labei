package com.erban.main.model;

import java.util.Date;

public class Valentine {
    private Integer id;

    private Integer maleUid;

    private Byte maleStatus;

    private Integer femaleUid;

    private Byte femaleStatus;

    private Byte valentineStatus;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMaleUid() {
        return maleUid;
    }

    public void setMaleUid(Integer maleUid) {
        this.maleUid = maleUid;
    }

    public Byte getMaleStatus() {
        return maleStatus;
    }

    public void setMaleStatus(Byte maleStatus) {
        this.maleStatus = maleStatus;
    }

    public Integer getFemaleUid() {
        return femaleUid;
    }

    public void setFemaleUid(Integer femaleUid) {
        this.femaleUid = femaleUid;
    }

    public Byte getFemaleStatus() {
        return femaleStatus;
    }

    public void setFemaleStatus(Byte femaleStatus) {
        this.femaleStatus = femaleStatus;
    }

    public Byte getValentineStatus() {
        return valentineStatus;
    }

    public void setValentineStatus(Byte valentineStatus) {
        this.valentineStatus = valentineStatus;
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
}
