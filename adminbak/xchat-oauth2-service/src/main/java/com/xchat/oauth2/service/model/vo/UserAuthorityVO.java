package com.xchat.oauth2.service.model.vo;

public class UserAuthorityVO {
    private Long uid;
    private Long erbanNo;
    private String nick;
    private Boolean authority;// true:有权限, false:无权限

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
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

    public Boolean getAuthority() {
        return authority;
    }

    public void setAuthority(Boolean authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return "UserAuthorityVO{" +
                "erbanNo=" + erbanNo +
                ", nick='" + nick + '\'' +
                ", authority=" + authority +
                '}';
    }
}
