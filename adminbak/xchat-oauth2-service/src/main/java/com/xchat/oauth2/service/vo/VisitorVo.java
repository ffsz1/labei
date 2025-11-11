package com.xchat.oauth2.service.vo;

/**
 * Created by liuguofu on 2017/6/30.
 */
public class VisitorVo {
    private Long uid;
    private String netEaseToken;
    private String nick;
    private Byte gender;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getNetEaseToken() {
        return netEaseToken;
    }

    public void setNetEaseToken(String netEaseToken) {
        this.netEaseToken = netEaseToken;
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
}
