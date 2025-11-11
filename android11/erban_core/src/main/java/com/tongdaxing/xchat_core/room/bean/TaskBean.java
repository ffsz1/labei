package com.tongdaxing.xchat_core.room.bean;

import java.util.List;

public class TaskBean {
    private List<TaskInfo> fresh;

    private List<TaskInfo> daily;

    private List<TaskInfo> dailyTime;

    private int roomTime;

    public List<TaskInfo> getFresh() {
        return fresh;
    }

    public void setFresh(List<TaskInfo> fresh) {
        this.fresh = fresh;
    }

    public List<TaskInfo> getDaily() {
        return daily;
    }

    public void setDaily(List<TaskInfo> daily) {
        this.daily = daily;
    }

    public List<TaskInfo> getDailyTime() {
        return dailyTime;
    }

    public void setDailyTime(List<TaskInfo> dailyTime) {
        this.dailyTime = dailyTime;
    }

    public int getRoomTime() {
        return roomTime;
    }

    public void setRoomTime(int roomTime) {
        this.roomTime = roomTime;
    }
}
