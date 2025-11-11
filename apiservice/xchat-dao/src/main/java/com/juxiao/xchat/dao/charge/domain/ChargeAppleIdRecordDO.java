package com.juxiao.xchat.dao.charge.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ChargeAppleIdRecordDO {
    private Integer appleId;
    private String chargeRecordId;
    private Long uid;
    private String transactionId;
    private Date createTime;
}