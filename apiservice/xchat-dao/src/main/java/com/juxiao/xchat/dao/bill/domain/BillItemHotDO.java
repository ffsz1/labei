package com.juxiao.xchat.dao.bill.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

/**
 * 购买推荐位账单
 *
 * @class: BillItemHotDO.java
 * @author: chenjunsheng
 * @date 2018/5/15
 */
@Getter
@Setter
public class BillItemHotDO {
    /**
     * 主键
     */
    private Long recordId;
    /**
     * 购买用户ID
     */
    private Long uid;
    /**
     * 消耗金币
     */
    private Integer goldCost;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 推荐位结束时间
     */
    private Date endTime;
}
