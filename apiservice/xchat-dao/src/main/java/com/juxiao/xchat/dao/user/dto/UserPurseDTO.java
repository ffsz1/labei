package com.juxiao.xchat.dao.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 用户钱包
 *
 * @class: UserPurseDTO.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */

@Data
public class UserPurseDTO{
    private Long uid;
    private Long chargeGoldNum;
    private Long nobleGoldNum;
    private Long goldNum;
    @ApiModelProperty(value = "剩余捡海螺次数")
    private Long conchNum;
    private Long trycoinNum;
    private Double diamondNum;
    private Long depositNum;
    private Boolean isFirstCharge;
    private Date firstRechargeTime;
    private Date updateTime;
}