package com.juxiao.xchat.dao.bill.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 购买座驾账单
 *
 * @class: BillItemCarDO.java
 * @author: chenjunsheng
 * @date 2018/5/15
 */
@Getter
@Setter
public class BillItemCarDO {

    /**
     * 主键
     */
    private Long id;
    /**
     * 购买对应的座驾ID
     */
    private Long recordId;
    /**
     * 购买用户
     */
    private Long uid;
    /**
     * 消耗的金币
     */
    private Integer goldCost;
    /**
     * 创建时间
     */
    private Date createTime;

}
