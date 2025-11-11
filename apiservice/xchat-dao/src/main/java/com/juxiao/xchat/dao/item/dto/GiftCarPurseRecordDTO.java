package com.juxiao.xchat.dao.item.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class GiftCarPurseRecordDTO {
    private Long recordId;
    private Long uid;
    private Long carId;
    private Integer carDate;
    private Long totalGoldNum;
    private Byte isUse;
    private Date createTime;
}