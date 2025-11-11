package com.juxiao.xchat.dao.room.dto;

import com.juxiao.xchat.base.utils.DateFormatUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @class: RoomPkVoteResultDTO.java
 * @author: chenjunsheng
 * @date 2018/7/3
 */
@Getter
@Setter
public class RoomPkVoteResultDTO {

    /**
     * 投票ID
     */
    private Long voteId;
    /**
     * 系统当前时间戳
     */
    private Long timestamps;

    /**
     * 投票剩余时间（单位：秒）
     */
    private Integer duration;

    /**
     * 投票类型
     */
    private Byte pkType;

    /**
     * 发起PK用户ID
     */
    private Long uid;

    /**
     * 用户昵称
     */
    private String nick;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 获得票数
     */
    private Integer voteCount;

    /**
     * 挑战者用户ID
     */
    private Long pkUid;

    /**
     * 挑战者用户昵称
     */
    private String pkNick;

    /**
     * 挑战者头像
     */
    private String pkAvatar;

    /**
     * 挑战者获得票数
     */
    private Integer pkVoteCount;

    /**
     * 过期时间
     */
    private Integer expireSeconds;

    /**
     * 创建时间
     */
    private Date createTime;

    public String getCreateTime() {
        return createTime == null ? null : DateFormatUtils.YYYY_MM_DD_HH_MM_SS.date2Str(createTime);
    }

}
