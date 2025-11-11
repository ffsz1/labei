package com.juxiao.xchat.dao.mcoin.dto;

import lombok.Data;

import java.util.Date;

@Data
public class McoinDailyMissionDTO {
    /**
     * 用户ID
     */
    private Long uid;
    /**
     * 任务ID
     */
    private Integer missionId;
    /**
     * 任务时间
     */
    private Date completeDate;
    /**
     * 任务状态，1，未完成；2，已完成；3，已领取
     */
    private Byte missionStatus;
}
