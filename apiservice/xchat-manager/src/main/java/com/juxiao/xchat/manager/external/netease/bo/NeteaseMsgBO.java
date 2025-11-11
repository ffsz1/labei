package com.juxiao.xchat.manager.external.netease.bo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NeteaseMsgBO extends BaseNeteaseMsgBO {
    private String from;//	String	是	发送者accid，用户帐号，最大32字符，APP内唯一
    private int ope;//0：点对点个人消息，1：群消息（高级群），其他返回414
    private int type;//	0 表示文本消息,1 表示图片，2 表示语音，3 表示视频，4 表示地理位置信息6 表示文件，100 自定义消息类型（特别注意，对于未对接易盾反垃圾功能的应用，该类型的消息不会提交反垃圾系统检测）
    private String to;//	String	是	msgtype==0是表示accid即用户id，msgtype==1表示tid即群id
    private Picture picture;
}
