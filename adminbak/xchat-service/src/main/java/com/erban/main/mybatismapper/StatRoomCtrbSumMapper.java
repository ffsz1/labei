package com.erban.main.mybatismapper;

import com.erban.main.model.StatRoomCtrbSum;
import com.erban.main.model.StatRoomCtrbSumExample;
import java.util.List;

public interface StatRoomCtrbSumMapper {
    int deleteByPrimaryKey(Integer ctrbId);

    int insert(StatRoomCtrbSum record);

    int insertSelective(StatRoomCtrbSum record);

    List<StatRoomCtrbSum> selectByExample(StatRoomCtrbSumExample example);

    StatRoomCtrbSum selectByPrimaryKey(Integer ctrbId);

    int updateByPrimaryKeySelective(StatRoomCtrbSum record);

    int updateByPrimaryKey(StatRoomCtrbSum record);
}
