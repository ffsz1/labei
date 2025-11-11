package com.erban.main.wechat;

/**
 * access_token响应（重要）微信唯一接口
 * AccessToken
 * @author 电子小孩
 *
 * 2016年5月27日 下午5:30:27
 */
public class AccessToken {
	
	//获取到的凭证
	private String token;
	//凭证有效时间
	private String expiresIn;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}

}
