package com.juxiao.xchat.dao.bill.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 使用抽奖礼物记录
 *
 * @class: BillGiftUseDO.java
 * @author: chenjunsheng
 * @date 2018/5/15
 */
@Getter
@Setter
public class BillGiftUseDO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 使用抽礼物用户
     */
    private Long uid;
    /**
     * 使用抽到礼物ID
     */
    private Integer giftId;
    /**
     * 使用抽到礼物数量
     */
    private Integer giftNum;
    /**
     * 使用时间
     */
    private Date createTime;
}
