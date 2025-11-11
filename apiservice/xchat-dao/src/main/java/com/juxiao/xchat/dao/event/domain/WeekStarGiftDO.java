package com.juxiao.xchat.dao.event.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author chris
 * @Title:
 * @date 2019-05-20
 * @time 17:05
 */
@Data
public class WeekStarGiftDO {

    private Long id;

    private Integer status;

    private Date createTime;

    private Integer giftId;

    private Integer adminId;

    private Integer seq;
}
