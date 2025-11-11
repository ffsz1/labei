package com.erban.admin.main.model;

import java.util.Date;

public class GuildHall {
    private Long id;

    private Long guildId;

    private Long roomId;

    private Integer memberCount;

    private Date createTime;

    private Date updateTime;
    
    private String roomName;
    
    private Long erbanNo;
    
    private Long hallUid;
    
    private Boolean isDel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGuildId() {
        return guildId;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
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

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public Long getErbanNo() {
		return erbanNo;
	}

	public void setErbanNo(Long erbanNo) {
		this.erbanNo = erbanNo;
	}

	public Long getHallUid() {
		return hallUid;
	}

	public void setHallUid(Long hallUid) {
		this.hallUid = hallUid;
	}

	public Boolean getIsDel() {
		return isDel;
	}

	public void setIsDel(Boolean isDel) {
		this.isDel = isDel;
	}
    
}