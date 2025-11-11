package com.erban.main.vo;

import java.util.Date;

public class StatUserStandTimeVo {
    private Long id;

    private Double avgTime;

    public void setAvgTime(Double avgTime) {
        this.avgTime = avgTime;
    }

    public Double getAvgTime() {

        return avgTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
