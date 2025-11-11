package com.juxiao.xchat.dao.item.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 虚拟货币礼物
 */
@Getter
@Setter
public class PointGiftSendRecordDO {
    private Long sendRecordId;
    private Long uid;
    private Long receiveUid;
    private Byte sendEnv;
    private Long roomUid;
    private Byte roomType;
    private Integer giftId;
    private Integer giftNum;
    private Long totalGoldNum;
    private Date createTime;
}