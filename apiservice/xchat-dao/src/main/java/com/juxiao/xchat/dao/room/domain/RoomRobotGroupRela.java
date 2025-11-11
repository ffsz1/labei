package com.juxiao.xchat.dao.room.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomRobotGroupRela {
    private Integer relaId;
    private Long uid;
    private Long roomId;
    private Integer groupNo;
    private Byte status;
}