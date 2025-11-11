package com.juxiao.xchat.dao.charge.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WithDrawCashProdDTO {
    private String cashProdId;
    private String cashProdName;
    private Long diamondNum;
    private Long cashNum;
    private Integer seqNo;
}