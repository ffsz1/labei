package com.juxiao.xchat.dao.guild;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.guild.domain.GuildHallDO;
import com.juxiao.xchat.dao.guild.dto.GuildHallDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

public interface GuildHallDao {
    int deleteByPrimaryKey(Long id);

    int insert(GuildHallDO record);

    int insertSelective(GuildHallDO record);

    @TargetDataSource(name = "ds2")
    GuildHallDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GuildHallDO record);

    int updateByPrimaryKey(GuildHallDO record);

    @TargetDataSource(name = "ds2")
    GuildHallDTO getDTOById(@Param("id") Long id);

    /**
     * 获取公会的厅列表，按当日流水倒序排序
     * @param guildId
     * @param date 指定排序的日期
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<GuildHallDTO> findByGuildIdOrderByGoldDesc(@Param("guildId") Long guildId, @Param("date") Date date);

    @TargetDataSource(name = "ds2")
    GuildHallDTO getDTOByHallUid(Long uid);

    /**
     * 获取公会的厅列表
     * @param guildId
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<GuildHallDO> findByGuildId(@Param("guildId") Long guildId);

//    @TargetDataSource(name = "ds2")
//    @Select("select count(*) from guild_hall where guild_id=#{guildId} and is_del=false")
//    int countByGuildId(Long guildId);
//
//    @TargetDataSource(name = "ds2")
//    @Select("select count(h.id) from guild_hall h inner join room r on h.room_id=r.room_id where h.is_del=false and h.guild_id=#{guildId} and r.uid<>#{presidentUid}")
//    int countByGuildIdAndPresidentUid(Long guildId, Long presidentUid);
}