package com.juxiao.xchat.dao.sysconf.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AppSignKeyDTO {
    private Integer appCode;
    private String signKey;
    private Date createTime;
}
