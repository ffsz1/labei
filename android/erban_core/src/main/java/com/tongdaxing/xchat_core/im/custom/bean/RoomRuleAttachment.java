package com.tongdaxing.xchat_core.im.custom.bean;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhouxiangfeng on 2017/6/8.
 */

public class RoomRuleAttachment extends IMCustomAttachment {

    private String rule;

    public RoomRuleAttachment(int first, int second) {
        super(first, second);
    }

    @Override
    protected void parseData(JSONObject data1) {
        this.setRule(data1.getString("rule"));
    }

    @Override
    protected JSONObject packData() {
        JSONObject object = new JSONObject();
        object.put("rule", rule);
        return object;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
}
