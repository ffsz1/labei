package com.juxiao.xchat.dao.room.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RoomOpenHistDTO {
    private String histId;
    private Long uid;
    private Long roomId;
    private String meetingName;
    private Byte type;
    private Long rewardMoney;
    private Integer servDura;
    private Byte closeType;
    private Date openTime;
    private Date closeTime;
    private Double dura;
}