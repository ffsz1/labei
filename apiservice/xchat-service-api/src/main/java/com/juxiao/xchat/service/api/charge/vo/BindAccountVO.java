package com.juxiao.xchat.service.api.charge.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 绑定提现账户
 */
@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BindAccountVO {
    private Long uid;
    private String alipayAccount;
    private String alipayAccountName;
    private Integer accountType;
}