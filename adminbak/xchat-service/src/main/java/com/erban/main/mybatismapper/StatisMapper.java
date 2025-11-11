package com.erban.main.mybatismapper;

import com.erban.main.model.Statis;
import com.erban.main.model.StatisExample;
import java.util.List;

public interface StatisMapper {
    int deleteByPrimaryKey(String statisId);

    int insert(Statis record);

    int insertSelective(Statis record);

    List<Statis> selectByExample(StatisExample example);

    Statis selectByPrimaryKey(String statisId);

    int updateByPrimaryKeySelective(Statis record);

    int updateByPrimaryKey(Statis record);
}
