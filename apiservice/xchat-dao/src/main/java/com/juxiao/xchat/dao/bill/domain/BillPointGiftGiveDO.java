package com.juxiao.xchat.dao.bill.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 赠送送礼物账单
 *
 * @class: BillGiftGiveDO.java
 * @author: chenjunsheng
 * @date 2018/5/15
 */
@Getter
@Setter
public class BillPointGiftGiveDO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 房间ID
     */
    private Long roomUid;
    /**
     * 送礼物的用户
     */
    private Long giverUid;
    /**
     * 收礼物的用户
     */
    private Long receiverUid;
    /**
     * 赠送礼物关联
     */
    private Long recordId;

    /**
     * 赠送礼物的ID
     */
    private Integer giftId;
    /**
     * 赠送礼物数量
     */
    private Integer giftNum;
    /**
     * 礼物金币总价值
     */
    private Integer goldCost;
    /**
     * 创建时间
     */
    private Date createTime;
}
