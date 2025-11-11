package com.juxiao.xchat.manager.common.item.mq;


import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 礼物消息
 */
@Getter
@Setter
public class GiftMessage implements Serializable {
    private String messId;    // 消息唯一标识
    private Long messTime;    // 消息创建时间
    private Long sendUid;     // 发送者UID
    private Long recvUid;     // 接收者UID
    private Long roomUid;     // 房间主UID
    private Byte roomType;    // 房间类型
    private Long goldNum;     // 金币数量
    private Long conchNum;     // 金币数量
    private Long afterGoldNum;     // 减去用户礼物后的金币数量
    private Long useGiftPurseNum;     // 剩余多少用户礼物
    private Double diamondNum;// 钻石数量
    private Double ownerDiamondNum;
    private Integer giftId;   // 礼物标识
    private Integer giftNum;  // 礼物数量
    private Byte sendType; // 赠送类型，如： 1对1送礼物，全麦送礼物
    private Integer beforeGiftId;   // 宝箱礼物id
    private String expressMessage; // 表白留言

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
