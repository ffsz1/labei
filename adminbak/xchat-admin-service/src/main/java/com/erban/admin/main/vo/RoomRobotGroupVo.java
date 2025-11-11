package com.erban.admin.main.vo;

public class RoomRobotGroupVo {
    private Long erbanNo;

    private Long uid;

    private String title;

    private Byte isPermitRoom;

    private Byte canShow;

    private Integer robotNum;

    public Long getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(Long erbanNo) {
        this.erbanNo = erbanNo;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Byte getIsPermitRoom() {
        return isPermitRoom;
    }

    public void setIsPermitRoom(Byte isPermitRoom) {
        this.isPermitRoom = isPermitRoom;
    }

    public Byte getCanShow() {
        return canShow;
    }

    public void setCanShow(Byte canShow) {
        this.canShow = canShow;
    }

    public Integer getRobotNum() {
        return robotNum;
    }

    public void setRobotNum(Integer robotNum) {
        this.robotNum = robotNum;
    }
}
