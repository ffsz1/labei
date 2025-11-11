package com.erban.main.wechat.message.req;

/**
 * 请求事件消息
 * EventMessage
 * @author 电子小孩
 *
 * 2016年5月27日 下午5:20:00
 */
public class EventMessage {
	
	 // 开发者微信号   
    private String ToUserName;  
    // 发送方帐号（一个OpenID）   
    private String FromUserName;  
    // 消息创建时间 （整型）   
    private long CreateTime;  
    // 消息类型（text/image/location/link）   
    private String MsgType;  
    //事件类型 (subscribe(订阅)、unsubscribe(取消订阅)、CLICK(自定义菜单点击事件))
    private String Event;
    //事件KEY值 (与自定义菜单接口中KEY值对应) 
    private String EventKey;
    
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
	public String getEvent() {
		return Event;
	}
	public void setEvent(String event) {
		Event = event;
	}
	public String getEventKey() {
		return EventKey;
	}
	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}
}
