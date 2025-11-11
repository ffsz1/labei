package com.erban.main.model.vo;

import java.util.Date;

public class GuessNameRecordDTO {
    private Long id;
    private Long uid;
    private String guessName;
    private Date createDate;
    private Long erbanNo;
    private String nick;

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

    @Override
    public String toString() {
        return "GuessNameRecordDTO{" +
                "id=" + id +
                ", uid=" + uid +
                ", guessName='" + guessName + '\'' +
                ", createDate=" + createDate +
                ", erbanNo=" + erbanNo +
                ", nick='" + nick + '\'' +
                '}';
    }
}
