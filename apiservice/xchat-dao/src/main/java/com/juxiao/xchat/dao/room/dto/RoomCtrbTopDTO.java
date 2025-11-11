package com.juxiao.xchat.dao.room.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @class: StatRoomUserCtrbSumDTO.java
 * @author: chenjunsheng
 * @date 2018/6/5
 */
@Getter
@Setter
@AllArgsConstructor
public class RoomCtrbTopDTO {
    /**
     * 1：财富；2：魅力分栏
     */
    private Byte type;

    /**
     * 用户头像
     */
    private String avatar;
}