package com.juxiao.xchat.dao.bill.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 用户礼物抽奖账单领域层
 *
 * @class: BillGiftDrawDO.java
 * @author: chenjunsheng
 * @date 2018/5/15
 */
@Getter
@Setter
public class BillGiftDrawDO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 抽礼物用户
     */
    private Long uid;
    /**
     * 抽到礼物ID
     */
    private Integer giftId;
    /**
     * 抽到礼物数量
     */
    private Integer giftNum;
    /**
     * 抽到礼物价值总金币
     */
    private Integer goldCost;
    /**
     * 抽奖时间
     */
    private Date createTime;

    public BillGiftDrawDO(Long uid, Integer giftId, Integer giftNum, Integer goldCost, Date createTime) {
        this.uid = uid;
        this.giftId = giftId;
        this.giftNum = giftNum;
        this.goldCost = goldCost;
        this.createTime = createTime;
    }



}
