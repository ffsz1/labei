package com.juxiao.xchat.dao.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FeedbackDO {
    private String feedbackId;
    private Long uid;
    private String feedbackDesc;
    private String imgUrl;
    private String contact;
    private Date createTime;
}