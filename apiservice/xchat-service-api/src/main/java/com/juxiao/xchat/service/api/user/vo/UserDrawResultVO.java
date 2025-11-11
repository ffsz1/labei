package com.juxiao.xchat.service.api.user.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户进行抽奖操作，返回结果
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDrawResultVO {
    private UserVO userVo;
    private Integer leftDrawNum;
    private Integer totalDrawNum;
    private Integer totalWinDrawNum;
    private Byte drawStatus;//中奖纪录状态，1创建，2未中奖，3已中奖
    private String srcObjName;
    private String drawPrizeName;
    private Integer drawPrizeId;

    private UserWordDrawResultVO wordDraw;

}
