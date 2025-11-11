package com.juxiao.xchat.service.api.user.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDrawWinRecordVO {
    private Long uid;
    private UserVO userVo;
    private Byte drawStatus;//中奖纪录状态，1创建，2未中奖，3已中奖
    private Byte type;
    private String srcObjName;
    private String drawPrizeName;
}
