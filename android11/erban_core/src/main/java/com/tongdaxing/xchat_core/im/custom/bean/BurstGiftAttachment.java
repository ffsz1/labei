package com.tongdaxing.xchat_core.im.custom.bean;

import com.alibaba.fastjson.JSONObject;

/**
 * 爆出礼物的消息
 * Created by zwk on 2018/9/13.
 */
public class BurstGiftAttachment extends IMCustomAttachment {
    private int giftId;
    private String giftName;
    private int giftNum;
    private int giftType;
    private String picUrl;
    private String vggUrl;

    private long uid;
    private String nick;
    private String avatar;

    private long sendUid;
    private String sendNick;
    private String sendAvatar;

    public BurstGiftAttachment(int first, int second) {
        super(first, second);
    }
    @Override
    protected void parseData(JSONObject data) {
        setGiftId(data.getInteger("giftId"));
        setGiftNum(data.getIntValue("giftNum"));
        setGiftName(data.getString("giftName"));
        setGiftType(data.getIntValue("giftType"));
        setPicUrl(data.getString("picUrl"));
        setVggUrl(data.getString("vggUrl"));

        setUid(data.getLong("uid"));
        setAvatar(data.getString("avatar"));
        setNick(data.getString("nick"));


        setSendUid(data.getLong("sendUid"));
        setSendNick(data.getString("sendNick"));
        setSendAvatar(data.getString("sendAvatar"));
    }

    @Override
    protected JSONObject packData() {
        JSONObject object = new JSONObject();
        object.put("giftId", getGiftId());
        object.put("giftName", getGiftName());
        object.put("giftNum", getGiftNum());
        object.put("picUrl",getPicUrl());
        object.put("vggUrl",getVggUrl());

        object.put("uid", getUid());
        object.put("avatar", getAvatar());
        object.put("nick", getNick());

        object.put("sendUid",getSendUid());
        object.put("sendNick", getSendNick());
        object.put("sendAvatar", getSendAvatar());
        object.put("giftType",getGiftType());
        return object;
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

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getVggUrl() {
        return vggUrl;
    }

    public void setVggUrl(String vggUrl) {
        this.vggUrl = vggUrl;
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

    public long getSendUid() {
        return sendUid;
    }

    public void setSendUid(long sendUid) {
        this.sendUid = sendUid;
    }

    public String getSendNick() {
        return sendNick;
    }

    public void setSendNick(String sendNick) {
        this.sendNick = sendNick;
    }

    public String getSendAvatar() {
        return sendAvatar;
    }

    public void setSendAvatar(String sendAvatar) {
        this.sendAvatar = sendAvatar;
    }

    public int getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(int giftNum) {
        this.giftNum = giftNum;
    }

    public int getGiftType() {
        return giftType;
    }

    public void setGiftType(int giftType) {
        this.giftType = giftType;
    }
}
