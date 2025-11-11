package com.erban.admin.main.mapper;

import com.erban.admin.main.model.GuildDailyTurnoverReport;
import com.erban.admin.main.model.GuildDailyTurnoverReportExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GuildDailyTurnoverReportMapper {
    int countByExample(GuildDailyTurnoverReportExample example);

    int deleteByExample(GuildDailyTurnoverReportExample example);

    int deleteByPrimaryKey(Long id);

    int insert(GuildDailyTurnoverReport record);

    int insertSelective(GuildDailyTurnoverReport record);

    List<GuildDailyTurnoverReport> selectByExample(GuildDailyTurnoverReportExample example);

    GuildDailyTurnoverReport selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") GuildDailyTurnoverReport record, @Param("example") GuildDailyTurnoverReportExample example);

    int updateByExample(@Param("record") GuildDailyTurnoverReport record, @Param("example") GuildDailyTurnoverReportExample example);

    int updateByPrimaryKeySelective(GuildDailyTurnoverReport record);

    int updateByPrimaryKey(GuildDailyTurnoverReport record);
}