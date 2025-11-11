package com.juxiao.xchat.dao.guild;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.guild.domain.GuildHallMemberDO;
import com.juxiao.xchat.dao.guild.dto.GuildHallMasterDTO;
import com.juxiao.xchat.dao.guild.dto.GuildHallMemberDTO;
import com.juxiao.xchat.dao.guild.dto.GuildHallMemberListDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface GuildHallMemberDao {
    int deleteByPrimaryKey(Long id);

    int insert(GuildHallMemberDO record);

    int insertSelective(GuildHallMemberDO record);

    @TargetDataSource(name = "ds2")
    GuildHallMemberDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GuildHallMemberDO record);

    int updateByPrimaryKey(GuildHallMemberDO record);

    @TargetDataSource(name = "ds2")
    GuildHallMemberDTO getGuildMemberInfo(Long uid);

    @TargetDataSource(name = "ds2")
    List<GuildHallMemberListDTO> getGuildMembers(Long guildId);

    @TargetDataSource(name = "ds2")
    List<GuildHallMemberListDTO> getHallMembers(Long hallId);

    @TargetDataSource(name = "ds2")
    @Select("select count(*) from guild_hall_member where uid=#{uid} and is_del=false")
    int countByUid(Long uid);

    @TargetDataSource(name = "ds2")
    List<GuildHallMemberDO> findByHallId(Long hallId);

    @TargetDataSource(name = "ds2")
    GuildHallMemberDO getByUid(Long uid);
}