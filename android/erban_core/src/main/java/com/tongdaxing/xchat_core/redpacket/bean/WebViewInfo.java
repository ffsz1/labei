package com.tongdaxing.xchat_core.redpacket.bean;

import java.io.Serializable;

/**
 * Created by ${Seven} on 2017/9/25.
 */

public class WebViewInfo implements Serializable {
    private String title;
    private String imgUrl;
    private String desc;
    private String showUrl;

    @Override
    public String toString() {
        return "WebViewInfo{" +
                "title='" + title + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", desc='" + desc + '\'' +
                ", showUrl='" + showUrl + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getShowUrl() {
        return showUrl;
    }

    public void setShowUrl(String showUrl) {
        this.showUrl = showUrl;
    }
}
