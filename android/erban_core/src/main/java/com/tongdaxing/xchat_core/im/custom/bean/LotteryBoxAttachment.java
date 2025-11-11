package com.tongdaxing.xchat_core.im.custom.bean;


import com.alibaba.fastjson.JSONObject;


/**
 * Created by Administrator on 2018/3/20.
 */

public class LotteryBoxAttachment extends IMCustomAttachment {
    private String giftName;
    private int count;
    private String nick;
    private boolean isFull = false;
    private int goldPrice;
    private long account;
    private int floatOpen;
    private long roomId;

    public LotteryBoxAttachment(int first, int second) {
        super(first, second);
    }

    @Override
    protected void parseData(JSONObject data) {
        JSONObject param = data.getJSONObject("params");
        goldPrice = param.getInteger("goldPrice");
        nick = param.getString("nick");
        giftName = param.getString("giftName");
        count = param.getIntValue("count");
        floatOpen = param.getIntValue("floatOpen");
        roomId = param.getLongValue("roomId");
        if (param.containsKey("isFull"))
            isFull = param.getBooleanValue("isFull");
        account = param.getIntValue("uid");
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObject = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("goldPrice", goldPrice);
        params.put("nick", nick);
        params.put("giftName", giftName);
        params.put("count", count);
        params.put("isFull", isFull);
        params.put("uid", account);
        params.put("roomId",roomId);
        params.put("floatOpen",floatOpen);
        jsonObject.put("params", params);
        return jsonObject;
    }

    public int getFloatOpen() {
        return floatOpen;
    }

    public void setFloatOpen(int floatOpen) {
        this.floatOpen = floatOpen;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public String getAccount() {
        return String.valueOf(account);
    }

    public void setAccount(long account) {
        this.account = account;
    }

    public int getGoldPrice() {
        return goldPrice;
    }

    public void setGoldPrice(int goldPrice) {
        this.goldPrice = goldPrice;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }
}

