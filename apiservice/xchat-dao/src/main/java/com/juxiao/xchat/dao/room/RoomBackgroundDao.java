package com.juxiao.xchat.dao.room;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.room.dto.RoomBackgroundResultDTO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 房间背景
 *
 * @class: RoomBackgroundDao.java
 * @author: chenjunsheng
 * @date 2018/7/17
 */
public interface RoomBackgroundDao {

    /**
     * 查询所有有效的列表
     *
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT id, name, uri FROM `room_background` WHERE `bg_status` = 1")
    List<RoomBackgroundResultDTO> listEffectiveBackground();

}
