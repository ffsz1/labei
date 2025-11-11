package com.tongdaxing.xchat_core.im.custom.bean;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by chenran on 2017/8/14.
 */

public class OpenRoomNotiAttachment extends CustomAttachment {
    private long uid;
    private String nick;
    private String avatar;

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getUid() {
        return uid;
    }

    @Override
    protected void parseData(JSONObject data) {
        uid = data.getLong("uid");
        nick = data.getString("nick");
        avatar = data.getString("avatar");

        JSONObject userInfo = data.getJSONObject("userVo");
        nick = userInfo.getString("nick");
        avatar = userInfo.getString("avatar");
    }

    OpenRoomNotiAttachment(int first, int second) {
        super(first,second);
    }

    @Override
    protected JSONObject packData() {
        JSONObject object = new JSONObject();
        return object;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
