package com.juxiao.xchat.dao.user.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 用户红包记录
 *
 * @class: UserPacketRecordDO.java
 * @author: chenjunsheng
 * @date 2018/6/5
 */
@Getter
@Setter
public class UserPacketRecordDO {
    private String packetId;
    private Long uid;
    private Double packetNum;
    private Long srcUid;
    private Byte type;
    private String objId;
    private Boolean hasUnpack;
    private Byte packetStatus;
    private Date createTime;
    private Date updateTime;
}