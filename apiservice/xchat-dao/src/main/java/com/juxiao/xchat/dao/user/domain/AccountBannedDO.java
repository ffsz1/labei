package com.juxiao.xchat.dao.user.domain;

import lombok.Data;

import java.util.Date;

@Data
public class AccountBannedDO {

    private Integer id;

    /**
     * UID
     */
    private Long uid;

    /**
     * 官方号
     */
    private Long erbanNo;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 设备号
     */
    private String deviceId;

    /**
     * 封禁类型,1.房间禁言,2.私聊禁言,3.广播禁言
     */
    private String bannedType;

    /**
     * 封禁ip
     */
    private String bannedIp;

    /**
     * 封禁状态1封禁中，2封禁结束（删除）
     */
    private Byte bannedStatus;

    /**
     * 封禁时长（单位：分）
     */
    private Integer bannedMinute;

    /**
     * 封禁开始时间
     */
    private Date bannedStartTime;

    /**
     * 封禁结束时间
     */
    private Date bannedEndTime;

    /**
     * 封禁原因描述
     */
    private String bannedDesc;

    /**
     * 操作管理员
     */
    private Integer adminId;

    /**
     * 创建时间
     */
    private Date createTime;
}
