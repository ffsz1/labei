package com.juxiao.xchat.service.api.room;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.room.dto.RoomDTO;
import com.juxiao.xchat.dao.room.dto.RoomLinkVo;
import com.juxiao.xchat.dao.room.dto.RoomSearchDTO;
import com.juxiao.xchat.dao.room.dto.RoomUserinDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.common.room.vo.RoomRecommendVo;
import com.juxiao.xchat.manager.common.room.vo.RoomVo;
import com.juxiao.xchat.service.api.room.bo.RoomAdminParamBO;
import com.juxiao.xchat.service.api.room.bo.RoomParamBO;
import com.juxiao.xchat.service.api.room.vo.ChatInfoVO;

import java.util.List;

/**
 * 房间服务
 */
public interface RoomService {

    // 房间麦位变化
    void roomMicDown(Long roomId, Long uid) throws WebServiceException;

    /**
     * 开通房间
     *
     * @param paramBO
     * @return
     */
    WebServiceMessage openRoom(RoomParamBO paramBO) throws Exception;

    /**
     * 根据房间ID查询房间信息</br>
     * 会去查缓存
     *
     * @param uid
     * @return
     */
    RoomDTO getRoomByUid(Long uid);

    /**
     * 查询房间信息
     *
     * @param uid        房主ID
     * @param os
     * @param appVersion
     * @param ip
     * @return
     */
    RoomUserinDTO getRoom(Long uid, Long visitorUid, String os, String app, String appVersion, String ip) throws Exception;

    /**
     * 根据roomId查询room信息
     *
     * @param roomId
     * @return
     */
    RoomDTO getRoomByRoomId(Long roomId) throws WebServiceException;

    /**
     * 修改正在运行中的房间信息
     *
     * @param paramBO 房间参数
     * @return
     */
    RoomUserinDTO updateRoomByRunning(RoomParamBO paramBO) throws Exception;

    /**
     * 管理员修改房间信息
     *
     * @param adminParamBO 参数
     * @return
     * @throws WebServiceException
     */
    RoomUserinDTO updateRoomByAdmin(RoomAdminParamBO adminParamBO) throws Exception;

    /**
     * 替换字符中的敏感字符
     *
     * @param str     需要检查的字符
     * @param replace 替换敏感词的字符
     * @return
     */
    String replaceSensitiveWords(String str, String replace);

    /**
     * 检查字符中是否包含敏感词
     *
     * @param str 需要检查的字符
     * @return
     */
    boolean hasSensitiveWords(String str);

    /**
     * 搜索房间信息
     *
     * @param key 搜索的内容
     * @return
     */
    List<RoomSearchDTO> search(String key, List<Long> uids) throws WebServiceException;

    String selectChatInfo();

    Object selectMalice() throws WebServiceException;

    /**
     * @param usersDto
     */
    void sendOpenRoomNoticeToFollowers(UsersDTO usersDto);

    List<RoomLinkVo> listAuditLinkPool(Long uid) throws WebServiceException;

    List<RoomLinkVo> linkPool(Long uid) throws WebServiceException;


    RoomLinkVo getAuditLinkRoom(Long uid) throws WebServiceException;

    RoomLinkVo link(Long uid) throws WebServiceException;

    List<RoomRecommendVo> getRoomRecommendList(Long uid, String os, String appVersion, String app,Integer pageNum,Integer pageSize);

    List<ChatInfoVO> getLobbyChatInfo(Long uid, String os, String appVersion, String app, String ip)  throws WebServiceException;
}
