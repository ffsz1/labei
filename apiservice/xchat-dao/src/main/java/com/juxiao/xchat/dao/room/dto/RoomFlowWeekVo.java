package com.juxiao.xchat.dao.room.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RoomFlowWeekVo {
    private Long uid;
    private Long firstWeek;// 当前时间往前第一周
    private Long secondWeek;// 当前时间往前第二周;
    private String proportion;
    private Date createDate;
}
