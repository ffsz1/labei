package com.tongdaxing.xchat_core.auth;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/4.
 */

public class ThirdUserInfo implements Serializable {
    private String userName;
    private String userIcon;
    private String userGender;
    private long birth;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public long getBirth() {
        return birth;
    }

    public void setBirth(long birth) {
        this.birth = birth;
    }
}
