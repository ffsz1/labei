package com.juxiao.xchat.dao.charge.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WithDrawCashProdDO {
    private String cashProdId;
    private String cashProdName;
    private Long diamondNum;
    private Long cashNum;
    private Integer seqNo;
}