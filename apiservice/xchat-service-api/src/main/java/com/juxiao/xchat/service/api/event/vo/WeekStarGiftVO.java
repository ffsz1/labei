package com.juxiao.xchat.service.api.event.vo;

import lombok.Data;

/**
 * @author chris
 * @Title:
 * @date 2019-05-20
 * @time 10:34
 */
@Data
public class WeekStarGiftVO {

    /**
     * 礼物ID
     */
    private Integer giftId;
    /**
     * 礼物名称
     */
    private String giftName;
    /**
     *     礼物价格
      */
    private Long goldPrice;
    /**
     * 礼物图片
     */
    private String picUrl;
    /**
     * 排序号
     */
    private Integer seq;
}
