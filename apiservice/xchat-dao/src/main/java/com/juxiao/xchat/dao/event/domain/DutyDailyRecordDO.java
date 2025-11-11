package com.juxiao.xchat.dao.event.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DutyDailyRecordDO {
    private Long id;
    private Long uid;
    private Integer dutyId;
    private Byte udStatus;
    private Integer goldAmount;
    private Date createTime;
    private Date updateTime;
}
