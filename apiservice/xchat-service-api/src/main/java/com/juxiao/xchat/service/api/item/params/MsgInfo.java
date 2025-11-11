package com.juxiao.xchat.service.api.item.params;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author chris
 * @Title:
 * @date 2018/11/16
 * @time 15:22
 */
@Data
public class MsgInfo {

    @JSONField(name = "room_id")
    private Long roomId;

    private Custom custom;
}
