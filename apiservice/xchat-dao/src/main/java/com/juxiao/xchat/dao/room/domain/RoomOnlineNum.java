package com.juxiao.xchat.dao.room.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomOnlineNum {
    private Long uid;
    private Byte type;
    private Integer factor;
}