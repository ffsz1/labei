package com.tongdaxing.xchat_core.im.custom.bean.nim;


import com.alibaba.fastjson.JSONObject;
import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment;


/**
 * Created by Administrator on 2018/3/20.
 */

public class WanFaAttachment extends IMCustomAttachment {
    private String roomDesc;//标题
    private String roomNotice;//内容
    private long roomId;

    public WanFaAttachment(int first, int second) {
        super(first, second);
    }

    @Override
    protected void parseData(JSONObject data) {
        JSONObject param = data.getJSONObject("params");
        roomDesc = param.getString("roomDesc");
        roomNotice = param.getString("roomNotice");
        roomId = param.getLongValue("roomId");
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObject = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("roomDesc", roomDesc);
        params.put("roomNotice", roomNotice);
        params.put("roomId", roomId);
        jsonObject.put("params", params);
        return jsonObject;
    }

    public String getRoomDesc() {
        return roomDesc;
    }

    public void setRoomDesc(String roomDesc) {
        this.roomDesc = roomDesc;
    }

    public String getRoomNotice() {
        return roomNotice;
    }

    public void setRooNotice(String roomNotice) {
        this.roomNotice = roomNotice;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }
}

