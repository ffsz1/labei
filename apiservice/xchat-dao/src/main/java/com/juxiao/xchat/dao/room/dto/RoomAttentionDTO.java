package com.juxiao.xchat.dao.room.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author chris
 * @Title:
 * @date 2018/11/28
 * @time 16:13
 */
@Data
public class RoomAttentionDTO {

    private Integer id;

    private Long roomId;

    private Long uid;

    private Long erbanNo;

    private Date createTime;

    private String title;

    private String avatar;

    private String roomTag;

    private String roomTagPic;
}
