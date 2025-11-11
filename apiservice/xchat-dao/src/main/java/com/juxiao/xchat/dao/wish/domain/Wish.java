package com.juxiao.xchat.dao.wish.domain;

import java.util.Date;

public class Wish {

    private Long uid;

    private String remarks; //补充说明
    private String viceUrl; //声音url
    private Integer audioDura;//声音时长
    private Date createTime; //创建时间
    private Date updateTime; //修改时间


    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }


    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getViceUrl() {
        return viceUrl;
    }

    public void setViceUrl(String viceUrl) {
        this.viceUrl = viceUrl;
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

    public Integer getAudioDura() {
        return audioDura;
    }

    public void setAudioDura(Integer audioDura) {
        this.audioDura = audioDura;
    }
}
