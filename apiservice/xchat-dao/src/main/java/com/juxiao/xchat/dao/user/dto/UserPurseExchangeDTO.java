package com.juxiao.xchat.dao.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 钻石兑换金币之后返回用户钱包对象
 *
 * @class: UserPurseExchangeDTO.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPurseExchangeDTO {
    private Long uid;
    private Long goldNum;
    private Long chargeGoldNum;
    private Long nobleGoldNum;
    private Double diamondNum;
    private Long depositNum;
    private Date firstRechargeTime;
    private String drawMsg;
    private String drawUrl;
}