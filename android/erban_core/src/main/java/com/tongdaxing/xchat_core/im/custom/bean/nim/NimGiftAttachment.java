package com.tongdaxing.xchat_core.im.custom.bean.nim;

import com.alibaba.fastjson.JSONObject;
import com.tongdaxing.xchat_core.gift.GiftReceiveInfo;
import com.tongdaxing.xchat_core.im.custom.bean.CustomAttachment;

/**
 * Created by chenran on 2017/7/28.
 */

public class NimGiftAttachment extends CustomAttachment {
    private GiftReceiveInfo giftRecieveInfo;
    private String uid;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public NimGiftAttachment(int first, int second) {
        super(first, second);
    }


    public GiftReceiveInfo getGiftRecieveInfo() {
        return giftRecieveInfo;
    }

    public void setGiftRecieveInfo(GiftReceiveInfo giftRecieveInfo) {
        this.giftRecieveInfo = giftRecieveInfo;
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

        giftRecieveInfo = new GiftReceiveInfo();
        giftRecieveInfo.setUid(data.getLong("uid"));
        giftRecieveInfo.setGiftId(data.getInteger("giftId"));
        giftRecieveInfo.setAvatar(data.getString("avatar"));
        giftRecieveInfo.setNick(data.getString("nick"));
        giftRecieveInfo.setTargetUid(data.getLong("targetUid"));
        giftRecieveInfo.setGiftNum(data.getIntValue("giftNum"));
        giftRecieveInfo.setTargetNick(data.getString("targetNick"));
        giftRecieveInfo.setTargetAvatar(data.getString("targetAvatar"));
        giftRecieveInfo.setRoomUid(data.getString("roomId"));
        giftRecieveInfo.setUserNo(data.getString("userNo"));
        experLevel = data.getInteger("experLevel");
    }

    @Override
    protected JSONObject packData() {
        JSONObject object = new JSONObject();

        object.put("uid", giftRecieveInfo.getUid());
        object.put("giftId", giftRecieveInfo.getGiftId());
        object.put("avatar", giftRecieveInfo.getAvatar());
        object.put("nick", giftRecieveInfo.getNick());
        object.put("targetUid", giftRecieveInfo.getTargetUid());
        object.put("giftNum", giftRecieveInfo.getGiftNum());
        object.put("targetNick", giftRecieveInfo.getTargetNick());
        object.put("targetAvatar", giftRecieveInfo.getTargetAvatar());
        object.put("roomId", giftRecieveInfo.getRoomUid());
        object.put("userNo", giftRecieveInfo.getUserNo());
        object.put("experLevel", experLevel);
        return object;
    }

    @Override
    public String toString() {
        return "GiftAttachment{" +
                "giftRecieveInfo=" + giftRecieveInfo +
                ", uid='" + uid + '\'' +
                ", experLevel=" + experLevel +
                ", charmLevel=" + charmLevel +
                '}';
    }
}
