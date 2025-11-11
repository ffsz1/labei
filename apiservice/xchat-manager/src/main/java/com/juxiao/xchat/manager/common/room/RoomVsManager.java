package com.juxiao.xchat.manager.common.room;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.room.domain.RoomVsDO;
import com.juxiao.xchat.dao.room.enumeration.RoomVsStatus;

/**
 * 描述：房间PK
 *
 * @创建时间： 2020/10/28 17:08
 * @作者： carl
 */
public interface RoomVsManager {

    /**
     * 停止房间PK
     * @param endUser 停止用户（可为空，例如倒计时到期）
     * @param roomVsId PK对应的主键
     * @param targetStatus 停止的目标状态
     */
    void endRoomVs(Long endUser, Long roomVsId, RoomVsStatus targetStatus) throws WebServiceException;
}
