package com.juxiao.xchat.dao.room.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class StatRoomCtrbSumDO {
    private Integer ctrbId;
    private Long uid;
    private Long ctrbUid;
    private Long sumGold;
    private Date createTime;
    private Date updateTime;
}