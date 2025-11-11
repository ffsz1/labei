package com.juxiao.xchat.manager.common.mcoin;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.mcoin.dto.McoinMissionDTO;
import com.juxiao.xchat.dao.mcoin.dto.McoinMissionInfoDTO;

import java.util.List;

public interface McoinMissionManager {
    /**
     * 任务状态：1，未完成；2，已完成；3，已领取
     */
    int MISSION_STATUS_UNFINISH = 1;

    int MISSION_STATUS_FINISH = 2;

    int MISSION_STATUS_GAIN = 3;

    /**
     * 更新萌币任务
     *
     * @param uid
     * @param missionId
     * @throws WebServiceException
     */
    void finish(Long uid, Integer missionId) throws WebServiceException;

    /**
     * 每日任务完成
     *
     * @param uid
     */
    void achieveWeeklyMission(Long uid);

    /**
     * 领取任务奖励
     *
     * @param uid
     * @param missionId
     * @throws WebServiceException
     */
    void achieve(Long uid, Integer missionId) throws WebServiceException;

    /**
     * 获取萌币任务
     *
     * @param missionId
     * @return
     */
    McoinMissionDTO getMcoinMission(Integer missionId);

    /**
     * 查询任务列表
     */
    List<McoinMissionInfoDTO> listMissions(Long uid, byte missionType);

    /**
     * 查询审核状态下的任务列表
     *
     * @param uid
     * @param missionType
     * @return
     */
    List<McoinMissionInfoDTO> listAuditingMissions(Long uid, byte missionType);

    /**
     * 获取用户完成任务数量
     *
     * @param uid
     * @return
     */
    int countUserMission(Long uid);
}
