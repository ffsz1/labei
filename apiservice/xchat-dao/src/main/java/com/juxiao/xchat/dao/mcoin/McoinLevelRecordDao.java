package com.juxiao.xchat.dao.mcoin;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.mcoin.domain.McoinLevelRecordDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface McoinLevelRecordDao {

    /**
     * 保存
     *
     * @param recordDo
     */
    @TargetDataSource
    @Insert("INSERT INTO `mcoin_level_record`(`uid`, `level`, `mcoin_num`, `create_time`, `update_time`) VALUES (#{uid}, #{level}, #{mcoinNum}, NOW(), NOW());")
    void save(McoinLevelRecordDO recordDo);

    /**
     * 计算等级记录
     *
     * @param uid
     * @param level
     * @return
     */
    @TargetDataSource
    @Select("SELECT COUNT(*) FROM `mcoin_level_record` WHERE `uid` = #{uid} AND `level` = #{level}")
    int countLevelMcoinRecord(@Param("uid") Long uid, @Param("level") Integer level);
}
