package com.erban.main.model;

import java.util.Date;

public class UserGuessNameRecord {

    private Long id;
    private Long uid;
    private String guessName;
    private Date createDate;

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

    public String getGuessName() {
        return guessName;
    }

    public void setGuessName(String guessName) {
        this.guessName = guessName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "UserGuessNameRecord{" +
                "id=" + id +
                ", uid=" + uid +
                ", guessName='" + guessName + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
