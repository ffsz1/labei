package com.juxiao.xchat.dao.family.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author chris
 * @Title: 送礼物记录
 * @date 2018/11/26
 * @time 09:54
 */
@Data
public class FamilyGiftRecordDO {


    /**
     * 编号
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 家族ID
     */
    private Long teamId;

    /**
     * 礼物ID
     */
    private Integer giftId;

    /**
     * 数量
     */
    private Long num;

    /**
     * 创建时间
     */
    private Date createTime;
}
