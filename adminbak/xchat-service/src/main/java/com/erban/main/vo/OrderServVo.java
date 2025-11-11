package com.erban.main.vo;

import java.util.Date;

import javax.xml.crypto.Data;

/**
 * Created by liuguofu on 2017/6/9.
 */
public class OrderServVo {
	private Long orderId;

	private Long uid;

	private Long prodUid;

	private Byte orderType;

	private String objId;

	private Byte curStatus;

	private Long totalMoney;

	private Date createTime;

	private Date finishTime;

	private String userName;

	private String prodName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	private String userImg;

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	public String getProdImg() {
		return prodImg;
	}

	public void setProdImg(String prodImg) {
		this.prodImg = prodImg;
	}

	public Long getRemainDay() {
		return remainDay;
	}

	public void setRemainDay(Long remainDay) {
		this.remainDay = remainDay;
	}

	private String prodImg;

	private Long remainDay;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getProdUid() {
		return prodUid;
	}

	public void setProdUid(Long prodUid) {
		this.prodUid = prodUid;
	}

	public Byte getOrderType() {
		return orderType;
	}

	public void setOrderType(Byte orderType) {
		this.orderType = orderType;
	}

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public Byte getCurStatus() {
		return curStatus;
	}

	public void setCurStatus(Byte curStatus) {
		this.curStatus = curStatus;
	}

	public Long getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Long totalMoney) {
		this.totalMoney = totalMoney;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
}
