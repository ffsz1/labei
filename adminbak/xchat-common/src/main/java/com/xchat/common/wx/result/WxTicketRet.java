package com.xchat.common.wx.result;

/**
 * Created by liuguofu on 2017/7/10.
 */
public class WxTicketRet {
    private String ticket;//	获取到的ticket
    private int expires_in;//凭证有效时间，单位：秒
    private int errcode;
    private String errmsg;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
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
