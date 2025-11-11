package com.tongdaxing.xchat_core.im.custom.bean.nim;


import com.alibaba.fastjson.JSONObject;
import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment;


/**
 * Created by Administrator on 2018/3/20.
 */

public class SendCallGiftAttachment extends IMCustomAttachment {
    private String giftUrl;//礼物url
    private long roomId;
    private String sendName;//打call者的名字
    private String targetName;//目标名字
    private String giftName;//礼物名字
    private long uid;//打call者的uid

    public SendCallGiftAttachment(int first, int second) {
        super(first, second);
    }

    @Override
    protected void parseData(JSONObject data) {
        JSONObject param = data.getJSONObject("params");
        giftUrl = param.getString("giftUrl");
        targetName = param.getString("targetName");
        giftName = param.getString("giftName");
        uid = param.getLong("uid");
        roomId = param.getLongValue("roomId");
        sendName = param.getString("sendName");
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObject = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("giftUrl", giftUrl);
        params.put("targetName", targetName);
        params.put("giftName", giftName);
        params.put("uid", uid);
        params.put("roomId", roomId);
        params.put("sendName", sendName);
        jsonObject.put("params", params);
        return jsonObject;
    }

    public String getGiftUrl() {
        return giftUrl;
    }

    public void setGiftUrl(String giftUrl) {
        this.giftUrl = giftUrl;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }
}

