package com.juxiao.xchat.dao.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPurseHotRoomResultDTO {
    private Long userNo;

    private Integer roomNo;

    private Integer goldNum;

    private String date;

    private String hour;

    private Date createTime;
}