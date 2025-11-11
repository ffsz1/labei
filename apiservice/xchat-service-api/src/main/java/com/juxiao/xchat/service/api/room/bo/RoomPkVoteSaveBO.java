package com.juxiao.xchat.service.api.room.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * @class: RoomPkVoteSaveParam.java
 * @author: chenjunsheng
 * @date 2018/6/28
 */
@Getter
@Setter
public class RoomPkVoteSaveBO {
    /**
     * 房间ID
     */
    private Long roomId;
    /**
     * 操作用户UID
     */
    private Long opUid;
    /**
     * 投票类型
     */
    private Byte pkType;
    /**
     * 发起投票用户ID
     */
    private Long uid;
    /**
     * 选择对手用户ID
     */
    private Long pkUid;
    /**
     * 时间限制
     */
    private Integer expireSeconds;
}
