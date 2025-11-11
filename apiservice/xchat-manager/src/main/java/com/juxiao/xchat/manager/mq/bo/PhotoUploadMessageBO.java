package com.juxiao.xchat.manager.mq.bo;

import lombok.Data;

@Data
public class PhotoUploadMessageBO {

    private long uid;
    private String photoUrl;

    public PhotoUploadMessageBO(long uid, String photoUrl) {
        this.uid = uid;
        this.photoUrl = photoUrl;
    }
}
