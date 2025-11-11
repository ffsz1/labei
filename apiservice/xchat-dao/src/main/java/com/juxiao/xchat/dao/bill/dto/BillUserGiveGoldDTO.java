package com.juxiao.xchat.dao.bill.dto;

import lombok.Data;

/**
 * 用户赠送金币记录
 */

@Data
public class BillUserGiveGoldDTO extends BaseUserBillDTO {
    /**
     * 赠送者用户Uid
     */
    private String sendUid;
    /**
     * 赠送者用户头像
     */
    private String sendAvatar;
    /**
     * 赠送者用户昵称
     */
    private String sendNick;
    /**
     * 接受者用户Uid
     */
    private String recvUid;
    /**
     * 接受者用户头像
     */
    private String recvAvatar;
    /**
     * 接受者用户昵称
     */
    private String recvNick;

    /**
     * 赠送金币
     */
    private Integer goldNum;
}
