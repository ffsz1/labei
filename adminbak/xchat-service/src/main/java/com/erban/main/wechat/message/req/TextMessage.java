package com.erban.main.wechat.message.req;

/***
 * 请求文本消息
 * TextMessage
 * @author 电子小孩
 *
 * 2016年5月27日 下午5:25:12
 */
public class TextMessage extends BaseMessage {
	
	   // 消息内容   
    private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}  
	

}
