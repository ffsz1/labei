package com.juxiao.xchat.dao.room.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @class: RoomContributionQuery.java
 * @author: chenjunsheng
 * @date 2018/6/5
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomContributionQuery {
    /**
     * 房主ID
     */
    private Long roomUid;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;


}