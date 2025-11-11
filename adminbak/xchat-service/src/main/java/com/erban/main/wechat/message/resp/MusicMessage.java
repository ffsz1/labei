package com.erban.main.wechat.message.resp;


/**
 * 音乐消息响应
 * MusicMessage
 * @author 电子小孩
 *
 * 2016年5月27日 下午5:29:04
 */
public class MusicMessage extends BaseMessage {
	
	//回复音乐
	private MusicModle Music;

	public MusicModle getMusic() {
		return Music;
	}

	public void setMusic( MusicModle music) {
		Music = music;
	}
	
	
}
