package com.juxiao.xchat.dao.event.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author chris
 * @Title:
 * @date 2019-05-20
 * @time 11:43
 */
@Data
public class WeekStarItemRewardDTO {

    private Integer id;

    /**
     * 1.有效2.无效
     */
    private Integer status;

    private Date createTime;

    /**
     * 礼物id
     */
    private Integer giftId;

    /**
     * 奖品id(座驾,头饰)
     */
    private Integer itemId;

    /**
     * 操作人id
     */
    private Integer adminId;

    /**
     * 类型 1.座驾 2.头饰
     */
    private Integer type;

    /**
     * 天数
     */
    private Integer days;

    /**
     * 内容描述
     */
    private String content;

    /**
     * 排序
     */
    private Integer seq;

}
