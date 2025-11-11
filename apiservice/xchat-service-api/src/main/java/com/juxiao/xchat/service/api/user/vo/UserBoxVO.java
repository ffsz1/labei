package com.juxiao.xchat.service.api.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserBoxVO {
    @ApiModelProperty(name = "userCharge", value = "用户充值金额")
    private Integer userCharge;
    @ApiModelProperty(name = "boxNum", value = "剩余礼盒次数")
    private Integer boxNum;
}
