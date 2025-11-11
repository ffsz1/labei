package com.juxiao.xchat.dao.room.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @class: StatRoomUserCtrbSumDTO.java
 * @author: chenjunsheng
 * @date 2018/6/5
 */
@Getter
@Setter
public class StatRoomUserCtrbSumDTO {
    /**
     * 房主UID
     */
    private Long uid;
    /**
     * 用户ID
     */
    private Long ctrbUid;
    /**
     * 用户昵称
     */
    private String nick;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 用户性别
     */
    private Byte gender;
    /**
     * 用户总金币
     */
    private Long sumGold;
    /**
     * 等级值
     */
    private Integer experLevel;
    /**
     * 魅力值
     */
    private Integer charmLevel;

}