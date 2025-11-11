package com.erban.main.model.dto;

import com.xchat.common.utils.DateUtil;

import java.util.Date;

public class RoomPkVoteDTO {
    /**
     * PKID
     */
    private Long voteId;
    /**
     * 系统当前时间戳
     */
    private Long timestamps;

    /**
     * 投票剩余时间（单位：秒）
     */
    private Integer duration;

    /**
     * 投票类型
     */
    private Byte pkType;

    /**
     * 发起PK用户ID
     */
    private Long uid;

    /**
     * 用户昵称
     */
    private String nick;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 获得票数
     */
    private Integer voteCount;

    /**
     * 挑战者用户ID
     */
    private Long pkUid;

    /**
     * 挑战者用户昵称
     */
    private String pkNick;

    /**
     * 挑战者头像
     */
    private String pkAvatar;

    /**
     * 挑战者获得票数
     */
    private Integer pkVoteCount;

    /**
     * 过期时间
     */
    private Integer expireSeconds;

    private Date createTime;

    public Long getVoteId() {
        return voteId;
    }

    public void setVoteId(Long voteId) {
        this.voteId = voteId;
    }

    public Long getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(Long timestamps) {
        this.timestamps = timestamps;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public String getPkAvatar() {
        return pkAvatar;
    }

    public void setPkAvatar(String pkAvatar) {
        this.pkAvatar = pkAvatar;
    }

    public Integer getPkVoteCount() {
        return pkVoteCount;
    }

    public void setPkVoteCount(Integer pkVoteCount) {
        this.pkVoteCount = pkVoteCount;
    }

    public String getCreateTime() {
        return createTime == null ? null : DateUtil.date2Str(createTime, DateUtil.DateFormat.YYYY_MM_DD_HH_MM_SS);
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPkNick() {
        return pkNick;
    }

    public void setPkNick(String pkNick) {
        this.pkNick = pkNick;
    }

    public Integer getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(Integer expireSeconds) {
        this.expireSeconds = expireSeconds;
    }
}
