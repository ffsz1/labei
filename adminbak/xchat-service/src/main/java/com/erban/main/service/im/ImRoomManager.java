package com.erban.main.service.im;

import com.erban.main.service.im.bo.ImRoomMemberBO;
import com.erban.main.service.im.dto.RoomDTO;
import com.erban.main.service.im.vo.RoomMicVO;

/**
 * im服务房间管理
 */
public interface ImRoomManager {

    /**
     * 更新同步IM的用户信息
     *
     * @param memberBo
     */
    void updateMemberInfo(ImRoomMemberBO memberBo);

    /**
     * 返回roomId
     *
     * @param roomDto
     * @return
     */
    Long createRoom(RoomDTO roomDto) throws Exception;

    /**
     * 更新IM中房间信息
     *
     * @param roomDto
     */
    void updateRoom(RoomDTO roomDto) throws Exception;

    /**
     * 推送房间麦位更新消息
     *
     * @param roomId
     * @param type
     * @param micVo
     * @throws Exception
     */
    void pushRoomMicUpdateNotice(Long roomId, Integer type, RoomMicVO micVo) throws Exception;

    /**
     * 往单个房间推送自定义消息
     *
     * @param msgInfo
     * @return
     */
    void pushRoomMsg(String msgInfo) throws Exception;

    /**
     * 往所有房间推送自定义消息(全服)
     *
     * @param msgInfo
     * @return
     */
    void pushAllRoomMsg(String msgInfo) throws Exception;

    void addRobotToRoom(Long roomId, String accounts) throws Exception;

    void deleteRobotToRoom(Long roomId, String accounts) throws Exception;

}
