package com.erban.main.model;

public class RoomRcmdPool {
    private Integer rcmdId;

    private Long roomFkId;

    public RoomRcmdPool() {
    }

    public RoomRcmdPool(Integer rcmdId, Long roomFkId) {
        this.rcmdId = rcmdId;
        this.roomFkId = roomFkId;
    }

    public Integer getRcmdId() {
        return rcmdId;
    }

    public void setRcmdId(Integer rcmdId) {
        this.rcmdId = rcmdId;
    }

    public Long getRoomFkId() {
        return roomFkId;
    }

    public void setRoomFkId(Long roomFkId) {
        this.roomFkId = roomFkId;
    }
}
