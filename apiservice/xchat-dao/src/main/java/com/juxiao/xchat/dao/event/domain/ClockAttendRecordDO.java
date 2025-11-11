package com.juxiao.xchat.dao.event.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ClockAttendRecordDO {
    private Integer clockRecordId;
    private Date clockDate;
    private Long uid;
    private Date createTime;
}