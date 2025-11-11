package com.erban.admin.main.model;

import java.util.Date;

public class RoomSensitiveWords {
    private Integer id;

    /**
     * 敏感词
     */
    private String sensitiveWords;

    /**
     * 设置的管理员id
     */
    private Integer adminId;

    private Date createTime;

    /**
     * 类型 1 房间名 2 聊天
     */
    private Byte type;

    private String adminName;

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
        this.sensitiveWords = sensitiveWords == null ? null : sensitiveWords.trim();
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

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    @Override
    public String toString() {
        return "RoomSensitiveWords{" +
                "id=" + id +
                ", sensitiveWords='" + sensitiveWords + '\'' +
                ", adminId=" + adminId +
                ", createTime=" + createTime +
                ", type=" + type +
                ", adminName=" + adminName +
                '}';
    }
}
