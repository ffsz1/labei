package com.erban.main.param.room;


import java.util.List;

public class RoomRcmdParam {

    private Integer rcmdId;

    private Integer minOnline;

    private String startDate;

    private String endDate;

    private Byte rcmdType;

    private List<Integer> rcmdRoomTags;

    private String rcmdRooms;

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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Byte getRcmdType() {
        return rcmdType;
    }

    public void setRcmdType(Byte rcmdType) {
        this.rcmdType = rcmdType;
    }

    public List<Integer> getRcmdRoomTags() {
        return rcmdRoomTags;
    }

    public void setRcmdRoomTags(List<Integer> rcmdRoomTags) {
        this.rcmdRoomTags = rcmdRoomTags;
    }

    public String getRcmdRooms() {
        return rcmdRooms;
    }

    public void setRcmdRooms(String rcmdRooms) {
        this.rcmdRooms = rcmdRooms;
    }
}
