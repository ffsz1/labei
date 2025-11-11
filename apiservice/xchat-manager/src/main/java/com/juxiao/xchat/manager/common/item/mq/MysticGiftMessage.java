package com.juxiao.xchat.manager.common.item.mq;

import lombok.Data;

/**
 * @Auther: alwyn
 * @Description: 神秘礼物消息
 * @Date: 2018/9/13 11:38
 */
@Data
public class MysticGiftMessage {
    /** 消息ID 唯一 */
    private String messId;
    /** 消息创建时间 */
    private Long messTime;
    /** 收到礼物的用户 */
    private Long uid;
    /** 送礼物的用户 */
    private Long sendUid;
    /** 送礼物的房间 */
    private Long roomUid;
    /** 送的礼物 */
    private Integer giftId;
    /** 礼物数量 */
    private Integer giftNum;
    /** 礼物总金额 */
    private Long goldNum;
    /** 神秘礼物ID */
    private Integer mysticId;
    /** 神秘礼物数量 */
    private Integer mysticNum;
}
