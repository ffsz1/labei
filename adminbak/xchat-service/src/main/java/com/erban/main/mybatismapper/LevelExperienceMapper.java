package com.erban.main.mybatismapper;

import com.erban.main.model.LevelExperience;
import com.erban.main.model.LevelExperienceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface LevelExperienceMapper {
    int countByExample(LevelExperienceExample example);

    int deleteByExample(LevelExperienceExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(LevelExperience record);

    int insertSelective(LevelExperience record);

    List<LevelExperience> selectByExample(LevelExperienceExample example);

    LevelExperience selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") LevelExperience record, @Param("example") LevelExperienceExample example);

    int updateByExample(@Param("record") LevelExperience record, @Param("example") LevelExperienceExample example);

    int updateByPrimaryKeySelective(LevelExperience record);

    int updateByPrimaryKey(LevelExperience record);

    @Select("SELECT " +
            "  `level_name` AS `levelName`" +
            "FROM " +
            "  `level_experience` " +
            "WHERE " +
            "   need_min <= #{experience} " +
            "AND " +
            "  need_max > #{experience} " +
            "AND " +
            "  `status` = 1")
    String getExperienceLevel(Long experience);

    @Select("SELECT " +
            "  `level_name` AS `levelName` " +
            "FROM " +
            "  `level_charm` " +
            "WHERE " +
            "   `need_min` <= #{experience} " +
            "AND " +
            "  `need_max` > #{experience} " +
            "AND " +
            "  `status` = 1")
    String getCharm(Long experience);
}
