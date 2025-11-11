package com.erban.main.model;

import java.util.Date;

public class RoomPkVote {
    /**
     * 主键
     */
    private Long id;
    /**
     * 房间ID
     */
    private Long roomId;
    /**
     * 投票类型
     */
    private Byte pkType;
    /**
     * 发起PK用户ID
     */
    private Long uid;
    /**
     * 获得票数
     */
    private Integer voteCount;
    /**
     * 挑战者用户ID
     */
    private Long pkUid;
    /**
     * 挑战者获得票数
     */
    private Integer pkVoteCount;
    /**
     * PK投票时间
     */
    private Integer expireSeconds;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Byte getPkType() {
        return pkType;
    }

    public void setPkType(Byte pkType) {
        this.pkType = pkType;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Long getPkUid() {
        return pkUid;
    }

    public void setPkUid(Long pkUid) {
        this.pkUid = pkUid;
    }

    public Integer getPkVoteCount() {
        return pkVoteCount;
    }

    public void setPkVoteCount(Integer pkVoteCount) {
        this.pkVoteCount = pkVoteCount;
    }

    public Integer getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(Integer expireSeconds) {
        this.expireSeconds = expireSeconds;
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
