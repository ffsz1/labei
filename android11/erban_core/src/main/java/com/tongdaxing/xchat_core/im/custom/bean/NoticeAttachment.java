package com.tongdaxing.xchat_core.im.custom.bean;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by chenran on 2017/9/22.
 */

public class NoticeAttachment extends CustomAttachment {
    private String title;
    private String desc;
    private String picUrl;
    private String webUrl;

    public NoticeAttachment(int first, int second) {
        super(first, second);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    @Override
    protected void parseData(JSONObject data) {
        super.parseData(data);
        title = data.getString("title");
        desc = data.getString("desc");
        picUrl = data.getString("picUrl");
        webUrl = data.getString("webUrl");
    }

    @Override
    protected JSONObject packData() {
        JSONObject object = new JSONObject();
        object.put("title", title);
        object.put("desc", desc);
        object.put("picUrl", picUrl);
        object.put("webUrl", webUrl);
        return object;
    }
}
