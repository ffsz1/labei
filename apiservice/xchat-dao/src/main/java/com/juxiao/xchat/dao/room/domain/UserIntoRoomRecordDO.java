package com.juxiao.xchat.dao.room.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class UserIntoRoomRecordDO {
    private Long id;
    private Long uid;
    private Long roomUid;
    private Date createDate;
    private String type;
}
