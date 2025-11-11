package com.juxiao.xchat.dao.guild;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.guild.domain.GuildDailyTurnoverReportDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GuildDailyTurnoverReportDao {
    int deleteByPrimaryKey(Long id);

    int insert(GuildDailyTurnoverReportDO record);

    int insertSelective(GuildDailyTurnoverReportDO record);

    @TargetDataSource(name = "ds2")
    GuildDailyTurnoverReportDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GuildDailyTurnoverReportDO record);

    int updateByPrimaryKey(GuildDailyTurnoverReportDO record);

    /**
     * 获取用户的流水记录，按日期倒序
     * @param memberId 成员id
     * @param hallId 厅id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<GuildDailyTurnoverReportDO> getMemberTurnovers(@Param("memberId") Long memberId, @Param("hallId") Long hallId, @Param("startTime") String startTime, @Param("endTime") String endTime);


    /**
     * 获取指定条件下的数据列表（参数可空）
     * @param memberId
     * @param hallId
     * @param guildId
     * @param date
     * @return
     */
    @TargetDataSource(name = "ds2")
    GuildDailyTurnoverReportDO getByParams(@Param("memberId") Long memberId, @Param("hallId") Long hallId, @Param("guildId") Long guildId, @Param("date") String date);

    /**
     * 从礼物发送记录中统计公会流水
     * @param start_time
     * @param end_time
     * @param guildId
     * @return
     */
    @TargetDataSource(name = "ds2")
    Long sumGuildTurnover(String start_time, String end_time, Long guildId);

    /**
     * 从礼物发送记录中统计厅流水
     * @param start_time
     * @param end_time
     * @param hallId
     * @return
     */
    @TargetDataSource(name = "ds2")
    Long sumHallTurnover(String start_time, String end_time, Long hallId);

    /**
     * 从礼物发送记录中统计成员流水
     * @param start_time
     * @param end_time
     * @param memberId
     * @return
     */
    @TargetDataSource(name = "ds2")
    Long sumMemberTurnover(String start_time, String end_time, Long hallId, Long memberId);


    /**
     * 从公会日流水表（本表）中统计时间段内流水
     * @param memberId
     * @param hallId
     * @param startTime
     * @param endTime
     * @return
     */
    @TargetDataSource(name = "ds2")
    Long sumGuildDailyTurnover(Long memberId, Long hallId, Long guildId, String startTime, String endTime);
}