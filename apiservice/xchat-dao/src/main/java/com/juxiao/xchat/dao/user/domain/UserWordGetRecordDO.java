package com.juxiao.xchat.dao.user.domain;
import lombok.Data;

import java.util.Date;

@Data
public class UserWordGetRecordDO {

    private Integer recordId;

    private Long uid;

    private String word;

    private Integer activityType;

    private Date createTime;

}
