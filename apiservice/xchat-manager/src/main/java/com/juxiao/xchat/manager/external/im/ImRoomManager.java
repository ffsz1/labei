package com.juxiao.xchat.manager.external.im;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.room.dto.RoomDTO;
import com.juxiao.xchat.manager.common.room.vo.RoomMicVO;
import com.juxiao.xchat.manager.external.im.bo.ImRoomMemberBO;
import com.juxiao.xchat.manager.external.im.bo.ImRoomMessage;

import java.util.List;

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
     * @throws WebServiceException
     */
    void pushRoomMicUpdateNotice(Long roomId, Integer type, RoomMicVO micVo) throws Exception;


    /**
     * 往单个房间推送自定义消息
     *
     * @param msginfo
     * @return
     */
    void pushRoomMsg(Object msginfo) throws Exception;

    /**
     * 往所有房间推送自定义消息(全服)
     *
     * @param msginfo
     * @param interceptRoomList
     * @return
     */
    void pushAllRoomMsg(Object msginfo, List<Long> interceptRoomList) throws Exception;

    /**
     * 验证该用户,是不是管理员
     *
     * @param uid    用户
     * @param roomId 房间
     * @return
     * @throws WebServiceException
     */
    boolean isRoomManager(Long roomId, Long uid) throws WebServiceException;


    void pushUserMsg(ImRoomMessage msginfo) throws Exception;

    /**
     * 往单个房间推送自定义消息
     *
     * @param msginfo
     * @return
     */
    void pushRoomCustomMsg(Object msginfo) throws Exception;
}