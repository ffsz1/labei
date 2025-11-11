package com.erban.main.model;

import java.util.Date;

public class StatPacketRegister {
    private String registerId;

    private Long uid;

    private Long registerUid;

    private Date createTime;

    public String getRegisterId() {
        return registerId;
    }

    public void setRegisterId(String registerId) {
        this.registerId = registerId == null ? null : registerId.trim();
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getRegisterUid() {
        return registerUid;
    }

    public void setRegisterUid(Long registerUid) {
        this.registerUid = registerUid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
