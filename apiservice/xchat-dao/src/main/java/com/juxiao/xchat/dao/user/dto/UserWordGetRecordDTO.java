package com.juxiao.xchat.dao.user.dto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserWordGetRecordDTO {

    private Integer recordId;

    private Long uid;

    private String word;

    private Integer activityType;

}
