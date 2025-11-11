package com.erban.main.vo;

import java.util.Date;

public class StatBasicRoomVo {
    private Long id;

    private Long roomUid;

    private Long moods;

    private Long roomIntoPeoples;

    private Long sumLiveTime;

    private Date createTime;

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

    public Long getMoods() {
        return moods;
    }

    public void setMoods(Long moods) {
        this.moods = moods;
    }

    public Long getRoomIntoPeoples() {
        return roomIntoPeoples;
    }

    public void setRoomIntoPeoples(Long roomIntoPeoples) {
        this.roomIntoPeoples = roomIntoPeoples;
    }

    public Long getSumLiveTime() {
        return sumLiveTime;
    }

    public void setSumLiveTime(Long sumLiveTime) {
        this.sumLiveTime = sumLiveTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
