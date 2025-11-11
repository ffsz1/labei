package com.erban.main.mybatismapper;

import com.erban.main.model.StatWeekList;
import com.erban.main.model.StatWeekListExample;
import java.util.List;

public interface StatWeekListMapper {
    int deleteByPrimaryKey(Long roomUid);

    int insert(StatWeekList record);

    int insertSelective(StatWeekList record);

    List<StatWeekList> selectByExample(StatWeekListExample example);

    StatWeekList selectByPrimaryKey(Long roomUid);

    int updateByPrimaryKeySelective(StatWeekList record);

    int updateByPrimaryKey(StatWeekList record);
}
