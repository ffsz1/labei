package com.juxiao.xchat.dao.user.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDrawGiftRoomDayDO {
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
