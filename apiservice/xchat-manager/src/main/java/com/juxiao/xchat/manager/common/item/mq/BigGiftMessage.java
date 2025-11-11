package com.juxiao.xchat.manager.common.item.mq;


import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 大额礼物消息，全服消息
 */
@Getter
@Setter
public class BigGiftMessage implements Serializable {
    private String messId;    // 消息唯一标识
    private Long messTime;    // 消息创建时间
    private Long uid;
    private Long targetUid;
    private Long roomUid;
    private int giftId;
    private int giftNum;
    private Long[] targetSize;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
