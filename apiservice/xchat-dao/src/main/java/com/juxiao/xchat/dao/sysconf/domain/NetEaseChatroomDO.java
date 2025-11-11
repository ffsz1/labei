package com.juxiao.xchat.dao.sysconf.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class NetEaseChatroomDO {
    private Integer id;
    private String attach;
    private String ext;
    private String fromAccount;
    private String fromAvator;
    private String fromClientType;
    private String fromExt;
    private String fromNick;
    private String msgTimestamp;
    private String msgType;
    private String msgidClient;
    private String resendFlag;
    private String roleInfoTimetag;
    private String roomId;
    private String antispam;
    private String yidunRes;
    private Date createTime;
}