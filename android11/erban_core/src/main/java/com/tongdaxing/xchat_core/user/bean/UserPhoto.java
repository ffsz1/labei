package com.tongdaxing.xchat_core.user.bean;
import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by chenran on 2017/7/25.
 */

public class UserPhoto extends RealmObject implements Serializable {

    @PrimaryKey
    private long pid;
    //图片地址
    private String photoUrl;

    public UserPhoto() {
    }

    public UserPhoto(long pid, String photoUrl) {
        this.pid = pid;
        this.photoUrl = photoUrl;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
