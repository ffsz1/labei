package com.erban.main.mybatismapper;

import com.erban.main.model.DailyReport;
import com.erban.main.model.DailyReportExample;
import java.util.List;
import java.util.Map;

import com.erban.main.model.DailyUserPurse;
import org.apache.ibatis.annotations.Param;

public interface DailyReportMapper {
    int countByExample(DailyReportExample example);

    int deleteByExample(DailyReportExample example);

    int deleteByPrimaryKey(Integer reportId);

    int insert(DailyReport record);

    int insertSelective(DailyReport record);

    List<DailyReport> selectByExample(DailyReportExample example);

    DailyReport selectByPrimaryKey(Integer reportId);

    int updateByExampleSelective(@Param("record") DailyReport record, @Param("example") DailyReportExample example);

    int updateByExample(@Param("record") DailyReport record, @Param("example") DailyReportExample example);

    int updateByPrimaryKeySelective(DailyReport record);

    int updateByPrimaryKey(DailyReport record);

    List<DailyUserPurse> selectDailyUserPurse (Map<String, Object> map);

}
