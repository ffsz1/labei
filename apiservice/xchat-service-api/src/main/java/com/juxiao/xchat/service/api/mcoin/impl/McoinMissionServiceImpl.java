package com.juxiao.xchat.service.api.mcoin.impl;

import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.mcoin.McoinPkInfoDao;
import com.juxiao.xchat.dao.mcoin.dto.McoinMissionDTO;
import com.juxiao.xchat.dao.mcoin.dto.UserMcoinPurseDTO;
import com.juxiao.xchat.manager.common.mcoin.McoinDrawManager;
import com.juxiao.xchat.manager.common.mcoin.McoinManager;
import com.juxiao.xchat.manager.common.mcoin.McoinMissionManager;
import com.juxiao.xchat.manager.common.mcoin.McoinPkManager;
import com.juxiao.xchat.manager.common.sysconf.AppVersionManager;
import com.juxiao.xchat.service.api.mcoin.McoinMissionService;
import com.juxiao.xchat.service.api.mcoin.vo.McoinMissionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class McoinMissionServiceImpl implements McoinMissionService {
    @Autowired
    private AppVersionManager versionManager;
    @Autowired
    private McoinManager mcoinManager;
    @Autowired
    private McoinMissionManager mcoinMissionManager;
    @Autowired
    private McoinPkManager mcoinPkManager;
    @Autowired
    private McoinPkInfoDao mcoinPkInfoDao;
    @Autowired
    private McoinDrawManager mcoinDrawManager;
    @Override
    public McoinMissionVO getInfo(Long uid, String os, String appid, String appVersion, String ip) throws WebServiceException {
        if (uid == null || uid == 0) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        UserMcoinPurseDTO purseDto = mcoinManager.getUserMcoinPurse(uid);
        if (purseDto == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        McoinMissionVO missionVo = new McoinMissionVO();

        // 查询用户萌币余额
        missionVo.setMcoinNum(purseDto.getMcoinNum());

        boolean isAuditingVersion = versionManager.checkAuditingVersion(os, appid, appVersion, ip, uid);
        // 1，新手任务；2，每日任务；3，签到任务
        if (isAuditingVersion) {
            // 查询用户新手任务
            missionVo.setBeginnerMissions(mcoinMissionManager.listAuditingMissions(uid, (byte) 1));
            // 查询用户每日任务列表
            missionVo.setDailyMissions(mcoinMissionManager.listAuditingMissions(uid, (byte) 2));
            // 查询用户每周签到列表
            missionVo.setWeeklyMissions(mcoinMissionManager.listAuditingMissions(uid, (byte) 3));
        } else {
            // 查询用户新手任务
            missionVo.setBeginnerMissions(mcoinMissionManager.listMissions(uid, (byte) 1));
            // 查询用户每日任务列表
            missionVo.setDailyMissions(mcoinMissionManager.listMissions(uid, (byte) 2));
            // 查询用户每周签到列表
            missionVo.setWeeklyMissions(mcoinMissionManager.listMissions(uid, (byte) 3));
        }

        //萌币PK和萌币竞猜发全服通知
        Integer term = mcoinPkInfoDao.getTermByPushMsgAndPkStatus();
        if (null != term) {
            mcoinPkManager.pkBeginning(term);
        }

        mcoinDrawManager.pushMsg((byte)1);
        mcoinDrawManager.pushMsg((byte)2);
        mcoinDrawManager.pushMsg((byte)3);

        return missionVo;
    }

    @Override
    public void gainMcoin(Long uid, Integer missionId) throws WebServiceException {
        if (uid == null || uid == 0 || missionId == null || missionId == 0) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        McoinMissionDTO missionDto = mcoinMissionManager.getMcoinMission(missionId);
        if (missionDto == null) {
            throw new WebServiceException(WebServiceCode.MISSION_NOT_EXISTS);
        }

        if (missionDto.getMissionType() == 3) {
            throw new WebServiceException(WebServiceCode.MISSION_GAIN_FAIL);
        }

        // 更新萌币
        mcoinMissionManager.achieve(uid, missionId);
    }
}
