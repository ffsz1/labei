package com.juxiao.xchat.service.api.room;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.room.dto.RoomUserinDTO;

public interface UserRoomService {

    /**
     * 用户进入房间
     *
     * @param uid     用户ID
     * @param roomUid 房间ID
     * @return
     */
    RoomUserinDTO userIntoRoom(Long uid, Long roomUid) throws Exception;

    /**
     * 用户退出房间
     *
     * @param uid 用户ID
     * @return
     */
    void userOutRoom(Long uid) throws WebServiceException;

    /**
     * 根据uid查询所在房间信息
     *
     * @param uid 用户ID
     * @return
     * @throws WebServiceException
     */
    RoomUserinDTO getRoomByUid(Long uid) throws WebServiceException;
}
