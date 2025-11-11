package com.juxiao.xchat.dao.bill.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 查询用户充值金币账单返回对象
 *
 * @class: BillGoldUserChargeDTO.java
 * @author: chenjunsheng
 * @date 2018/5/17
 */
@Getter
@Setter
public class BillGoldUserChargeDTO extends BaseUserBillDTO {
    /**
     * 用户昵称
     */
    private String srcNick;
    /**
     * 用户头像
     */
    private String srcAvatar;
    /**
     * 发起动作用户昵称
     */
    private String targetNick;
    /**
     * 发起动作用户头像
     */
    private String targetAvatar;
    /**
     * 礼物数量
     */
    private int giftNum = 1;
    /**
     * 获得金币
     */
    private String goldNum;
    /**
     * 充值金额
     */
    private Integer money;
    /**
     * 显示字符串
     */
    private String showStr;
}
