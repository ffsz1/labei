package com.erban.main.model;

import java.util.Date;

public class RoomSensitiveWords {
    private Integer id;
    private String sensitiveWords;
    private Integer adminId;
    private Date createTime;
    private Byte type;

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

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "RoomSensitiveWords{" +
                "id=" + id +
                ", sensitiveWords='" + sensitiveWords + '\'' +
                ", adminId=" + adminId +
                ", createTime=" + createTime +
                ", type=" + type +
                '}';
    }
}
