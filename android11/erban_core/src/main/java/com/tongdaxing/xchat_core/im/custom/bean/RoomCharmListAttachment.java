package com.tongdaxing.xchat_core.im.custom.bean;


import com.alibaba.fastjson.JSONObject;


public class RoomCharmListAttachment extends IMCustomAttachment {

    private String params;

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public RoomCharmListAttachment(int first, int second) {
        super(first, second);
    }

    @Override
    protected void parseData(JSONObject data) {
        params = data.getString("params");
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("params", params);
        return jsonObject;
    }
}
