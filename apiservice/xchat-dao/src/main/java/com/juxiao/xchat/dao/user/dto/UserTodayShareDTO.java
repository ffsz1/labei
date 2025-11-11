package com.juxiao.xchat.dao.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("UserTodayShare")
public class UserTodayShareDTO {
    @ApiModelProperty(name = "uid", value = "被邀请人id")
    private Long uid;
    @ApiModelProperty(name = "avater", value = "被邀请人头像")
    private String avater;
    @ApiModelProperty(name = "newcomer", value = "是否新人：true，新人")
    private Boolean newcomer;
    @ApiModelProperty(name = "freeDrawCount", value = "增加的免费抽奖次数")
    private Integer freeDrawCount;

}
