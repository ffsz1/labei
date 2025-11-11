package com.tongdaxing.xchat_core.room.queue.bean;

/**
 * Created by chenran on 2017/10/25.
 */

public class MicMemberInfo {
    private long uid;
    private String avatar;
    private String nick;
    private int micPosition;
    private boolean isRoomOwnner;
    private boolean isSelect = false;//礼物用户选择中状态判断

    public MicMemberInfo() {

    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getMicPosition() {
        return micPosition;
    }

    public void setMicPosition(int micPosition) {
        this.micPosition = micPosition;
    }

    public boolean isRoomOwnner() {
        return isRoomOwnner;
    }

    public void setRoomOwnner(boolean roomOwnner) {
        isRoomOwnner = roomOwnner;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
