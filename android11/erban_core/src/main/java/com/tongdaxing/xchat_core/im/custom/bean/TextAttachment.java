package com.tongdaxing.xchat_core.im.custom.bean;


import com.alibaba.fastjson.JSONObject;

/**
 * Created by Administrator on 2018/3/13.
 */

public class TextAttachment extends IMCustomAttachment {


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
        experLevel = data.getInteger("experLevel");

    }

    @Override
    protected JSONObject packData() {


        JSONObject jsonObject = new JSONObject();

        jsonObject.put("experLevel", experLevel);

        return jsonObject;
    }
}
