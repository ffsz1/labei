package com.xchat.common.wx.result;

/**
 * Created by liuguofu on 2017/7/10.
 */
public class WxAccessTokenRet {
    private String access_token;//	获取到的凭证
    private int expires_in;//凭证有效时间，单位：秒
    private int errcode;
    private String errmsg;
    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }
}
