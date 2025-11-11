package com.juxiao.xchat.service.api.item.mq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * @author chris
 * @Title:
 * @date 2019-05-08
 * @time 12:01
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GiftDrawMessage {
    /***
     * 消息唯一标识
     */
    private String messageId;
    /**
     * 消息创建时间
     */
    private Long createTime;
    /**
     * 房间ID
     */
    private Long roomId;
    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 捡海螺类型
     */
    private Integer drawType;
    /**
     * 捡海螺结果
     */
    Map<Integer, Integer> result;
}
