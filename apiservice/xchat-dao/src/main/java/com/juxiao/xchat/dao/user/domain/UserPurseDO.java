package com.juxiao.xchat.dao.user.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 用户钱包
 */
@Getter
@Setter
public class UserPurseDO {
    private Long uid;
    private Long chargeGoldNum;
    private Long nobleGoldNum;
    private Long goldNum;
    private Long trycoinNum;
    private Long conchNum;
    private Double diamondNum;
    private Long depositNum;
    private Boolean isFirstCharge;
    private Date firstRechargeTime;
    private Date updateTime;
}