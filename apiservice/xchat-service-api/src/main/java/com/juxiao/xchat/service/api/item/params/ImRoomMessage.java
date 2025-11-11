package com.juxiao.xchat.service.api.item.params;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author chris
 * @Title:
 * @date 2019-05-21
 * @time 16:02
 */
@Data
public class ImRoomMessage {

    @JSONField(name = "room_id")
    private String roomId;

    private Custom custom;

    private Long uid;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
