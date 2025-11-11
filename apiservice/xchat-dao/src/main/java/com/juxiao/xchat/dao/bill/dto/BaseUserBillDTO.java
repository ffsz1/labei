package com.juxiao.xchat.dao.bill.dto;

import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

/**
 * @class: BaseUserBillDTO.java
 * @author: chenjunsheng
 * @date 2018/5/16
 */
@Setter
public class BaseUserBillDTO {
    /**
     * 分组时间戳
     */
    private Long date;

    /**
     * 记录时间
     */
    private Date recordTime;

    public Long getDate() {
        if (date == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(recordTime);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            this.date = calendar.getTimeInMillis();
        }
        return date;
    }

    public Long getRecordTime() {
        return recordTime == null ? null : recordTime.getTime();
    }
}
