package com.erban.main.vo;

import java.util.List;

public class ClockRecordVo {
    private String goldAmount;
    private String attendAmount;
    private List<ClockUserVo> avatarList;
    private Integer clockSuccess;
    private Integer clockError;
    private List<ClockUserVo> clockList;

    public String getGoldAmount() {
        return goldAmount;
    }

    public void setGoldAmount(String goldAmount) {
        this.goldAmount = goldAmount;
    }

    public String getAttendAmount() {
        return attendAmount;
    }

    public void setAttendAmount(String attendAmount) {
        this.attendAmount = attendAmount;
    }

    public List<ClockUserVo> getAvatarList() {
        return avatarList;
    }

    public void setAvatarList(List<ClockUserVo> avatarList) {
        this.avatarList = avatarList;
    }

    public Integer getClockSuccess() {
        return clockSuccess;
    }

    public void setClockSuccess(Integer clockSuccess) {
        this.clockSuccess = clockSuccess;
    }

    public Integer getClockError() {
        return clockError;
    }

    public void setClockError(Integer clockError) {
        this.clockError = clockError;
    }

    public List<ClockUserVo> getClockList() {
        return clockList;
    }

    public void setClockList(List<ClockUserVo> clockList) {
        this.clockList = clockList;
    }
}
