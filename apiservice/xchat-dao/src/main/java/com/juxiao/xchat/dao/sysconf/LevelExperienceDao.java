package com.juxiao.xchat.dao.sysconf;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.sysconf.dto.LevelExperienceDTO;
import org.apache.ibatis.annotations.Param;

/**
 * 经验等级数据操作层
 *
 * @class: LevelExperienceDao.java
 * @author: chenjunsheng
 * @date 2018/6/6
 */
public interface LevelExperienceDao {

    /**
     * 根据经验值查询对应等级
     *
     * @param experience
     * @return
     * @author: chenjunsheng
     * @date 2018/6/6
     */
    @TargetDataSource(name = "ds2")
    LevelExperienceDTO getExperienceLevel(@Param("experience") Long experience);

    /**
     * 根据等级名查询
     *
     * @param levelName 等级名
     * @return
     */
    @TargetDataSource(name = "ds2")
    LevelExperienceDTO getByName(@Param("levelName") String levelName);
}
