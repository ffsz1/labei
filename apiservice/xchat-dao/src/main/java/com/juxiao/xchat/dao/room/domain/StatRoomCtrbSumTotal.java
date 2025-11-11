package com.juxiao.xchat.dao.room.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class StatRoomCtrbSumTotal {
    private Long uid;
    private Long flowSumTotal;
    private Date createTime;
}