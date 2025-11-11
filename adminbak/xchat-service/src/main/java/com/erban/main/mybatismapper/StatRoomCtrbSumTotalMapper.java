package com.erban.main.mybatismapper;

import com.erban.main.model.StatRoomCtrbSumTotal;
import com.erban.main.model.StatRoomCtrbSumTotalExample;
import com.erban.main.param.admin.RoomFlowParam;
import com.erban.main.vo.admin.StatRoomFlowVo;

import java.util.List;

public interface StatRoomCtrbSumTotalMapper {
    int deleteByPrimaryKey(Long uid);

    int insert(StatRoomCtrbSumTotal record);

    int insertSelective(StatRoomCtrbSumTotal record);

    List<StatRoomCtrbSumTotal> selectByExample(StatRoomCtrbSumTotalExample example);

    StatRoomCtrbSumTotal selectByPrimaryKey(Long uid);

    int updateByPrimaryKeySelective(StatRoomCtrbSumTotal record);

    int updateByPrimaryKey(StatRoomCtrbSumTotal record);

    List<StatRoomFlowVo> selectByQuery(RoomFlowParam roomFlowParam);
}
