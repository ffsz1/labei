package com.tongdaxing.xchat_core.msg;

/**
 * Created by zhouxiangfeng on 2017/4/29.
 */
public class Message {
    private String title;
    private String content;
    private int type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}