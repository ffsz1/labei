package com.tongdaxing.xchat_core.im.custom.bean;


import com.alibaba.fastjson.JSONObject;
import com.tongdaxing.xchat_framework.util.util.Json;


/**
 * Created by Administrator on 2018/3/20.
 */

public class PublicChatRoomAttachment extends IMCustomAttachment {
    private String msg;
    private String roomId;
    private long uid;
    private String avatar;
    private int charmLevel;
    private int experLevel;
    private String nick;
    private String txtColor;
    private int server_msg_id;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public PublicChatRoomAttachment(int first, int second) {
        super(first, second);
    }

    @Override
    protected void parseData(JSONObject data) {
        roomId = data.getString("roomId");
        msg = data.getString("msg");
        if (data.containsKey("server_msg_id"))
            server_msg_id = data.getIntValue("server_msg_id");
        JSONObject param = data.getJSONObject("params");
        avatar = param.getString("avatar");
        nick = param.getString("nick");
        uid = param.getLong("uid");
        charmLevel = param.getIntValue("charmLevel");
        if (param.containsKey("experLevel"))
            experLevel = param.getIntValue("experLevel");
        if (param.containsKey("txtColor"))
            txtColor = param.getString("txtColor");

    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", msg);
        JSONObject params = new JSONObject();
        params.put("uid", uid);
        params.put("avatar", avatar);
        params.put("nick", nick);
        params.put("charmLevel", charmLevel);
        params.put("experLevel", experLevel);
        params.put("txtColor", txtColor);
        jsonObject.put("params", params);
        jsonObject.put("roomId", roomId);
        jsonObject.put("server_msg_id",server_msg_id);
        return jsonObject;
    }

    /**
     * 发送消息因为发送位置是Json类型导致旧版方法返回的JSONObject被放入Json中时自带反斜杠，发送给后台
     * 会被转义多出两个\\出来而出现无法解析问题  --- json字符串格式不不正确
     * @return
     */
    @Override
    protected Json packData2() {
        Json jsonObject = new Json();
        jsonObject.set("msg", msg);
        Json params = new Json();
        params.set("uid", uid);
        params.set("avatar", avatar);
        params.set("nick", nick);
        params.set("charmLevel", charmLevel);
        params.set("experLevel", experLevel);
        params.set("txtColor", txtColor);
        jsonObject.set("params", params);
        jsonObject.set("roomId", roomId);
        jsonObject.set("server_msg_id",server_msg_id);
        return jsonObject;
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

    @Override
    public int getCharmLevel() {
        return charmLevel;
    }

    @Override
    public void setCharmLevel(int charmLevel) {
        this.charmLevel = charmLevel;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public int getExperLevel() {
        return experLevel;
    }

    @Override
    public void setExperLevel(int experLevel) {
        this.experLevel = experLevel;
    }

    public String getTxtColor() {
        return txtColor;
    }

    public void setTxtColor(String txtColor) {
        this.txtColor = txtColor;
    }

    public int getServer_msg_id() {
        return server_msg_id;
    }

    public void setServer_msg_id(int server_msg_id) {
        this.server_msg_id = server_msg_id;
    }
}
