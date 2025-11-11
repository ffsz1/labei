package com.juxiao.xchat.dao.room.domain;

import java.util.Date;

public class RoomVsTeamDO {
    private Long id;
    //PK id
    private Long vsId;
    //队伍序号：1（蓝方），2（红方）
    private Integer teamIndex;
    //得分
    private Long score;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVsId() {
        return vsId;
    }

    public void setVsId(Long vsId) {
        this.vsId = vsId;
    }

    public Integer getTeamIndex() {
        return teamIndex;
    }

    public void setTeamIndex(Integer teamIndex) {
        this.teamIndex = teamIndex;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
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