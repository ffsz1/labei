package com.erban.main.wechat.message.resp;


/**
 * 文本消息响应
 * TextMessage
 * @author 电子小孩
 *
 * 2016年5月27日 下午5:30:09
 */
public class TextMessage extends BaseMessage {
	
	//回复的消息内容
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

}
