package com.juxiao.xchat.dao.item.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class GiftSendRecordDO {
    private Long sendRecordId;
    private Long uid;
    private Long reciveUid;
    private Byte sendEnv;
    private Long roomUid;
    private Byte roomType;
    private Integer giftId;
    private Integer giftNum;
    private Long totalGoldNum;
    private Double totalDiamondNum;
    private Date createTime;
}