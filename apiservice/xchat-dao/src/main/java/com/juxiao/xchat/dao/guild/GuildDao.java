package com.juxiao.xchat.dao.guild;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.guild.domain.GuildDO;
import com.juxiao.xchat.dao.guild.dto.GuildDTO;
import com.juxiao.xchat.dao.guild.dto.GuildExtendDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 描述： 公会
 */
public interface GuildDao {
    int deleteByPrimaryKey(Long id);

    int insert(GuildDO record);

    int insertSelective(GuildDO record);

    @TargetDataSource(name = "ds2")
    GuildDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GuildDO record);

    int updateByPrimaryKey(GuildDO record);

    @TargetDataSource(name = "ds2")
    List<GuildDTO> getRecommendList(Integer startIndex, Integer pageSize);

    @TargetDataSource(name = "ds2")
    @Select("select id, guild_no, name, logo_url, president_uid, create_time, update_time, is_del from guild where is_del=false")
    List<GuildDO> getValidList();

    @TargetDataSource(name = "ds2")
    GuildDTO getDTOById(Long id);

    @TargetDataSource(name = "ds2")
    List<GuildExtendDTO> searchValidList(@Param("key") String key);

    @TargetDataSource(name = "ds2")
    GuildDTO getDTOByPresidentUid(Long presidentUid);
}
