package com.erban.main.vo;

import com.erban.main.model.StatPacketBouns;

import java.util.List;

/**
 * Created by liuguofu on 2017/9/19.
 */
public class StatPacketBounsDetailParentVo {
    private Double todayBouns;
    private Double totalBouns;
    private List<StatPacketBounsListVo> bounsList;

    public List<StatPacketBounsListVo> getBounsList() {
        return bounsList;
    }

    public void setBounsList(List<StatPacketBounsListVo> bounsList) {
        this.bounsList = bounsList;
    }

    public Double getTodayBouns() {
        return todayBouns;
    }

    public void setTodayBouns(Double todayBouns) {
        this.todayBouns = todayBouns;
    }

    public Double getTotalBouns() {
        return totalBouns;
    }

    public void setTotalBouns(Double totalBouns) {
        this.totalBouns = totalBouns;
    }
}
