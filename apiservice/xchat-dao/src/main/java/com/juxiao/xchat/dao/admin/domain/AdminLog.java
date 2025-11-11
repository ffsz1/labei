package com.juxiao.xchat.dao.admin.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AdminLog {
    private Long id;
    private Integer optUid;
    private String optClass;
    private String optMethod;
    private String optMess;
    private Date createTime;
    private Integer tmpint;
    private String tmpstr;
}