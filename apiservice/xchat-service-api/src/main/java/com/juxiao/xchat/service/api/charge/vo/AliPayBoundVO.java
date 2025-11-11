package com.juxiao.xchat.service.api.charge.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AliPayBoundVO {
    private Long uid;
    private String alipayAccount;
    private String alipayAccountName;
}
