package com.juxiao.xchat.manager.common.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserPurseVO {
    private Long uid;
    private Long goldNum;
    @ApiModelProperty(value = "剩余捡海螺次数")
    private Long conchNum;
    private Long chargeGoldNum;
    private Long nobleGoldNum;
    private Double diamondNum;
    private Long depositNum;
    private Boolean isFirstCharge;
}
