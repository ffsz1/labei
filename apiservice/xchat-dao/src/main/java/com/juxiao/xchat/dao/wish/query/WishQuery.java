package com.juxiao.xchat.dao.wish.query;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class WishQuery {

    private Long erbanNo;
    private String nick;
    private Byte gender;

    private String remarks; //补充说明

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;//创建时间


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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
