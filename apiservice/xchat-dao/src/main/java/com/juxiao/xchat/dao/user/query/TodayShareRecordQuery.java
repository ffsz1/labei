package com.juxiao.xchat.dao.user.query;

import com.juxiao.xchat.base.utils.DateTimeUtils;
import lombok.Getter;

import java.util.Date;

/**
 * @class: TodayShareRecordQuery.java
 * @author: chenjunsheng
 * @date 2018/6/14
 */
@Getter
public class TodayShareRecordQuery {

    private Long uid;

    private Date startDate;

    private Date endDate;

    public TodayShareRecordQuery(Long uid) {
        this.uid = uid;
        Date date = new Date();
        this.startDate = DateTimeUtils.setTime(date, 0, 0, 0);
        this.endDate = DateTimeUtils.setTime(date, 23, 59, 59);
    }

}
