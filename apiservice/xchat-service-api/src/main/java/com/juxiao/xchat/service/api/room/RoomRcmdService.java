package com.juxiao.xchat.service.api.room;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.manager.common.room.vo.RoomVo;

public interface RoomRcmdService {

    /**
     * 获取推荐房间
     * @param uid
     * @return
     */
    RoomVo getUserRcmdRoom(Long uid) throws WebServiceException;

}
