package com.juxiao.xchat.dao.room.domain;

import lombok.Data;

import java.util.Date;

@Data
public class RoomGameRecordDO {
    private Integer recordId;
    private Long uid;
    private Long roomId;
    private Byte type;
    private String result;
    private Byte gameStatus;
    private Date createTime;

}