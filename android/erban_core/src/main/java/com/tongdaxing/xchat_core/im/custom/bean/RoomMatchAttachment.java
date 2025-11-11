package com.tongdaxing.xchat_core.im.custom.bean;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


/**
 * Created by Administrator on 2018/3/20.
 */

public class RoomMatchAttachment extends IMCustomAttachment {
    private long uid;
    private String nick;
    private int[] numArr;
    private boolean isShowd;

    public RoomMatchAttachment(int first, int second) {
        super(first, second);
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int[] getNumArr() {
        return numArr;
    }

    public void setNumArr(int[] numArr) {
        this.numArr = numArr;
    }

    public boolean isShowd() {
        return isShowd;
    }

    public void setShowd(boolean showd) {
        isShowd = showd;
    }

    @Override
    protected void parseData(JSONObject data) {
        uid = data.getLong("uid");
        nick = data.getString("nick");
        isShowd = data.getBoolean("isShowd");
        experLevel = data.getIntValue("level");
        JSONArray array = data.getJSONArray("numArr");
        if (array != null && !array.isEmpty()) {
            numArr = new int[array.size()];
            for (int i = 0; i < array.size();i++) {
                numArr[i] = array.getIntValue(i);
            }
        }
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uid", uid);
        jsonObject.put("nick", nick);
        jsonObject.put("isShowd", isShowd);
        jsonObject.put("level",experLevel);
        JSONArray array = new JSONArray();
        if (numArr != null && numArr.length > 0) {
            for (int i = 0; i < numArr.length;i++) {
                array.add(numArr[i]);
            }
        }
        jsonObject.put("numArr",array);
        return jsonObject;
    }
}
