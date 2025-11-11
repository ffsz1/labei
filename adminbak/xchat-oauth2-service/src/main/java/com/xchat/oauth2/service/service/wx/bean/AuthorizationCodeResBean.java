package com.xchat.oauth2.service.service.wx.bean;

/**
 * 
 * @class: WeixinAuthorizationCodeResponse
 * @author: chenjunsheng
 * @date 2018年4月25日
 */
public class AuthorizationCodeResBean {
	private String errcode;
	private String errmsg;
	private String openid;
	private Integer expires_in;
	private String session_key;

	public AuthorizationCodeResBean() {
	}

	public AuthorizationCodeResBean(String errcode, String errmsg) {
		this.errcode = errcode;
		this.errmsg = errmsg;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getSession_key() {
		return session_key;
	}

	public void setSession_key(String session_key) {
		this.session_key = session_key;
	}

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public Integer getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(Integer expires_in) {
		this.expires_in = expires_in;
	}
}
