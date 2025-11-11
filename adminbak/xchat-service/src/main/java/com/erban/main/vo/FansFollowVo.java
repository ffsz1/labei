package com.erban.main.vo;

import com.erban.main.model.NobleUsers;

public class FansFollowVo {

    private String nick;

    private Long uid;

    private String avatar;

    private boolean valid;

    private Integer fansNum;

    private String roomBack;

    private Byte type;

    private Integer onlineNum;

    private Byte gender;

    private Byte operatorStatus;

    private String title;

    private NobleUsers nobleUsers; // 用户贵族信息
    private RoomVo userInRoom;     // 所在房间信息，用于踩人

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRoomBack(String roomBack) {
        this.roomBack = roomBack;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public void setOnlineNum(Integer onlineNum) {
        this.onlineNum = onlineNum;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getRoomBack() {

        return roomBack;
    }

    public RoomVo getUserInRoom() {
        return userInRoom;
    }

    public void setUserInRoom(RoomVo userInRoom) {
        this.userInRoom = userInRoom;
    }

    public Byte getOperatorStatus() {
        return operatorStatus;
    }

    public void setOperatorStatus(Byte operatorStatus) {
        this.operatorStatus = operatorStatus;
    }

    public Byte getType() {
        return type;
    }

    public Integer getOnlineNum() {
        return onlineNum;
    }

    public Byte getGender() {
        return gender;
    }

    public void setFansNum(Integer fansNum) {
        this.fansNum = fansNum;
    }

    public Integer getFansNum() {

        return fansNum;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }


    public void setUid(Long uid) {
        this.uid = uid;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getNick() {

        return nick;
    }

    public Long getUid() {
        return uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public boolean isValid() {
        return valid;
    }

    public NobleUsers getNobleUsers() {
        return nobleUsers;
    }

    public void setNobleUsers(NobleUsers nobleUsers) {
        this.nobleUsers = nobleUsers;
    }
}
