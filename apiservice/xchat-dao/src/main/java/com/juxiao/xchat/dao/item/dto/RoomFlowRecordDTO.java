package com.juxiao.xchat.dao.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.juxiao.xchat.base.utils.DateFormatUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomFlowRecordDTO {
    private Long uid;
    private Long totalGoldNum;
    private String date;
    private Date createTime;

    public String getDate() {
        return DateFormatUtils.YYYY_MM_DD.date2Str(createTime);
    }
}
