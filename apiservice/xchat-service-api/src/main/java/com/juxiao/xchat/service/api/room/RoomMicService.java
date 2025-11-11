package com.juxiao.xchat.service.api.room;

import com.juxiao.xchat.base.web.WebServiceException;

import java.util.Map;

public interface RoomMicService {

    /**
     * 上麦--测试时需要考虑上麦用户意外掉线时队列是否更新
     *
     * @param uid      上麦用户
     * @param roomId   房间ID
     * @param position 上麦的位置
     * @param operator 操作人
     * @return
     * @throws WebServiceException
     */
    int upMic(Long uid, Long roomId, Integer position, Long operator) throws WebServiceException;

    /**
     * 下麦--测试时需要考虑上麦用户意外掉线时队列是否更新
     *
     * @param micUid   下麦用户
     * @param roomId   房间ID
     * @param position 下麦位置
     * @return
     * @throws WebServiceException
     */
    int downMic(Long micUid, Long roomId, Integer position) throws WebServiceException;

    /**
     * 锁麦/开麦
     *
     * @param roomUid  房间ID
     * @param uid      用户ID
     * @param position 麦的位置
     * @param state    状态，1：锁麦，0开麦（即取消锁麦）
     * @return
     */
    int lockMic(Long roomUid, Long uid, Integer position, Integer state) throws Exception;

    /**
     * 邀请上麦--通知被邀请用户
     *
     * @param micUid   上麦用户
     * @param roomUid  房主或管理员ID
     * @param roomId   房间ID
     * @param position 麦序位置
     * @return
     */
    int sendInviteMicMessage(Long micUid, Long roomUid, Long roomId, Integer position) throws WebServiceException;

    /**
     * 踢用户下麦
     *
     * @param micUid   下麦用户
     * @param roomUid  房主或管理员ID
     * @param roomId   下麦房间
     * @param position 麦序位置
     * @return
     * @throws WebServiceException
     */
    int kickMic(Long micUid, Long roomUid, Long roomId, Integer position) throws WebServiceException;

    /**
     * 锁住/取消坑位
     *
     * @param roomUid  房主/管理员ID
     * @param uid      当前操作用户ID
     * @param position 坑位
     * @param state    状态
     * @return
     * @throws WebServiceException
     */
    int lockPos(Long roomUid, Long uid, Integer position, Integer state) throws Exception;

    /**
     * 根据房间ID获取房间麦的信息
     *
     * @param uid UID
     * @return
     */
    Map<String, String> getRoomMicByUid(Long uid);

}
