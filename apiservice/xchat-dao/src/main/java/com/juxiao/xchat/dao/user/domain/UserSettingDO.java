package com.juxiao.xchat.dao.user.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserSettingDO {
    private Long uid;
    private Byte likedSend;
    private Date createTime;
    private Integer chatPermission;

}
