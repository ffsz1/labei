package com.juxiao.xchat.manager.external.netease.conf;

/**
 * 云信--子协议
 */
public class DefineProtocol {
    public static final int CUSTOM_MESS_DEFINE              = 100; // 表示自定义消息
    public static final int CUSTOM_MESS_HEAD_QUEUE          = 8;   // 队列

    public static final int CUSTOM_MESS_SUB_INVITE          = 81;  // 邀请上麦
    public static final int CUSTOM_MESS_SUB_KICKIT          = 82;  // 踢它下麦

    public static final int CUSTOM_MESS_HEAD_NOBLE          = 14;  // 贵族
    public static final int CUSTOM_MESS_SUB_OPENNOBLE       = 142; // 开通贵族
    public static final int CUSTOM_MESS_SUB_RENEWNOBLE      = 143; // 续费贵族
    public static final int CUSTOM_MESS_SUB_WILLEXPIRE      = 144; // 贵族快到期
    public static final int CUSTOM_MESS_SUB_HADEXPIRE       = 145; // 贵族已到期
    public static final int CUSTOM_MESS_SUB_GOODNUM_OK      = 146; // 靓号生效
    public static final int CUSTOM_MESS_SUB_GOODNUM_NOTOK   = 147; // 靓号未生效
    public static final int CUSTOM_MESS_SUB_ROOM_INCOME     = 148; // 房主分成
    public static final int CUSTOM_MESS_SUB_RECOM_ROOM      = 149; // 推荐房间
}
