package com.juxiao.xchat.manager.common.room;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.room.domain.RoomDO;
import com.juxiao.xchat.dao.room.dto.RoomDTO;
import com.juxiao.xchat.manager.common.room.vo.RoomVo;

/**
 * 房间通用操作
 *
 * @class: RoomManager.java
 * @author: chenjunsheng
 * @date 2018/6/8
 */
public interface RoomManager {

    /**
     * 获取房间信息
     *
     * @param roomId
     * @return
     */
    RoomDTO getRoom(Long roomId);

    /**
     * 获取房间
     *
     * @param uid
     * @return
     * @author: chenjunsheng
     * @date 2018/6/8
     */
    RoomDTO getUserRoom(Long uid);

    /**
     * 保存房间信息--有ID则更新</br>
     * 会更新到缓存中
     *
     * @param roomDo room
     * @return
     */
    RoomDTO updateRoomInfo(RoomDO roomDo);

    /**
     * 关闭房间
     *
     * @param uid 房间ID
     * @return 房间信息
     */
    RoomDTO close(Long uid) throws WebServiceException;

    /**
     * 根据UID获取对应的机器人数量
     *
     * @param uid UID
     * @return
     */
    int getRobotNum(Long uid);

    /**
     * @param uid
     * @param onlineNum
     * @return
     */
    Integer getNeedAddNum(Long uid, Integer onlineNum);

    RoomVo convertRoomToVo(RoomDTO roomDTO);
}
