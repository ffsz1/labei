package com.erban.main.mybatismapper;

import com.erban.main.model.StatSumRoom;
import com.erban.main.model.StatSumRoomExample;
import java.util.List;

public interface StatSumRoomMapper {
    int deleteByPrimaryKey(Long roomUid);

    int insert(StatSumRoom record);

    int insertSelective(StatSumRoom record);

    List<StatSumRoom> selectByExample(StatSumRoomExample example);

    StatSumRoom selectByPrimaryKey(Long roomUid);

    int updateByPrimaryKeySelective(StatSumRoom record);

    int updateByPrimaryKey(StatSumRoom record);
}
