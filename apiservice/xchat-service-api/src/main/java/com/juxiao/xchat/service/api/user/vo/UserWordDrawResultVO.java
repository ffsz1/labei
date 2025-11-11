package com.juxiao.xchat.service.api.user.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserWordDrawResultVO {

    private Long uid;
    private Integer leftDrawNum;
    private Integer totalDrawNum;
    private Byte drawStatus;//中奖纪录状态，1创建，2未中奖，3已中奖

    private List<String> collectedWords;

    private List<String> allWords;

    private String word;
}
