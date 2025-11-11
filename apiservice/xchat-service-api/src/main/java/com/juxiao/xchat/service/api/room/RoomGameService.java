package com.juxiao.xchat.service.api.room;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.room.dto.RoomGameDTO;
import com.juxiao.xchat.service.api.room.vo.RoomGameRecordVO;

import java.util.List;

public interface RoomGameService {

    /**
     * 获取游戏状态
     *
     * @param uid
     * @param roomId
     * @return
     * @throws WebServiceException
     */
    RoomGameRecordVO getState(Long uid, Long roomId) throws WebServiceException;

    /**
     * 选择
     *
     * @param uid
     * @param roomId
     * @param type
     * @param result
     * @return
     * @throws WebServiceException
     */
    String choose(Long uid, Long roomId, Integer type, String result) throws WebServiceException;

    /**
     * 提交
     *
     * @param uid
     * @param roomId
     * @param type
     * @param result
     * @return
     * @throws WebServiceException
     */
    String confirm(Long uid, Long roomId, Integer type, String result) throws WebServiceException;

    /**
     * 取消
     *
     * @param uid
     * @param roomId
     * @param type
     * @param result
     * @return
     * @throws WebServiceException
     */
    String cancel(Long uid, Long roomId, Integer type, String result) throws WebServiceException;


    /**
     * 根据uid获取个人房间游戏信息
     *
     * @param uid
     * @param roomId
     * @return
     * @throws WebServiceException
     */
    List<RoomGameDTO> getPersonGame(Long uid, Long roomId) throws WebServiceException;
}
