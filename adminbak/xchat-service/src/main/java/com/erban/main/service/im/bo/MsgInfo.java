package com.erban.main.service.im.bo;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author chris
 * @Title:
 * @date 2018/11/16
 * @time 15:22
 */
public class MsgInfo {

    @JSONField(name = "room_id")
    private Long roomId;

    private Custom custom;

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Custom getCustom() {
        return custom;
    }

    public void setCustom(Custom custom) {
        this.custom = custom;
    }
}
