package com.erban.admin.main.dto;

import java.util.Date;

/**
 * @Author: alwyn
 * @Description:
 * @Date: 2018/11/11 001116:31
 */
public class MakeFriendDTO {
    private Long uid;
    private Double source;
    private Date createDate;
    private Long erbanNo;
    private String nick;
    private String avatar;
    private Long initSource;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Double getSource() {
        return source;
    }

    public void setSource(Double source) {
        this.source = source;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getInitSource() {
        return initSource;
    }

    public void setInitSource(Long initSource) {
        this.initSource = initSource;
    }

    @Override
    public String toString() {
        return "MakeFriendVO{" +
                "uid=" + uid +
                ", source=" + source +
                ", createDate=" + createDate +
                ", erbanNo=" + erbanNo +
                ", nick='" + nick + '\'' +
                ", avatar='" + avatar + '\'' +
                ", initSource=" + initSource +
                '}';
    }
}
