package com.juxiao.xchat.dao.item.dto;

import com.juxiao.xchat.base.utils.DateFormatUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author laizhilong
 * @Title: 捡海螺统计记录
 * @Package com.erban.main.model.dto
 * @date 2018/8/9
 * @time 17:36
 */
@Getter
@Setter
public class RoomDrawRecordDTO {

    private Long uid;

    private String nick;

    private Long erbanNo;

    private Integer frequency;

    private Long totalGoldNum;

    private Date createTime;

    public String getDate() {
        return createTime == null ? "" : DateFormatUtils.YYYY_MM_DD.date2Str(createTime);
    }
}

