package com.juxiao.xchat.dao.sysconf.enumeration;

public enum NetEaseMsgEventType {
    /**
     * 表示CONVERSATION消息，即会话类型的消息（目前包括P2P聊天消息，群组聊天消息，群组操作，好友操作）
     */
    CONVERSATION(1),
    /**
     * 表示LOGIN消息，即用户登录事件的消息
     */
    LOGIN(2),
    /**
     * 表示LOGOUT消息，即用户登出事件的消息
     */
    LOGOUT(3),
    /**
     * 表示CHATROOM消息，即聊天室中聊天的消息
     */
    CHATROOM(4),
    /**
     * 表示AUDIO/VEDIO/DataTunnel消息，即汇报实时音视频通话时长、白板事件时长的消息
     */
    AUDIO(5),
    /**
     * 表示音视频/白板文件存储信息，即汇报音视频/白板文件的大小、下载地址等消息
     */
    AUDIOSAVE(6),
    /**
     * 表示单聊消息撤回抄送
     */
    DRAWBACK(7),
    /**
     * 表示群聊消息撤回抄送
     */
    QDRAWBACK(8),
    /**
     * 表示CHATROOM_INOUT信息，即汇报主播或管理员进出聊天室事件消息
     */
    CHATROOMINTOUT(9),
    /**
     * 表示ECP_CALLBACK信息，即汇报专线电话通话结束回调抄送的消息
     */
    ECPCALLBACK(10),
    /**
     * 表示SMS_CALLBACK信息，即汇报短信回执抄送的消息
     */
    SMSCALLBACK(11),
    /**
     * 表示SMS_REPLY信息，即汇报短信上行消息
     */
    SMSREPLY(12),
    /**
     * 表示AVROOM_INOUT信息，即汇报用户进出音视频/白板房间的消息
     */
    AVROOMINOUT(13),
    /**
     * 表示CHATROOM_QUEUE_OPERATE信息，即汇报聊天室队列操作的事件消息
     */
    CHATROOMQUEUEOPERATE(14);

    private int value;

    NetEaseMsgEventType(int value) {
        this.value = value;
    }

    /**
     * @param value
     * @return
     */
    public boolean compareValue(Integer value) {
        return value != null && value.intValue() == this.value;
    }
}
