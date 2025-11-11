package com.juxiao.xchat.dao.user.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDrawGiftRecordDO {
    private Integer recordId;
    private Long uid;
    private Long roomId;
    private Integer giftId;
    private Integer num;
    private Integer type;
    private Integer doDrawType;
    private Date createTime;

    public UserDrawGiftRecordDO(Long uid, Long roomId, Integer giftId, Integer num, Integer type, Date createTime) {
        this.uid = uid;
        this.roomId = roomId;
        this.giftId = giftId;
        this.num = num;
        this.type = type;
        this.createTime = createTime;
    }

    public UserDrawGiftRecordDO(Long uid, Long roomId, Integer giftId, Integer num, Integer type, Integer doDrawType, Date createTime) {
        this.uid = uid;
        this.roomId = roomId;
        this.giftId = giftId;
        this.num = num;
        this.type = type;
        this.doDrawType = doDrawType;
        this.createTime = createTime;
    }
}