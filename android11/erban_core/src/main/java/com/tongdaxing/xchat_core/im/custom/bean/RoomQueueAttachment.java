package com.tongdaxing.xchat_core.im.custom.bean;

import com.alibaba.fastjson.JSONObject;
import com.tongdaxing.xchat_core.room.queue.bean.RoomQueueInfo;

/**
 * Created by chenran on 2017/9/7.
 */

public class RoomQueueAttachment extends IMCustomAttachment {
    private long uid;
    private RoomQueueInfo roomQueueInfo;

    public RoomQueueAttachment(int first, int second) {
        super(first, second);
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public RoomQueueInfo getRoomQueueInfo() {
        return roomQueueInfo;
    }

    public void setRoomQueueInfo(RoomQueueInfo roomQueueInfo) {
        this.roomQueueInfo = roomQueueInfo;
    }

    @Override
    protected void parseData(JSONObject data) {
        uid = data.getLong("uid");
        JSONObject jsonObject = data.getJSONObject("data");
        int queueType = jsonObject.getIntValue("queueType");
        roomQueueInfo = new RoomQueueInfo(queueType);
        roomQueueInfo.setMute(jsonObject.getBoolean("isMute"));
        roomQueueInfo.setInviteUid(jsonObject.getString("inviteUid"));
        roomQueueInfo.setPosition(jsonObject.getString("position"));
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("queueType", roomQueueInfo.getQueueType());
        jsonObject.put("isMute", roomQueueInfo.isMute());
        jsonObject.put("inviteUid", roomQueueInfo.getInviteUid());
        jsonObject.put("position", roomQueueInfo.getPosition());
        JSONObject object = new JSONObject();
        object.put("uid", uid);
        object.put("data", jsonObject);
        return object;
    }
}
