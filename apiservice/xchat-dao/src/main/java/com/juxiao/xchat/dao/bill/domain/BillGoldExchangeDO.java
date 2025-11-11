package com.juxiao.xchat.dao.bill.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 钻石兑换金币账单
 *
 * @class: BillGoldExchangeDO.java
 * @author: chenjunsheng
 * @date 2018/5/15
 */
@Getter
@Setter
public class BillGoldExchangeDO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 关联记录ID
     */
    private String recordId;
    /**
     * 兑换用户ID
     */
    private Long uid;
    /**
     * 用户消耗钻石数量
     */
    private Double diamondCost;
    /**
     * 得到的金币数量
     */
    private Integer goldAmount;
    /**
     * 兑换时间
     */
    private Date createTime;
}
