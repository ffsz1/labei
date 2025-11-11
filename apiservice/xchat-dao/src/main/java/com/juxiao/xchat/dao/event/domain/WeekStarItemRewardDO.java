package com.juxiao.xchat.dao.event.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author chris
 * @Title:
 * @date 2019-05-21
 * @time 10:31
 */
@Data
public class WeekStarItemRewardDO {

    private Long id;

    private Integer status;

    private Date createTime;

    private Integer giftId;

    private Integer itemId;

    private Integer adminId;

    private Integer type;

    private Integer days;

    private String content;

    private Integer seq;
}
