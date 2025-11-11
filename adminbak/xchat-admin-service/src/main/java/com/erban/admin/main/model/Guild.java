package com.erban.admin.main.model;

import java.util.Date;

public class Guild {
    private Long id;

    private String guildNo;

    private String name;

    private String logoUrl;

    private Long presidentUid;

    private Integer hallCount;

    private Integer memberCount;

    private Date createTime;

    private Date updateTime;

    private Boolean isDel;
    
    private String nick;
    
    private Long erbanNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuildNo() {
        return guildNo;
    }

    public void setGuildNo(String guildNo) {
        this.guildNo = guildNo == null ? null : guildNo.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl == null ? null : logoUrl.trim();
    }

    public Long getPresidentUid() {
        return presidentUid;
    }

    public void setPresidentUid(Long presidentUid) {
        this.presidentUid = presidentUid;
    }

    public Integer getHallCount() {
        return hallCount;
    }

    public void setHallCount(Integer hallCount) {
        this.hallCount = hallCount;
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

    public Boolean getIsDel() {
        return isDel;
    }

    public void setIsDel(Boolean isDel) {
        this.isDel = isDel;
    }

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public Long getErbanNo() {
		return erbanNo;
	}

	public void setErbanNo(Long erbanNo) {
		this.erbanNo = erbanNo;
	}
    
}