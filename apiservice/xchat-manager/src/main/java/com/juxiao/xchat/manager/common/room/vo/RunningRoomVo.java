package com.juxiao.xchat.manager.common.room.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RunningRoomVo {
    private Long uid;
    private int onlineNum;
    private Long roomId;
    private Byte type;
    private int calcSumDataIndex;//综合人气+流水值
    private int count;  // 计数器，用于指示某段时间内一直没人
}
