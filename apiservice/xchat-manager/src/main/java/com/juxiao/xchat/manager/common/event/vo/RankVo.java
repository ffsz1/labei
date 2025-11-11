package com.juxiao.xchat.manager.common.event.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RankVo {
    private Long uid;
    private Long erbanNo;
    private String avatar;
    private String nick;
    private Byte gender;
    private Long totalNum;
    private Integer nobleId;
    private String nobleName;
    private Byte rankHide;
    private Boolean hasPrettyNo;
    //等级值
    private Integer experLevel;
    //魅力值
    private Integer charmLevel;
    //距离上一级差值
    private double distance;
}
