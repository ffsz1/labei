package com.erban.main.mybatismapper;

import com.erban.main.model.RoomOpenHist;
import com.erban.main.model.RoomOpenHistExample;
import java.util.List;

public interface RoomOpenHistMapper {
    int deleteByPrimaryKey(Long uid);

    int insert(RoomOpenHist record);

    int insertSelective(RoomOpenHist record);

    List<RoomOpenHist> selectByExample(RoomOpenHistExample example);

    RoomOpenHist selectByPrimaryKey(Long uid);

    int updateByPrimaryKeySelective(RoomOpenHist record);

    int updateByPrimaryKey(RoomOpenHist record);
}
