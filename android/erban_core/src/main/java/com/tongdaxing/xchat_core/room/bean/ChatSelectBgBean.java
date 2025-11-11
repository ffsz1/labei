package com.tongdaxing.xchat_core.room.bean;

/**
 * Created by Administrator on 2018/3/23.
 */

public class ChatSelectBgBean {
    public ChatSelectBgBean() {
    }

    public ChatSelectBgBean(String bgId, int isSelect,String backPicUrl,String name) {
        this.id = bgId;
        this.isSelect = isSelect;
        this.picUrl = backPicUrl;
        this.name = name;
    }

    public String id;
    public int isSelect;
    public String picUrl;
    private String name;

    public interface BgNo {

    }

    public String getBackName() {
        return name;
    }

    public void setBackName(String backName) {
        this.name = backName;
    }
}
