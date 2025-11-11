package com.juxiao.xchat.dao.charge.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChargeProdDO {
    private String chargeProdId;
    private String prodName;
    private String prodDesc;
    private Byte prodStatus;
    private Long money;
    private Integer changeGoldRate;
    private Integer giftGoldNum;
    private Integer firstGiftGoldNum;
    private Byte channel;
    private Byte seqNo;
}