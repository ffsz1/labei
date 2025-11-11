package com.erban.main.service.room.vo;

import com.xchat.common.utils.DateUtil;

import java.util.Date;
import java.util.List;

public class RoomRcmdVo {
    private Integer rcmdId;

    private Integer minOnline;

    private Byte rcmdType;

    private Date startDate;

    private Date endDate;

    private String rcmdRooms;

    private List<Integer> rcmdRoomTags;

    public Integer getRcmdId() {
        return rcmdId;
    }

    public void setRcmdId(Integer rcmdId) {
        this.rcmdId = rcmdId;
    }

    public Integer getMinOnline() {
        return minOnline;
    }

    public void setMinOnline(Integer minOnline) {
        this.minOnline = minOnline;
    }

    public Byte getRcmdType() {
        return rcmdType;
    }

    public void setRcmdType(Byte rcmdType) {
        this.rcmdType = rcmdType;
    }

    public String getStartDate() {
        return DateUtil.date2Str(startDate, DateUtil.DateFormat.YYYY_MM_DD_HH_MM_SS);
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return DateUtil.date2Str(endDate, DateUtil.DateFormat.YYYY_MM_DD_HH_MM_SS);
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getRcmdRooms() {
        return rcmdRooms;
    }

    public void setRcmdRooms(String rcmdRooms) {
        this.rcmdRooms = rcmdRooms;
    }

    public List<Integer> getRcmdRoomTags() {
        return rcmdRoomTags;
    }

    public void setRcmdRoomTags(List<Integer> rcmdRoomTags) {
        this.rcmdRoomTags = rcmdRoomTags;
    }
}
