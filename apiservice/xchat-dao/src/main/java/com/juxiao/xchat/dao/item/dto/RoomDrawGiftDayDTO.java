package com.juxiao.xchat.dao.item.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RoomDrawGiftDayDTO {
    /**
     * 房间ID
     */
    private Long roomId;
    /**
     * 统计的日期
     */
    private Date createDate;
    /**
     * 捡海螺次数
     */
    private Integer drawCount;
    /**
     * 捡海螺总金币流水
     */
    private Integer goldCount;
}
