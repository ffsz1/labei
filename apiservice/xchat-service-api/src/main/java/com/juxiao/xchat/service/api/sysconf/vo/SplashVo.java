package com.juxiao.xchat.service.api.sysconf.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SplashVo {
    private String pict;
    private String link;
    private Integer type;
}
