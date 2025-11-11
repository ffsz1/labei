package com.juxiao.xchat.service.api.event.vo;

import lombok.Data;

/**
 * @author chris
 * @Title: 周星奖励
 * @date 2019-05-20
 * @time 10:43
 */
@Data
public class WeekStarItemRewardVO {

    /**
     * 礼物ID
     */
    private Integer giftId;
    /**
     * 礼物名称
     */
    private String giftName;

    /**
     * 奖项ID
     */
    private Integer itemId;
    /**
     * 名称
     */
    private String itemName;

    /**
     * 价格
     */
    private Long itemPrice;

    /**
     * 天数
     */
    private Integer itemDays;

    /**
     * 内容
     */
    private String itemContent;

    /**
     * 排序
     */
    private Integer itemSeq;
}
