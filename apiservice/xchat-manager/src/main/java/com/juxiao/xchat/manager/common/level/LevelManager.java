package com.juxiao.xchat.manager.common.level;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.sysconf.dto.LevelCharmDTO;
import com.juxiao.xchat.dao.sysconf.dto.LevelExperienceDTO;
import com.juxiao.xchat.manager.common.level.vo.LevelVO;

/**
 * 用户等级服务
 *
 * @class: LevelManager.java
 * @author: chenjunsheng
 * @date 2018/6/6
 */
public interface LevelManager {

    /**
     * 获取用户经验等级
     *
     * @param uid
     * @return
     */
    int getUserExperienceLevelSeq(Long uid);

    /**
     * 获取用户魅力等级
     *
     * @param uid
     * @return
     */
    int getUserCharmLevelSeq(Long uid);

    /**
     * 根据级别名称获取
     *
     * @param levelName
     * @return
     */
    LevelExperienceDTO getLevelExperienceByName(String levelName);

    /**
     * @param levelName
     * @return
     */
    LevelCharmDTO getLevelCharmByName(String levelName);

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
