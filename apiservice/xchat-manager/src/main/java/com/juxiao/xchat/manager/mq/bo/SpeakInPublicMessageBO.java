package com.juxiao.xchat.manager.mq.bo;

import lombok.Data;

@Data
public class SpeakInPublicMessageBO {

    private Long uid;

    public SpeakInPublicMessageBO(Long uid) {
        this.uid = uid;
    }
}
