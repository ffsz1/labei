package com.tongdaxing.xchat_core.linked;

/**
 * Created by chenran on 2017/8/11.
 */

public class LinkedInfo {
    private String channel;
    private String roomUid;
    private String uid;
    private String type;
    private boolean isNewUser;
    private String shareUid;

    public String getShareUid() {
        return shareUid;
    }

    public void setShareUid(String shareUid) {
        this.shareUid = shareUid;
    }

    @Override
    public String toString() {
        return "LinkedInfo{" +
                "channel='" + channel + '\'' +
                ", roomUid='" + roomUid + '\'' +
                ", uid='" + uid + '\'' +
                ", type='" + type + '\'' +
                ", isNewUser=" + isNewUser +
                ", shareUid='" + shareUid + '\'' +
                '}';
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getRoomUid() {
        return roomUid;
    }

    public void setRoomUid(String roomUid) {
        this.roomUid = roomUid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isNewUser() {
        return isNewUser;
    }

    public void setNewUser(boolean newUser) {
        isNewUser = newUser;
    }
}
