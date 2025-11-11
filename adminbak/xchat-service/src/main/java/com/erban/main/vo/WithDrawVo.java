package com.erban.main.vo;

public class WithDrawVo {

	private Long uid;
	
	private Double diamondNum;
	
	private Boolean isNotBoundPhone;
	
	private String alipayAccount;

	private String alipayAccountName;

	public Boolean getIsNotBoundPhone() {
		return isNotBoundPhone;
	}

	public void setIsNotBoundPhone(Boolean isNotBoundPhone) {
		this.isNotBoundPhone = isNotBoundPhone;
	}

	public Double getDiamondNum() {
		return diamondNum;
	}

	public void setDiamondNum(Double diamondNum) {
		this.diamondNum = diamondNum;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getAlipayAccount() {
		return alipayAccount;
	}

	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}

	public String getAlipayAccountName() {
		return alipayAccountName;
	}

	public void setAlipayAccountName(String alipayAccountName) {
		this.alipayAccountName = alipayAccountName;
	}
}
