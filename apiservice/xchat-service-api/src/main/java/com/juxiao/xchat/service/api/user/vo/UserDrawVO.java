package com.juxiao.xchat.service.api.user.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDrawVO {
    private Long uid;
    private Integer leftDrawNum;
    private Integer totalDrawNum;
    private Integer totalWinDrawNum;
}
