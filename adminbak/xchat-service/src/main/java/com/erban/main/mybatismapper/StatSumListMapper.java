package com.erban.main.mybatismapper;

import com.erban.main.model.StatSumList;
import com.erban.main.model.StatSumListExample;
import java.util.List;

public interface StatSumListMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StatSumList record);

    int insertSelective(StatSumList record);

    List<StatSumList> selectByExample(StatSumListExample example);

    StatSumList selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StatSumList record);

    int updateByPrimaryKey(StatSumList record);
}
