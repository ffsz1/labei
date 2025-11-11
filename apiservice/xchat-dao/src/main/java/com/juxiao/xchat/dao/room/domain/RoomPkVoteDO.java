package com.juxiao.xchat.dao.room.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RoomPkVoteDO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 房间ID
     */
    private Long roomId;
    /**
     * 投票类型
     */
    private Byte pkType;
    /**
     * 发起PK用户ID
     */
    private Long uid;
    /**
     * 获得票数
     */
    private Integer voteCount;
    /**
     * 挑战者用户ID
     */
    private Long pkUid;
    /**
     * 挑战者获得票数
     */
    private Integer pkVoteCount;
    /**
     * PK投票时间
     */
    private Integer expireSeconds;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
