package com.juxiao.xchat.service.api.event.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChipParamVo {

    private Long uid;
    private Long chipId;
    private Integer chipSource;
    private Long sendUid;
    private Integer num;
    private Long cardId;
    private Integer operateType;

}
