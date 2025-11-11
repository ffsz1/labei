package com.juxiao.xchat.manager.common.room;

import com.juxiao.xchat.dao.room.dto.RoomUserinDTO;

/**
 * @class: UserRoomManager.java
 * @author: chenjunsheng
 * @date 2018/6/13
 */
public interface UserRoomManager {
    /**
     * @param uid
     * @param userinDto
     */
    void saveUserInRoom(Long uid, RoomUserinDTO userinDto);

    /**
     * @param uid
     * @return
     */
    RoomUserinDTO getUserInRoom(Long uid);
}
