package com.juxiao.xchat.dao.item.query;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ActivitHtmlQuery {

    private Integer giftId;
    private Byte gender;
    private Date startDate;
    private Date endDate;
    // 数据条数
    private long total;

}
