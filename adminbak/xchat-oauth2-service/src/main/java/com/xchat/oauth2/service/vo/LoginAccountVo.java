package com.xchat.oauth2.service.vo;

/**
 * Created by liuguofu on 2017/3/5.
 */
public class LoginAccountVo {

    private Long uid;

    private String phone;

    private String empassword;

    private  String token;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmpassword() {
        return empassword;
    }

    public void setEmpassword(String empassword) {
        this.empassword = empassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
