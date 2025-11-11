package com.juxiao.xchat.manager.common.mcoin;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.mcoin.dto.McoinMissionDTO;
import com.juxiao.xchat.dao.mcoin.dto.UserMcoinPurseDTO;

public interface McoinManager {

    /**
     * 查询用户钱包
     *
     * @param uid uid
     * @return UserMcoinPurseDTO
     */
    UserMcoinPurseDTO getUserMcoinPurse(Long uid) throws WebServiceException;

    /**
     * 增加钱包中的萌币
     *
     * @param uid uid
     * @param amount amount
     * @throws WebServiceException WebServiceException
     */
    void updateAddMcoin(Long uid, int amount) throws WebServiceException;

    /**
     * 减少钱包中的萌币
     *
     * @param uid uid
     * @param cost cost
     *  @throws WebServiceException WebServiceException
     */
    void updateReduceMcoin(Long uid, int cost) throws WebServiceException;

    /**
     * 等级达成赠送萌币
     *
     * @param uid uid
     * @param charmLevel charmLevel
     * @throws WebServiceException WebServiceException
     */
    void updateAddLevelMcoin(Long uid, Integer charmLevel) throws WebServiceException;

    /**
     * 发送消息给邀请人
     * @param uid uid
     * @param missionDto missionDto
     * @throws WebServiceException WebServiceException
     */
    void sendMessageToUser(Long uid, McoinMissionDTO missionDto) throws WebServiceException;

    /**
     * 减少缓存中点点币
     * @param sendUid sendUid
     * @param afterGoldNum afterGoldNum
     * @param pushMsg pushMsg
     */
    void reduceGoldCache(Long sendUid, Long afterGoldNum, boolean pushMsg)throws WebServiceException;
}
