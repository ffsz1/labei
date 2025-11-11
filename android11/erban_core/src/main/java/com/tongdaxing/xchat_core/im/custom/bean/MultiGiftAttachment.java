package com.tongdaxing.xchat_core.im.custom.bean;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tongdaxing.xchat_core.gift.MultiGiftReceiveInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenran on 2017/10/25.
 */

public class MultiGiftAttachment extends IMCustomAttachment {

    private MultiGiftReceiveInfo multiGiftRecieveInfo;
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public MultiGiftAttachment(int first, int second) {
        super(first, second);
    }

    public MultiGiftReceiveInfo getMultiGiftRecieveInfo() {
        return multiGiftRecieveInfo;
    }

    public void setMultiGiftAttachment(MultiGiftReceiveInfo multiGiftRecieveInfo) {
        this.multiGiftRecieveInfo = multiGiftRecieveInfo;
    }

    //财富等级
    private int experLevel;
    //魅力等级
    private int charmLevel;

    public int getExperLevel() {
        return experLevel;
    }

    public void setExperLevel(int experLevel) {
        this.experLevel = experLevel;
    }

    public int getCharmLevel() {
        return charmLevel;
    }

    public void setCharmLevel(int charmLevel) {
        this.charmLevel = charmLevel;
    }

    @Override
    protected void parseData(JSONObject data) {
        multiGiftRecieveInfo = new MultiGiftReceiveInfo();
        multiGiftRecieveInfo.setExperLevel(data.getInteger("experLevel"));
        multiGiftRecieveInfo.setUid(data.getLong("uid"));
        multiGiftRecieveInfo.setGiftId(data.getInteger("giftId"));
        multiGiftRecieveInfo.setAvatar(data.getString("avatar"));
        multiGiftRecieveInfo.setNick(data.getString("nick"));
        multiGiftRecieveInfo.setGiftNum(data.getIntValue("giftNum"));
        multiGiftRecieveInfo.setGiftSendTime(data.getLong("giftSendTime"));
        JSONArray jsonArray = data.getJSONArray("targetUids");
        List<Long> targetUids = new ArrayList<>();
        int roomUid = data.getIntValue("roomUid");
        for (int i = 0; i < jsonArray.size(); i++) {
            Long uid = jsonArray.getLong(i);
            targetUids.add(uid);
        }
        multiGiftRecieveInfo.setTargetUids(targetUids);
        multiGiftRecieveInfo.setRoomUid(String.valueOf(roomUid));
    }

    @Override
    protected JSONObject packData() {
        JSONObject object = new JSONObject();
        object.put("uid", multiGiftRecieveInfo.getUid());
        object.put("giftId", multiGiftRecieveInfo.getGiftId());
        object.put("avatar", multiGiftRecieveInfo.getAvatar());
        object.put("nick", multiGiftRecieveInfo.getNick());
        object.put("giftNum", multiGiftRecieveInfo.getGiftNum());
        object.put("giftSendTime", multiGiftRecieveInfo.getGiftSendTime());
        object.put("experLevel", multiGiftRecieveInfo.getExperLevel());
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < multiGiftRecieveInfo.getTargetUids().size(); i++) {
            Long uid = multiGiftRecieveInfo.getTargetUids().get(i);
            jsonArray.add(uid);

        }
        object.put("roomUid", multiGiftRecieveInfo.getRoomUid());
        object.put("targetUids", jsonArray);
        return object;
    }
}
