package com.juxiao.xchat.dao.room.domain;

import lombok.Data;

import java.util.Date;

@Data
public class RoomRcmdDTO {
    private Integer rcmdId;
    private Integer minOnline;
    private Byte rcmdType;
    private Date startDate;
    private Date endDate;

}