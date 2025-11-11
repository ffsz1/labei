package com.juxiao.xchat.dao.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserPurseHotRoomRecordDTO {
    private Integer recordId;
    private Long uid;
    private Integer erbanNo;
    private Integer goldNum;
    private Date startTime;
    private Date endTime;
    private Date createTime;
}