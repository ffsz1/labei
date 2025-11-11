package com.juxiao.xchat.manager.common.event.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityHtmlVO {
    private String activityImage;

    private String activityShareImage;

    private String activityShareTitle;

    private String activityShareContent;

    private String activityLink;
}
