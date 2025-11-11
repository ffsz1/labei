package com.erban.main.vo;

public class UserBlacklistVo {

	private Integer blackId;
	private Long blacklistUid;
	private Long userNo;
	private String nick;
	private String avatar;

	public Integer getBlackId() {
		return blackId;
	}

	public void setBlackId(Integer blackId) {
		this.blackId = blackId;
	}

	public Long getBlacklistUid() {
		return blacklistUid;
	}

	public void setBlacklistUid(Long blacklistUid) {
		this.blacklistUid = blacklistUid;
	}

	public Long getUserNo() {
		return userNo;
	}

	public void setUserNo(Long userNo) {
		this.userNo = userNo;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
}
