package com.juxiao.xchat.dao.event.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ClockResultRecord {
    private Integer clockResultId;
    private Date clockDate;
    private Integer uid;
    private Integer goldAmount;
    private Date createTime;
}