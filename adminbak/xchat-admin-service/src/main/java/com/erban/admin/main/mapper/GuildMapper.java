package com.erban.admin.main.mapper;

import com.erban.admin.main.model.Guild;
import com.erban.admin.main.model.GuildExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
public interface GuildMapper {
    int countByExample(GuildExample example);

    int deleteByExample(GuildExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Guild record);

    int insertSelective(Guild record);

    List<Guild> selectByExample(GuildExample example);

    Guild selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Guild record, @Param("example") GuildExample example);

    int updateByExample(@Param("record") Guild record, @Param("example") GuildExample example);

    int updateByPrimaryKeySelective(Guild record);

    int updateByPrimaryKey(Guild record);
    
    Guild findNotIsDelById(@Param("id")Long id);
    
    List<Guild> getGuildList(@Param("guildNo")String guildNo,@Param("erbanNo") Long erbanNo);
    
    List<Map<String,Object>> getGuildAll();
}