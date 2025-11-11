package com.juxiao.xchat.service.api.charge.vo;

import lombok.Data;

@Data
public class TransferG2GVo {
    private long sendUid;
    private String sendName;
    private String sendAvatar;
    private long recvUid;
    private String recvName;
    private String recvAvatar;
    private int goldNum;
}
