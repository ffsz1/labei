package com.erban.main.wechat.message.resp;


/**
 * 音乐模板
 * MusicModle
 * @author 电子小孩
 *
 * 2016年5月27日 下午5:29:25
 */
public class MusicModle {
	
	 // 音乐名称   
    private String Title;  
    // 音乐描述   
    private String Description;  
    // 音乐链接   
    private String MusicUrl;  
    // 高质量音乐链接，WIFI环境优先使用该链接播放音乐   
    private String HQMusicUrl;
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
	public String getMusicUrl() {
		return MusicUrl;
	}
	public void setMusicUrl(String musicUrl) {
		MusicUrl = musicUrl;
	}
	public String getHQMusicUrl() {
		return HQMusicUrl;
	}
	public void setHQMusicUrl(String musicUrl) {
		HQMusicUrl = musicUrl;
	}  
    
    

}
