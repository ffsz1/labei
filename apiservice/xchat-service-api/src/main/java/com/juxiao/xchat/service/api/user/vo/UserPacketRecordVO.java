package com.juxiao.xchat.service.api.user.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPacketRecordVO {
    private String packetName;
    private Byte type;
    private Long uid;
    private Double packetNum;
    private Date createTime;
    private boolean needAlert;
}
