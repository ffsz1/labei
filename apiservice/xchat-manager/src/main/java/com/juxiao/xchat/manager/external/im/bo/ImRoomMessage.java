package com.juxiao.xchat.manager.external.im.bo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author chris
 * @Title:
 * @date 2018/11/16
 * @time 15:22
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
