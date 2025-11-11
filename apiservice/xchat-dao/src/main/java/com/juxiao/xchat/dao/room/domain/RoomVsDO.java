package com.juxiao.xchat.dao.room.domain;

import java.util.Date;

public class RoomVsDO {
	
	private Long id;
	// 房主uid
	private Long roomUid;
	// PK时长(单位：秒)
	private Integer seconds;
    //得分类型：0（按礼物价值）
	private Integer scoreType;
    //PK开始时间
	private Date startTime;
	//预计PK结束时间(到期)
	private Date endTime;
	//PK结果：0（有胜负），1（平局）
	private Integer resultType;
	//获胜队（若为平局则为null，否则存储获胜队id等）
	private Long winTeamId;
    //状态：0（进行中），1（倒计时结束），2（房主/管理员点击结束），3（PK用户掉线结束），4（PK用户退出房间）'
	private Integer status;
    //发起PK用户uid
	private Long startUser;
    //主动终止PK用户uid(PK状态为2时才有值)
	private Long endUser;
    //PK真实结束时间
	private Date terminateTime;
    
	private Date createTime;

	private Date updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRoomUid() {
		return roomUid;
	}

	public void setRoomUid(Long roomUid) {
		this.roomUid = roomUid;
	}

	public Integer getSeconds() {
		return seconds;
	}

	public void setSeconds(Integer seconds) {
		this.seconds = seconds;
	}

	public Integer getScoreType() {
		return scoreType;
	}

	public void setScoreType(Integer scoreType) {
		this.scoreType = scoreType;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getResultType() {
		return resultType;
	}

	public void setResultType(Integer resultType) {
		this.resultType = resultType;
	}

	public Long getWinTeamId() {
		return winTeamId;
	}

	public void setWinTeamId(Long winTeamId) {
		this.winTeamId = winTeamId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getStartUser() {
		return startUser;
	}

	public void setStartUser(Long startUser) {
		this.startUser = startUser;
	}

	public Long getEndUser() {
		return endUser;
	}

	public void setEndUser(Long endUser) {
		this.endUser = endUser;
	}

	public Date getTerminateTime() {
		return terminateTime;
	}

	public void setTerminateTime(Date terminateTime) {
		this.terminateTime = terminateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}