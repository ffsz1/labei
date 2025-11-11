package com.juxiao.xchat.service.api.event.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RankMineVo {
    private Long erbanNo;
    private String avatar;
    private String nick;
    private Byte gender;
    private double totalNum;
    private int seqNo;
    private Long reachGoldNum;
    private Integer nobleId;
    private String nobleName;
}
