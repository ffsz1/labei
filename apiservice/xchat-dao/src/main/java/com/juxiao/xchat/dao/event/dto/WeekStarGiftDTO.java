package com.juxiao.xchat.dao.event.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author chris
 * @Title: 周星礼物
 * @date 2019-05-20
 * @time 10:32
 */
@Data
public class WeekStarGiftDTO {

    private Long id;

    private Integer status;

    private Date createTime;

    private Integer giftId;

    private Integer adminId;

    private Integer seq;
}
