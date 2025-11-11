package com.juxiao.xchat.service.api.charge.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * 提现金融账户
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FinancialAccountVO {
    private Long uid;
    private String alipayAccount;
    private String alipayAccountName;
    private String bankCard;
    private String bankCardName;
}