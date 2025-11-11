package com.juxiao.xchat.dao.sysconf;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.sysconf.dto.LevelCharmDTO;
import org.apache.ibatis.annotations.Param;

/**
 * 经验等级数据操作层
 *
 * @class: LevelCharmDao.java
 * @author: chenjunsheng
 * @date 2018/6/6
 */
public interface LevelCharmDao {

    /**
     * 根据经验值查询对应的魅力等级
     *
     * @param experience
     * @return
     * @author: chenjunsheng
     * @date 2018/6/6
     */
    @TargetDataSource(name = "ds2")
    LevelCharmDTO getCharmLevel(@Param("experience") Long experience);

    /**
     * @param levelName
     * @return
     */
    @TargetDataSource(name = "ds2")
    LevelCharmDTO getLevelByName(@Param("levelName") String levelName);
}
