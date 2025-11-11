package com.erban.main.mybatismapper;

import com.erban.main.model.StatWeekLists;
import com.erban.main.model.StatWeekListsExample;
import java.util.List;

public interface StatWeekListsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StatWeekLists record);

    int insertSelective(StatWeekLists record);

    List<StatWeekLists> selectByExample(StatWeekListsExample example);

    StatWeekLists selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StatWeekLists record);

    int updateByPrimaryKey(StatWeekLists record);
}
