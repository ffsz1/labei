package com.xchat.oauth2.service.vo;

/**
 * Created by liuguofu on 2017/4/29.
 */
public class AccountVo {
    private Long uid;
    private String phone;
    private String accid;
    private String netEaseToken;

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

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public String getNetEaseToken() {
        return netEaseToken;
    }

    public void setNetEaseToken(String netEaseToken) {
        this.netEaseToken = netEaseToken;
    }
}
