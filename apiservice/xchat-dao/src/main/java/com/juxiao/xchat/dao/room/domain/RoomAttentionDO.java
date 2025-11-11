package com.juxiao.xchat.dao.room.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author chris
 * @Title:
 * @date 2018/11/28
 * @time 16:18
 */
@Data
public class RoomAttentionDO {

    private Integer id;

    private Long roomId;

    private Long uid;

    private Date createTime;


}