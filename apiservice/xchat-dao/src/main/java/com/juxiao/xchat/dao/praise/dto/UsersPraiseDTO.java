package com.juxiao.xchat.dao.praise.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class UsersPraiseDTO {
    private Long id;
    private Long uid;
    private Long praisedUid;
    private Byte type;
    private String state;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    private String nick;
    private Byte gender;
    private Long erbanNo;
    private String avatar;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getPraisedUid() {
        return praisedUid;
    }

    public void setPraisedUid(Long praisedUid) {
        this.praisedUid = praisedUid;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Long getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(Long erbanNo) {
        this.erbanNo = erbanNo;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
