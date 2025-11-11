package com.erban.main.wechat.message.req;

/**
 * 
 * BaseMessage
 * @author 电子小孩
 *	Message基类（用户向公众号发送的消息）
 * 2016年5月27日 下午5:18:37
 */
public class BaseMessage {

	 // 开发者微信号   
    private String ToUserName;  
    // 发送方帐号（一个OpenID）   
    private String FromUserName;  
    // 消息创建时间 （整型）   
    private long CreateTime;  
    // 消息类型（text/image/location/link）   
    private String MsgType;  
    // 消息id，64位整型   
    private long MsgId;
    //====================新增===================//
    //接收的语音消息
    private String Recognition;
    
	public String getRecognition()
	{
		return Recognition;
	}
	public void setRecognition(String recognition)
	{
		Recognition = recognition;
	}
	public String getToUserName() {
		return ToUserName;
	}
	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}
	public String getFromUserName() {
		return FromUserName;
	}
	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}
	public long getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(long createTime) {
		CreateTime = createTime;
	}
	public String getMsgType() {
		return MsgType;
	}
	public void setMsgType(String msgType) {
		MsgType = msgType;
	}
	public long getMsgId() {
		return MsgId;
	}
	public void setMsgId(long msgId) {
		MsgId = msgId;
	}  
}
