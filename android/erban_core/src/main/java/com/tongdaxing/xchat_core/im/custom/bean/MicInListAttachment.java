package com.tongdaxing.xchat_core.im.custom.bean;


import com.alibaba.fastjson.JSONObject;


/**
 * Created by Administrator on 2018/3/20.
 */

public class MicInListAttachment extends IMCustomAttachment {



    private String params;






    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public MicInListAttachment(int first, int second) {
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
