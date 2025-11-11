package com.juxiao.xchat.dao.user.query;

import lombok.Data;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;

/**
 * @Auther: alwyn
 * @Description: 捡海螺流水查询
 * @Date: 2018/10/23 18:18
 */
@Data
public class DrawGiftRecordQuery {

    private Long roomId;
    private Date beginDate;

    public DrawGiftRecordQuery(Long roomId, Date beginDate) {
        this.roomId = roomId;
        this.beginDate = beginDate;
    }

    /**
     *
     * @return
     */
    public Date getBeginDate() {
        if (beginDate == null) {
            return DateUtils.addDays(new Date(), 14);
        }
        return beginDate;
    }
}
