package com.erban.main.mybatismapper;

import com.erban.main.model.StatRoomFlowOnlinePeriod;
import com.erban.main.model.StatRoomFlowOnlinePeriodExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StatRoomFlowOnlinePeriodMapper {
    int countByExample(StatRoomFlowOnlinePeriodExample example);

    int deleteByExample(StatRoomFlowOnlinePeriodExample example);

    int deleteByPrimaryKey(Long uid);

    int insert(StatRoomFlowOnlinePeriod record);

    int insertSelective(StatRoomFlowOnlinePeriod record);

    List<StatRoomFlowOnlinePeriod> selectByExample(StatRoomFlowOnlinePeriodExample example);

    StatRoomFlowOnlinePeriod selectByPrimaryKey(Long uid);

    int updateByExampleSelective(@Param("record") StatRoomFlowOnlinePeriod record, @Param("example") StatRoomFlowOnlinePeriodExample example);

    int updateByExample(@Param("record") StatRoomFlowOnlinePeriod record, @Param("example") StatRoomFlowOnlinePeriodExample example);

    int updateByPrimaryKeySelective(StatRoomFlowOnlinePeriod record);

    int updateByPrimaryKey(StatRoomFlowOnlinePeriod record);
}
