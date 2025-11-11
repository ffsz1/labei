package com.juxiao.xchat.dao.room.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author chris
 * @Title:
 * @date 2018/9/14
 * @time 上午9:57
 */
@Data
public class RoomGameConfigDTO {

    private Integer id;

    private Long uid;

    private String start;

    private String end;

    private Integer status;

    private Date createTime;
}
