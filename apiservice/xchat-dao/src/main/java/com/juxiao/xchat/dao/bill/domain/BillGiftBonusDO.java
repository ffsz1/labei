package com.juxiao.xchat.dao.bill.domain;


import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

/**
 * 钻石回馈账单
 *
 * @class: BillGiftBonusDO.java
 * @author: chenjunsheng
 * @date 2018/5/17
 */
@Getter
@Setter
public class BillGiftBonusDO {
    /**
     * 主键
     */
    private BigInteger id;
    /**
     * 绑定反馈记录
     */
    private Integer bonusId;
    /**
     * 接收钻石用户ID
     */
    private Long uid;
    /**
     * 反馈赠送的钻石数量
     */
    private Double diamondAmount;
    /**
     * 创建时间
     */
    private Date createTime;
}
