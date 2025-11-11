package com.tongdaxing.xchat_core.user.bean;

import java.io.Serializable;

/**
 * Created by ${Seven} on 2017/8/9.
 */

public class FansInfo implements Serializable {
    private  long uid;
    private  boolean valid;
    private  String avatar;
    private String nick;
    private boolean isMyFriend;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
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

    public boolean isMyFriend() {
        return isMyFriend;
    }

    public void setMyFriend(boolean isMyFriend) {
        this.isMyFriend = isMyFriend;
    }

    @Override
    public String toString() {
        return "FansInfo{" +
                "uid=" + uid +
                ", valid=" + valid +
                ", avatar='" + avatar + '\'' +
                ", nick='" + nick + '\'' +
                '}';
    }
}
