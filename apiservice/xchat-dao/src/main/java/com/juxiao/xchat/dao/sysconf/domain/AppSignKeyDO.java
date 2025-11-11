package com.juxiao.xchat.dao.sysconf.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AppSignKeyDO {
    private Integer appVersion;
    private String signKey;
    private Date createTime;

}
