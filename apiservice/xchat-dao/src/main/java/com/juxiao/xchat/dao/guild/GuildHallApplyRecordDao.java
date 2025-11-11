package com.juxiao.xchat.dao.guild;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.guild.domain.GuildHallApplyRecordDO;
import com.juxiao.xchat.dao.guild.dto.GuildHallApplyRecordDTO;

import java.util.List;

public interface GuildHallApplyRecordDao {
    int deleteByPrimaryKey(Long id);

    int insert(GuildHallApplyRecordDO record);

    int insertSelective(GuildHallApplyRecordDO record);

    @TargetDataSource(name = "ds2")
    GuildHallApplyRecordDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GuildHallApplyRecordDO record);

    int updateByPrimaryKey(GuildHallApplyRecordDO record);

    @TargetDataSource(name = "ds2")
    List<GuildHallApplyRecordDTO> selectByUidAndTypeAndStatus(Long uid, byte type, byte status);

    @TargetDataSource(name = "ds2")
    List<GuildHallApplyRecordDO> findByUidAndTypeAndStatus(Long uid, byte type, byte status);

    @TargetDataSource(name = "ds2")
    List<GuildHallApplyRecordDTO> getPageByGuildId(int startIndex, int pageSize, Long guildId);

    @TargetDataSource(name = "ds2")
    List<GuildHallApplyRecordDTO> getPageByHallId(int startIndex, int pageSize, Long hallId);

    @TargetDataSource(name = "ds2")
    int countAuditingByGuildIdAndStatus(Long guildId, byte status);

    @TargetDataSource(name = "ds2")
    int countAuditingByHallIdAndStatus(Long hallId, byte status);
}