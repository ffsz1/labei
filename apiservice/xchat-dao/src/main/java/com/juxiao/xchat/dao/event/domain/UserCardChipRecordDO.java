package com.juxiao.xchat.dao.event.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class UserCardChipRecordDO {

    private Long id;
    private Long uid;
    private Integer chipSource;
    private Integer num;
    private Long sendUid;
    private Long chipId;
    private Date createDate;
    private Integer operateType;

}
