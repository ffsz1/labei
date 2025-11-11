package com.juxiao.xchat.dao.user.domain;

import lombok.Data;

import java.util.Date;

@Data
public class UserBoxDrawRecordDO {
    private Integer recordId;
    private Long uid;
    private Integer prizeId;
    private String prizeName;
    private Integer num;
    private Integer type;   // 1头饰 2座驾 3礼物
    private String desc;
    private Date createTime;
}