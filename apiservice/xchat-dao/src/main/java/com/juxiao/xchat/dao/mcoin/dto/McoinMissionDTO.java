package com.juxiao.xchat.dao.mcoin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class McoinMissionDTO {
    private Integer id;
    private String missionName;
    private Integer mcoinAmount;
    private Integer freebiesType;
    private Integer freebiesId;
    private Integer seq;
    private Byte missionType;

}
