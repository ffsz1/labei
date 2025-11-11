package com.juxiao.xchat.dao.sysconf.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SysNoticeDTO {

    private Integer noticeId;

    private Byte type;

    private String content;

    private Integer seq;

}
