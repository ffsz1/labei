package com.juxiao.xchat.service.api.mcoin;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.service.api.mcoin.vo.McoinMissionVO;

/**
 * 萌币服务
 */
public interface McoinMissionService {

    /**
     * 查询萌币任务列表
     *
     * @param uid
     * @return
     * @throws WebServiceException
     */
    McoinMissionVO getInfo(Long uid, String os, String appid, String appVersion, String ip) throws WebServiceException;

    /**
     * 领取萌币接口
     *
     * @param uid
     * @param missionId
     * @throws WebServiceException
     */
    void gainMcoin(Long uid, Integer missionId) throws WebServiceException;
}
