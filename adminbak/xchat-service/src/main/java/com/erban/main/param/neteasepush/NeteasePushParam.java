package com.erban.main.param.neteasepush;

import com.xchat.common.constant.Attach;

/**
 * Created by liuguofu on 2017/7/13.
 */
public class NeteasePushParam extends NeteasePushBase{
    private String from;//	String	是	发送者accid，用户帐号，最大32字符，APP内唯一
    private int msgtype;//	int	是	0：点对点自定义通知，1：群消息自定义通知，其他返回414
    private String to;//	String	是	msgtype==0是表示accid即用户id，msgtype==1表示tid即群id

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(int msgtype) {
        this.msgtype = msgtype;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
