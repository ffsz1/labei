package com.erban.main.param;

import java.util.Date;

/**
 * Created by liuguofu on 2017/11/11.
 */
public class RankParam {
    private Long uid;
    private Date startTime;
    private Date endTime;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
