package com.juxiao.xchat.dao.event.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ActivityHtmlDTO {
    private Long id;

    private String activityId;

    private String activityName;

    private String activityImage;

    private Boolean activityStatus;

    private String activityShareImage;

    private String activityShareTitle;

    private String activityShareContent;

    private String activityLink;

    private Date createTime;

    private Date updateTime;

    private Integer adminId;
}
