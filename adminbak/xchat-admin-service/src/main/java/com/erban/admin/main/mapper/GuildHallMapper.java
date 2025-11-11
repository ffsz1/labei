package com.erban.admin.main.mapper;

import com.erban.admin.main.model.GuildHall;
import com.erban.admin.main.model.GuildHallExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface GuildHallMapper {
    int countByExample(GuildHallExample example);

    int deleteByExample(GuildHallExample example);

    int deleteByPrimaryKey(Long id);

    int insert(GuildHall record);

    int insertSelective(GuildHall record);

    List<GuildHall> selectByExample(GuildHallExample example);

    GuildHall selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") GuildHall record, @Param("example") GuildHallExample example);

    int updateByExample(@Param("record") GuildHall record, @Param("example") GuildHallExample example);

    int updateByPrimaryKeySelective(GuildHall record);

    int updateByPrimaryKey(GuildHall record);
    
    GuildHall selectByRoomId(Long roomId);
    
    List<Map<String,Object>> findByGuildId(@Param("guildId")Long guildId);
    
    int isDelHallByGuildId(@Param("guildId")Long guildId);
    
    int isDelHallById(@Param("id")Long id);
    
    GuildHall selectById(@Param("id")Long id);
    
    List<GuildHall> findGuildHallList(@Param("guildId")Long guildId);
}