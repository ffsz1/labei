package com.juxiao.xchat.dao.user.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserWxappShareRecordDO {
    private Long uid;
    private Date shareDate;
    private Long shareUid;
    private Integer shareCount;
}
