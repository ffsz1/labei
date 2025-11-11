package com.juxiao.xchat.dao.charge.domain;

import lombok.Data;

import java.util.Date;

@Data
public class UserGiveRecordDo {
    private String giveId;
    private Long sendUid;
    private Long recvUid;
    private Integer gold;
    private Long sendBeforeGoldNum;
    private Long sendAfterGoldNum;
    private Long recvBeforeGoldNum;
    private Long recvAfterGoldNum;
    private String giveDesc; // 赠送描述
    private Date createTime;
}
