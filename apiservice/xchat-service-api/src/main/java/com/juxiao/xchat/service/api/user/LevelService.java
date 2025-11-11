package com.juxiao.xchat.service.api.user;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.manager.common.level.vo.LevelVO;

public interface LevelService {

    /**
     * 获取用户经验等级
     *
     * @param uid 用户UID
     * @return
     */
    LevelVO getLevelExperience(Long uid) throws WebServiceException;

    /**
     * 获取用户魅力等级
     *
     * @param uid
     * @return
     * @throws WebServiceException
     */
    LevelVO getLevelCharm(Long uid) throws WebServiceException;
}
