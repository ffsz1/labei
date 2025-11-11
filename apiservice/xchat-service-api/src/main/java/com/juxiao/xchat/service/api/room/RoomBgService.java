package com.juxiao.xchat.service.api.room;

import com.juxiao.xchat.dao.room.domain.RoomBgDO;

import java.util.List;

/**
 * @Auther: alwyn
 * @Description: 房间背景接口
 * @Date: 2018/10/9 11:36
 */
public interface RoomBgService {

    /**
     * 根据ID查询背景信息
     * @param id
     * @return
     */
    RoomBgDO getById(Integer id, Long roomId);

    /**
     * 根据ID查询背景信息
     * @param id
     * @return
     */
    RoomBgDO getById(String id, Long roomId);

    /**
     *
     * @param roomId 房间ID
     * @return
     */
    List<RoomBgDO> listByRoomId(Long roomId);
}
