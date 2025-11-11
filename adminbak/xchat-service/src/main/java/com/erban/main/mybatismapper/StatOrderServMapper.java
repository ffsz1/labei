package com.erban.main.mybatismapper;

import com.erban.main.model.StatOrderServ;
import com.erban.main.model.StatOrderServExample;
import java.util.List;

public interface StatOrderServMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(StatOrderServ record);

    int insertSelective(StatOrderServ record);

    List<StatOrderServ> selectByExample(StatOrderServExample example);

    StatOrderServ selectByPrimaryKey(Long orderId);

    int updateByPrimaryKeySelective(StatOrderServ record);

    int updateByPrimaryKey(StatOrderServ record);
}
