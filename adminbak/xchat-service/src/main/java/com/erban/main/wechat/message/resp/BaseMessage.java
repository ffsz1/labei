package com.erban.main.wechat.message.resp;


/**
 * 响应消息基类（微信端（服务器）--》用户）
 * BaseMessage
 * @author 电子小孩
 *
 * 2016年5月27日 下午5:28:20
 */
public class BaseMessage {
	
	// 接收方帐号（收到的OpenID）   
    private String ToUserName;  
    // 开发者微信号   
    private String FromUserName;  
    // 消息创建时间 （整型）   
    private long CreateTime;  
    // 消息类型（text/music/news）   
    private String MsgType;  
    // 位0x0001被标志时，星标刚收到的消息   
    private int FuncFlag;
    
    //=================新增=================//
    //用于发送的语音消息
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
	public int getFuncFlag() {
		return FuncFlag;
	}
	public void setFuncFlag(int funcFlag) {
		FuncFlag = funcFlag;
	}  


}
