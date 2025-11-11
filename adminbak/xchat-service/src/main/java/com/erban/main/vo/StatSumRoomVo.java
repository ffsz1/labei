package com.erban.main.vo;

public class StatSumRoomVo {
    private Long roomUid;

    private Long moods;

    private Long roomIntoPeoples;

    private Long sumLiveTime;

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
}
