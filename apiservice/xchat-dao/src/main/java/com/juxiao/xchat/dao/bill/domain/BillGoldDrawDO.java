package com.juxiao.xchat.dao.bill.domain;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 抽奖得金币
 *
 * @class: BillGoldDrawDO.java
 * @author: chenjunsheng
 * @date 2018/5/17
 */
@Getter
@Setter
public class BillGoldDrawDO {

    private Long id;
    /**
     * 对应user_draw_record表主键
     */
    private Long recordId;
    /**
     * 用户ID
     */
    private Long uid;
    /**
     * 赠送金币
     */
    private Integer goldAmount;
    /**
     * 创建时间
     */
    private Date createTime;


}
