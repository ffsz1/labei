package com.erban.admin.main.dto;

import java.util.Date;

/**
 * @Description: 用户在房间的消费记录
 * @Author: alwyn
 * @Date: 2018/11/9 11:57
 */
public class UserConsumeDTO {

    private Long uid;
    private Long roomUid;
    private Date minDate;
    private Date maxDate;
    private Long totalNum;
    private Long totalGold;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getRoomUid() {
        return roomUid;
    }

    public void setRoomUid(Long roomUid) {
        this.roomUid = roomUid;
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public Long getTotalGold() {
        return totalGold;
    }

    public void setTotalGold(Long totalGold) {
        this.totalGold = totalGold;
    }

    @Override
    public String toString() {
        return "UserConsumeDTO{" +
                "uid=" + uid +
                ", roomUid=" + roomUid +
                ", minDate=" + minDate +
                ", maxDate=" + maxDate +
                ", totalNum=" + totalNum +
                ", totalGold=" + totalGold +
                '}';
    }
}
