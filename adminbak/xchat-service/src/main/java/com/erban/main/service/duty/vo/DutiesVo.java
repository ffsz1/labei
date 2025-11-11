package com.erban.main.service.duty.vo;

import com.erban.main.service.duty.dto.DutyResultDTO;

import java.util.List;


public class DutiesVo {

    private Integer roomTime;

    private List<DutyResultDTO> fresh;

    private List<DutyResultDTO> daily;

    private List<DutyResultDTO> dailyTime;

    public DutiesVo() {
    }

    public Integer getRoomTime() {
        return roomTime;
    }

    public void setRoomTime(Integer roomTime) {
        this.roomTime = roomTime;
    }

    public List<DutyResultDTO> getFresh() {
        return fresh;
    }

    public void setFresh(List<DutyResultDTO> fresh) {
        this.fresh = fresh;
    }

    public List<DutyResultDTO> getDaily() {
        return daily;
    }

    public void setDaily(List<DutyResultDTO> daily) {
        this.daily = daily;
    }

    public List<DutyResultDTO> getDailyTime() {
        return dailyTime;
    }

    public void setDailyTime(List<DutyResultDTO> dailyTime) {
        this.dailyTime = dailyTime;
    }
}
