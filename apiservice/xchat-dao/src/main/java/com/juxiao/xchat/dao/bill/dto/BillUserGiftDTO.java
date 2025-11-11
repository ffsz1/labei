package com.juxiao.xchat.dao.bill.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 礼物支出记录
 *
 * @class: BillUserGiftDTO.java
 * @author: chenjunsheng
 * @date 2018/5/16
 */
@Getter
@Setter
public class BillUserGiftDTO extends BaseUserBillDTO {
    /**
     * 赠送用户昵称
     */
    private String srcNick;
    /**
     * 来源用户头像
     */
    private String srcAvatar;
    /**
     * 接收用户昵称
     */
    private String targetNick;
    /**
     * 接收用户头像
     */
    private String targetAvatar;

    /**
     * 赠送礼品图片
     */
    private String giftPict;
    /**
     * 赠送礼品名称
     */
    private String giftName;
    /**
     * 赠送礼品数量
     */
    private Integer giftNum;
    /**
     * 消耗砖石数量
     */
    private Double diamondNum;
    /**
     * 对方收到金币数量
     */
    private Integer goldNum;
}
