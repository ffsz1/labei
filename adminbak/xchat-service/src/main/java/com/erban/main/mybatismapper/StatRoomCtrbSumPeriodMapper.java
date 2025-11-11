package com.erban.main.mybatismapper;

import com.erban.main.model.StatRoomCtrbSumPeriod;
import com.erban.main.model.StatRoomCtrbSumPeriodExample;
import java.util.List;

public interface StatRoomCtrbSumPeriodMapper {
    int deleteByExample(StatRoomCtrbSumPeriodExample example);

    int deleteByPrimaryKey(Long uid);

    int insert(StatRoomCtrbSumPeriod record);

    int insertSelective(StatRoomCtrbSumPeriod record);

    List<StatRoomCtrbSumPeriod> selectByExample(StatRoomCtrbSumPeriodExample example);

    StatRoomCtrbSumPeriod selectByPrimaryKey(Long uid);

    int updateByPrimaryKeySelective(StatRoomCtrbSumPeriod record);

    int updateByPrimaryKey(StatRoomCtrbSumPeriod record);
}
