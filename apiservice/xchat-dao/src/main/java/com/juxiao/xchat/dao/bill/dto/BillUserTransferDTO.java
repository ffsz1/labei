package com.juxiao.xchat.dao.bill.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户提现记录
 *
 * @class: BillUserTransferDTO.java
 * @author: chenjunsheng
 * @date 2018/5/16
 */
@Data
public class BillUserTransferDTO extends BaseUserBillDTO {
    /**
     * 赠送用户昵称
     */
    private String srcNick;
    /**
     * 来源用户头像
     */
    private String srcAvatar;
    /**
     * 赠送礼品数量
     */
    private Integer giftNum;
    /**
     * 消耗钻石数量
     */
    private Double diamondNum;
    /**
     * 提现金额
     */
    private Integer money;
}
