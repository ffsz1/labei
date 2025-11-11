package com.erban.main.mybatismapper;

import com.erban.main.model.StatBasicUsers;
import com.erban.main.model.StatBasicUsersExample;
import java.util.List;

public interface StatBasicUsersMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StatBasicUsers record);

    int insertSelective(StatBasicUsers record);

    List<StatBasicUsers> selectByExample(StatBasicUsersExample example);

    StatBasicUsers selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StatBasicUsers record);

    int updateByPrimaryKey(StatBasicUsers record);
}
