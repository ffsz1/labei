package com.juxiao.xchat.manager.common.event.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RankActVo {
    private long uid;

    private long erbanNo;

    private String nick;

    private String avatar;

    private Integer experLevel; //等级值

    private Integer charmLevel; //魅力值

    private long total;
}
