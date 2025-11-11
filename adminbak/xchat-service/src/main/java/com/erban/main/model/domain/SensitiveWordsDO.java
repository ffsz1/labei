package com.erban.main.model.domain;

import java.util.Date;

public class SensitiveWordsDO {

    private Integer id;
    private String sensitiveWords;
    private Integer adminId;
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSensitiveWords() {
        return sensitiveWords;
    }

    public void setSensitiveWords(String sensitiveWords) {
        this.sensitiveWords = sensitiveWords;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SensitiveWordsDO{" +
                "id=" + id +
                ", sensitiveWords='" + sensitiveWords + '\'' +
                ", adminId=" + adminId +
                ", createTime=" + createTime +
                '}';
    }
}
