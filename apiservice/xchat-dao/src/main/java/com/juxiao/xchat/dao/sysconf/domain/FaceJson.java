package com.juxiao.xchat.dao.sysconf.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FaceJson {
    private Integer id;
    private String version;
    private Byte status;
    private Date createTime;
    private String json;
}