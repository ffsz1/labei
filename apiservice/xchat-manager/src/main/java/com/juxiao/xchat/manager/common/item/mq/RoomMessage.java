package com.juxiao.xchat.manager.common.item.mq;


import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 房间公屏消息
 */
@Getter
@Setter
public class RoomMessage implements Serializable {
    private String messId;    // 消息唯一标识
    private Long messTime;    // 消息创建时间
    private Long uid;
    private Long roomId;
    private Object data;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
