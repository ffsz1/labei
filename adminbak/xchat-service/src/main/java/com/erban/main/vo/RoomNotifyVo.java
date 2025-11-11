package com.erban.main.vo;


public class RoomNotifyVo {
    private int type;  // 1：房间消息，2：坑位消息
    private String roomInfo;
    private String micInfo;
  public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(String roomInfo) {
        this.roomInfo = roomInfo;
    }

    public String getMicInfo() {
        return micInfo;
    }

    public void setMicInfo(String micInfo) {
        this.micInfo = micInfo;
    }
}
