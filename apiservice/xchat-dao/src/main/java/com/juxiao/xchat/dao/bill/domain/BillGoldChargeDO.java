package com.juxiao.xchat.dao.bill.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 用户充值账单
 *
 * @class: BillGoldChargeDO.java
 * @author: chenjunsheng
 * @date 2018/5/15
 */
@Getter
@Setter
@Accessors(chain = true)
public class BillGoldChargeDO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 支付的用户ID
     */
    private Long uid;
    /**
     * 充值获得的金币
     */
    private Integer goldAmount;
    /**
     * 用户充值金额，单位：分
     */
    private Integer money;
    /**
     * charge_record关联
     */
    private String chargeId;
    /**
     * 订单创建时间
     */
    private Date createTime;
}
