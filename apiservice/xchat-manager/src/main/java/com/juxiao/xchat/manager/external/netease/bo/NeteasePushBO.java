package com.juxiao.xchat.manager.external.netease.bo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NeteasePushBO extends BaseNeteasePushBO {
    /**
     * 发送者accid，用户帐号，最大32字符，APP内唯一
     */
    private String from;//	String	是
    /**
     * 0：点对点自定义通知，1：群消息自定义通知，其他返回414
     */
    private int msgtype;//	int	是
    /**
     * msgtype==0是表示accid即用户id，msgtype==1表示tid即群id
     */
    private String to;//	String	是
}
