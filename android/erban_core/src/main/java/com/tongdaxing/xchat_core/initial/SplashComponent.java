package com.tongdaxing.xchat_core.initial;


import java.io.Serializable;

/**
 * @author xiaoyu
 * @date 2017/12/29
 */

public class SplashComponent implements Serializable {
    /**
     * 封面
     */
    private String pict;
    /**
     * 跳转的链接
     */
    private String link;
    /**
     * 跳转的类型
     * 1跳app页面，2跳聊天室，3跳h5页面
     */
    private int type;

    public String getPict() {
        return pict;
    }

    public void setPict(String pict) {
        this.pict = pict;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SplashComponent{" +
                "pict='" + pict + '\'' +
                ", link='" + link + '\'' +
                ", type=" + type +
                '}';
    }
}
