package com.erban.main.model;

import java.util.Date;

/**
 * @author laizhilong
 * @Title:
 * @Package com.erban.main.model
 * @date 2018/7/24
 * @time 19:28
 */
public class RoomFlowWeekVo {

    private Long uid;
    private Long firstWeek;// 当前时间往前第一周
    private Long secondWeek;// 当前时间往前第二周;
    private String proportion;
    private Date createDate;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getFirstWeek() {
        return firstWeek;
    }

    public void setFirstWeek(Long firstWeek) {
        this.firstWeek = firstWeek;
    }

    public Long getSecondWeek() {
        return secondWeek;
    }

    public void setSecondWeek(Long secondWeek) {
        this.secondWeek = secondWeek;
    }

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
