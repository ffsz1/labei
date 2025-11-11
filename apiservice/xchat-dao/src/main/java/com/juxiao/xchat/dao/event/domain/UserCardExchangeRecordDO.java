package com.juxiao.xchat.dao.event.domain;

import lombok.Data;

import java.util.Date;

@Data
public class UserCardExchangeRecordDO {
    private Long id;
    private Long uid;
    private Long cardId;
    private Long giftId;
    private Integer goldNum;
    private Integer days;
    private Date createDate;
    private Integer type;
}
