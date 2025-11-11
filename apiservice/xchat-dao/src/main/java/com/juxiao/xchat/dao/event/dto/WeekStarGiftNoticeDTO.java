package com.juxiao.xchat.dao.event.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author chris
 * @Title: 礼物预告
 * @date 2019-05-20
 * @time 11:27
 */
@Data
public class WeekStarGiftNoticeDTO {


    private Integer id;

    private Integer giftId;

    private Integer adminId;

    private Integer status;

    private Date createTime;

    private Integer seq;
}
