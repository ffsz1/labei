package com.erban.main.vo;

import java.util.Date;

public class ClockResultVo {
    private Integer clockResultId;
    private String clockDate;
    private Long uid;
    private Integer goldAmount;
    private Integer status;
    private String nick;
    private String avatar;

    public Integer getClockResultId() {
        return clockResultId;
    }

    public void setClockResultId(Integer clockResultId) {
        this.clockResultId = clockResultId;
    }

    public String getClockDate() {
        return clockDate;
    }

    public void setClockDate(String clockDate) {
        this.clockDate = clockDate;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getGoldAmount() {
        return goldAmount;
    }

    public void setGoldAmount(Integer goldAmount) {
        this.goldAmount = goldAmount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
}
