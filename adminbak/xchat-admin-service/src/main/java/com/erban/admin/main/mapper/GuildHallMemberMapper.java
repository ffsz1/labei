package com.erban.admin.main.mapper;

import com.erban.admin.main.model.GuildHallMember;
import com.erban.admin.main.model.GuildHallMemberExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GuildHallMemberMapper {
    int countByExample(GuildHallMemberExample example);

    int deleteByExample(GuildHallMemberExample example);

    int deleteByPrimaryKey(Long id);

    int insert(GuildHallMember record);

    int insertSelective(GuildHallMember record);

    List<GuildHallMember> selectByExample(GuildHallMemberExample example);

    GuildHallMember selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") GuildHallMember record, @Param("example") GuildHallMemberExample example);

    int updateByExample(@Param("record") GuildHallMember record, @Param("example") GuildHallMemberExample example);

    int updateByPrimaryKeySelective(GuildHallMember record);

    int updateByPrimaryKey(GuildHallMember record);
    
    int isDelMemberByGuildId(@Param("guildId")Long guildId);
    
    int isDelMemberByHallId(@Param("hallId")Long hallId);
    
    int isDelMemberById(@Param("id")Long id);
    
    List<GuildHallMember> findGuildMember(@Param("guildId")Long guildId);
    List<GuildHallMember> findHallMember(@Param("guildId")Long guildId,@Param("hallId")Long hallId);
}