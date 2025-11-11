package com.erban.main.wechat.message.req;

/**
 * 请求连接消息
 * LinkMessage
 * @author 电子小孩
 *
 * 2016年5月27日 下午5:24:31
 */
public class LinkMessage extends BaseMessage {
	
	//消息标题
	private String Title;
	  // 消息描述   
    private String Description;  
    // 消息链接   
    private String Url;
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}  

}
