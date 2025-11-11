package com.erban.admin.main.domain;

import java.util.Date;

/**
 * @Description:
 * @Author: alwyn
 * @Date: 2018/11/9 16:33
 */
public class UserRoomConsume {

    private Long uid;
    private Long roomUid;
    private Date firstDate;
    private Date lastDate;
    private Long totalGold;
    private Long totalNum;
    private Date updateDate;

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

    public Date getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(Date firstDate) {
        this.firstDate = firstDate;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    public Long getTotalGold() {
        return totalGold;
    }

    public void setTotalGold(Long totalGold) {
        this.totalGold = totalGold;
    }

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return "UserRoomConsume{" +
                "uid=" + uid +
                ", roomUid=" + roomUid +
                ", firstDate=" + firstDate +
                ", lastDate=" + lastDate +
                ", totalGold=" + totalGold +
                ", totalNum=" + totalNum +
                ", updateDate=" + updateDate +
                '}';
    }
}
