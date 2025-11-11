package com.juxiao.xchat.dao.charge.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 充值产品返回对象
 *
 * @class: ChargeProdDTO.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */
@Getter
@Setter
public class ChargeProdDTO {
    private String chargeProdId;
    private String prodName;
    private String prodDesc;
    private Byte prodStatus;
    private Integer money;
    private Integer changeGoldRate;
    private Integer giftGoldNum;
    private Integer goldNum;
    private Integer firstGiftGoldNum;
    private Byte channel;
    private Byte seqNo;
}