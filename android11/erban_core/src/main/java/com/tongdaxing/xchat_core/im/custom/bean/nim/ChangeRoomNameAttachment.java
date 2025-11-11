package com.tongdaxing.xchat_core.im.custom.bean.nim;


import com.alibaba.fastjson.JSONObject;
import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment;


/**
 * Created by Administrator on 2018/3/20.
 */

public class ChangeRoomNameAttachment extends IMCustomAttachment {
    private String roomName;//房间名字
    private long roomId;
    private long uid;

    public ChangeRoomNameAttachment(int first, int second) {
        super(first, second);
    }

    @Override
    protected void parseData(JSONObject data) {
        JSONObject param = data.getJSONObject("params");
        roomName = param.getString("roomName");
        roomId = param.getLongValue("roomId");
        uid = param.getLongValue("uid");
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObject = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("roomName", roomName);
        params.put("roomId", roomId);
        params.put("uid", uid);
        jsonObject.put("params", params);
        return jsonObject;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getUId() {
        return uid;
    }

    public void setUId(long uid) {
        this.uid = uid;
    }
}

