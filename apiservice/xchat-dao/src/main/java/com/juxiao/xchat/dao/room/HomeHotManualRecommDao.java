package com.juxiao.xchat.dao.room;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.room.domain.HomeHotManualRecommDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

/**
 * @class: HomeHotManualRecommDao.java
 * @author: chenjunsheng
 * @date 2018/7/12
 */
public interface HomeHotManualRecommDao {

    /**
     * 保存推荐位
     *
     * @param recommDo
     */
    @TargetDataSource
    void save(HomeHotManualRecommDO recommDo);

    /**
     * @param uid
     * @param startTime
     * @param endTime
     * @return
     */
    @TargetDataSource(name = "ds2")
    @Select("SELECT count(1) FROM home_hot_manual_recomm WHERE uid = #{uid} AND `status` = 1 AND seq_no = 1 AND start_valid_time = #{startTime} AND end_valid_time = #{endTime} ")
    int countUserHotRecomm(@Param("uid") Long uid, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
