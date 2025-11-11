package com.juxiao.xchat.dao.room.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RoomTagDTO {
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
    /** 选中时的标签图片 */
    private String optPic;
    /** 默认的标签图片 */
    private String defPic;
}