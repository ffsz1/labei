package com.juxiao.xchat.dao.room.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RoomTag {
    private Integer id;
    private String name;
    private String pict;
    private Integer seq;
    private Integer type;
    private Boolean status;
    private Boolean istop;
    private Date createTime;
    private String description;
    private Integer tmpint;
    private String tmpstr;
    private String children;
}