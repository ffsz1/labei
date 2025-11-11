package com.erban.main.mybatismapper;

import com.erban.main.model.Fans;
import com.erban.main.model.FansExample;
import java.util.List;

public interface FansMapper {
    int deleteByPrimaryKey(Long fanId);

    int insert(Fans record);

    int insertSelective(Fans record);

    List<Fans> selectByExample(FansExample example);

    Fans selectByPrimaryKey(Long fanId);

    int updateByPrimaryKeySelective(Fans record);

    int updateByPrimaryKey(Fans record);
}
