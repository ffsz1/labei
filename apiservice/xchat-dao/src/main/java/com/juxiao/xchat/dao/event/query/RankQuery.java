package com.juxiao.xchat.dao.event.query;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class RankQuery {

    private Long uid;
    private Date startTime;
    private Date endTime;
}
