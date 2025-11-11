package com.erban.admin.main.mapper;

import com.erban.admin.main.model.GuildHallApplyRecord;
import com.erban.admin.main.model.GuildHallApplyRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GuildHallApplyRecordMapper {
    int countByExample(GuildHallApplyRecordExample example);

    int deleteByExample(GuildHallApplyRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(GuildHallApplyRecord record);

    int insertSelective(GuildHallApplyRecord record);

    List<GuildHallApplyRecord> selectByExample(GuildHallApplyRecordExample example);

    GuildHallApplyRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") GuildHallApplyRecord record, @Param("example") GuildHallApplyRecordExample example);

    int updateByExample(@Param("record") GuildHallApplyRecord record, @Param("example") GuildHallApplyRecordExample example);

    int updateByPrimaryKeySelective(GuildHallApplyRecord record);

    int updateByPrimaryKey(GuildHallApplyRecord record);
}