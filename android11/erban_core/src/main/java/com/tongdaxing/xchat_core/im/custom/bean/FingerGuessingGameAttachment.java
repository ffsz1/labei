package com.tongdaxing.xchat_core.im.custom.bean;


import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tongdaxing.xchat_core.room.bean.FingerGuessingGameInfo;

/**
 * Function:
 * Author: Edward on 2019/6/20
 */
public class FingerGuessingGameAttachment extends IMCustomAttachment {
    private String timestamps;
    private long roomId;
    private String avatar;
    private int experienceLevel;
    private String giftUrl;
    private String nick;
    private int recordId;
    private int giftNum;
    private String opponentNick;
    private long uid;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(int giftNum) {
        this.giftNum = giftNum;
    }

    public String getOpponentNick() {
        return opponentNick;
    }

    public void setOpponentNick(String opponentNick) {
        this.opponentNick = opponentNick;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(int experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public String getGiftUrl() {
        return giftUrl;
    }

    public void setGiftUrl(String giftUrl) {
        this.giftUrl = giftUrl;
    }

    //    private FingerGuessingGameInfo moraRecordMessage;

    public FingerGuessingGameAttachment(int first, int second) {
        super(first, second);
    }

    public String getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(String timestamps) {
        this.timestamps = timestamps;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    //    public FingerGuessingGameInfo getMoraRecordMessage() {
//        return moraRecordMessage;
//    }
//
//    public void setMoraRecordMessage(FingerGuessingGameInfo moraRecordMessage) {
//        this.moraRecordMessage = moraRecordMessage;
//    }

    @Override
    protected void parseData(JSONObject data) {
        super.parseData(data);
        timestamps = data.getString("timestamps");
        roomId = data.getLong("roomId");
        String string = data.getString("moraRecordMessage");
        JSONObject object = JSON.parseObject(string);
        avatar = object.getString("avatar");
        nick = object.getString("nick");
        if (object.containsKey("uid")) {
            uid = object.getLong("uid");
        }
        if (object.containsKey("giftNum")) {
            giftNum = object.getInteger("giftNum");
        }
        recordId = object.getInteger("recordId");
        experienceLevel = object.getInteger("experienceLevel");
        if (object.containsKey("giftUrl")) {
            giftUrl = object.getString("giftUrl");
        }
        if (object.containsKey("opponentNick")) {
            opponentNick = object.getString("opponentNick");
        }
    }

    @Override
    protected JSONObject packData() {
        JSONObject object = new JSONObject();
        object.put("timestamps", timestamps);
        object.put("roomId", roomId);
        JSONObject info = new JSONObject();
        info.put("avatar", avatar);
        info.put("giftNum", giftNum);
        info.put("recordId", recordId);
        info.put("uid", uid);
        info.put("experienceLevel", experienceLevel);
        if (!TextUtils.isEmpty(giftUrl)) {
            info.put("giftUrl", giftUrl);
        }
        info.put("nick", nick);
        if (!TextUtils.isEmpty(opponentNick)) {
            info.put("opponentNick", opponentNick);
        }
        object.put("moraRecordMessage", JSONObject.toJSONString(info));
        return object;
    }
}
