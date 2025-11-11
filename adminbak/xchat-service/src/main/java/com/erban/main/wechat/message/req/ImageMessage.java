package com.erban.main.wechat.message.req;

/**
 * 请求图片消息类
 * ImageMessage
 * @author 电子小孩
 *
 * 2016年5月27日 下午5:23:59
 */
public class ImageMessage extends BaseMessage {
	
	// 图片链接   
    private String PicUrl;

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}  
}
