package com.juxiao.xchat.service.api.charge.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * 提现用户信息
 *
 * @class: WithDrawVO.java
 * @author: chenjunsheng
 * @date 2018/6/11
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WithDrawVO {
    private Long uid;

    private Double diamondNum;

    private Boolean isNotBoundPhone;

    private String alipayAccount;

    private String alipayAccountName;

    private Boolean hasWx;

    private Integer withDrawType;
}
