package com.juxiao.xchat.service.api.room;

import com.juxiao.xchat.dao.room.dto.RoomBackgroundResultDTO;

import java.util.List;

/**
 * @class: RoomBackgroundService.java
 * @author: chenjunsheng
 * @date 2018/7/17
 */
public interface RoomBackgroundService {

    /**
     * 查询当前有效的房间背景图片
     *
     * @return
     */
    List<RoomBackgroundResultDTO> listEffectiveBackground();
}
