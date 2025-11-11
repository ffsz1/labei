package com.juxiao.xchat.dao.item.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class HeadwearPurseRecordDTO {
    private Long recordId;
    private Long uid;
    private Long headwearId;
    private Integer headwearDate;
    private Long totalGoldNum;
    private Byte isUse;
    private Date createTime;
}