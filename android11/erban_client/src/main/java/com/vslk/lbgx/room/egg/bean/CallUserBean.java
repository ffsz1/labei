package com.vslk.lbgx.room.egg.bean;

import com.tongdaxing.xchat_core.user.bean.UserInfo;

public class CallUserBean {
    private boolean isSelect = false;
    private UserInfo userInfo;


    public CallUserBean() {

    }

    public CallUserBean(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
