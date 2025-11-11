package com.juxiao.xchat.dao.charge.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 支付充值记录
 */
@Getter
@Setter
public class ChargeRecordDO {
    private String chargeRecordId;
    private Long uid;
    private Long roomUid;
    private String pingxxChargeId;
    private String chargeProdId;
    private String channel;
    private Byte bussType;
    private Byte chargeStatus;
    private String chargeStatusDesc;
    private Integer amount;
    private Long totalGold;
    private String clientIp;
    private String wxPubOpenid;
    private String subject;
    private String body;
    private String extra;
    private String metadata;
    private String chargeDesc;
    private Date createTime;
    private Date updateTime;
}