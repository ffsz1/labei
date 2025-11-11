package com.juxiao.xchat.dao.user.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserQuestionnaireAnswerStatisticsKey {
    private Integer type;
    private Integer question;
    private String answer;
}