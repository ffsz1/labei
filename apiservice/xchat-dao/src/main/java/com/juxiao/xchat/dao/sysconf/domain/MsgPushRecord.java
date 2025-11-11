package com.juxiao.xchat.dao.sysconf.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MsgPushRecord {
    private Integer recordId;
    private Long fromAccid;
    private Byte toObjType;
    private Byte msgType;
    private String toAccids;
    private String toErbanNos;
    private String title;
    private String webUrl;
    private String picUrl;
    private Byte skipType;
    private String skipUri;
    private String msgDesc;
    private String adminId;
    private Date crateTime;
}