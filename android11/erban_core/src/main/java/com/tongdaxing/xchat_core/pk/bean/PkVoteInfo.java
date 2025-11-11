package com.tongdaxing.xchat_core.pk.bean;

import java.io.Serializable;

/**
 * 房间投票信息bean
 */
public class PkVoteInfo implements Serializable {
    public long timestamps;
    public int duration;
    public int expireSeconds;
    public int pkType;
    public long uid;
    public String nick;
    public String avatar;
    public int voteCount;
    public long pkUid;
    public String pkNick;
    public String pkAvatar;
    public int pkVoteCount;
    public String createTime;
    public long voteId;
    public long opUid;

    public long getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(long timestamps) {
        this.timestamps = timestamps;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPkType() {
        return pkType;
    }

    public void setPkType(int pkType) {
        this.pkType = pkType;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
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

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public long getPkUid() {
        return pkUid;
    }

    public void setPkUid(long pkUid) {
        this.pkUid = pkUid;
    }

    public String getPkNick() {
        return pkNick;
    }

    public void setPkNick(String pkNick) {
        this.pkNick = pkNick;
    }

    public String getPkAvatar() {
        return pkAvatar;
    }

    public void setPkAvatar(String pkAvatar) {
        this.pkAvatar = pkAvatar;
    }

    public int getPkVoteCount() {
        return pkVoteCount;
    }

    public void setPkVoteCount(int pkVoteCount) {
        this.pkVoteCount = pkVoteCount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getVoteId() {
        return voteId;
    }

    public void setVoteId(long voteId) {
        this.voteId = voteId;
    }

    public long getOpUid() {
        return opUid;
    }

    public void setOpUid(long opUid) {
        this.opUid = opUid;
    }

    public int getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(int expireSeconds) {
        this.expireSeconds = expireSeconds;
    }
}
