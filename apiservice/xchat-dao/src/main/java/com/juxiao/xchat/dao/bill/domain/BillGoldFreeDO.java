package com.juxiao.xchat.dao.bill.domain;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 赠送金币账单
 *
 * @class: BillGoldFreeDO.java
 * @author: chenjunsheng
 * @date 2018/5/15
 */
@Getter
@Setter
public class BillGoldFreeDO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 用户ID
     */
    private Long uid;
    /**
     * 免费赠送金币入口：1，官方直接送；2，关注公众号送；3，活动奖励金币；4，打款至公账充值金币
     */
    private Byte freeSource;
    /**
     * 赠送金币
     */
    private Integer goldAmount;
    /**
     * 操作用户
     */
    private Long opUid;
    /**
     * 创建时间
     */
    private Date createTime;
}
