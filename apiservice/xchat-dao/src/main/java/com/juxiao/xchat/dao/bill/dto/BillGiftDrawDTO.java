package com.juxiao.xchat.dao.bill.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Auther: alwyn
 * @Description:
 * @Date: 2018/9/10 16:30
 */
@Data
public class BillGiftDrawDTO {
    /**
     * 礼物ID
     */
    private Integer giftId;
    /**
     * 礼物类型
     */
    private Integer giftType;
    /**
     * 礼物名称
     */
    private String giftName;
    /**
     * 抽到礼物数量
     */
    private Integer giftNum;
    /**
     * 抽到礼物价值总金币
     */
    private Integer goldCost;
    /**
     * 抽到的礼物价值
     */
    private Integer goldPrice;
    /**
     * 抽奖时间
     */
    private Date createTime;
    /**
     * 礼物图片
     */
    private String picUrl;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 用户昵称
     */
    private String nick;
    /**
     * 用户性别
     */
    private Byte gender;
}
