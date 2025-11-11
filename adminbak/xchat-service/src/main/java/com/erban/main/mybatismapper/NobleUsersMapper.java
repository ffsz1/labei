package com.erban.main.mybatismapper;

import com.erban.main.model.NobleUsers;
import com.erban.main.model.NobleUsersExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NobleUsersMapper {
    int countByExample(NobleUsersExample example);

    int deleteByExample(NobleUsersExample example);

    int deleteByPrimaryKey(Long uid);

    int insert(NobleUsers record);

    int insertSelective(NobleUsers record);

    List<NobleUsers> selectByExample(NobleUsersExample example);

    NobleUsers selectByPrimaryKey(Long uid);

    int updateByExampleSelective(@Param("record") NobleUsers record, @Param("example") NobleUsersExample example);

    int updateByExample(@Param("record") NobleUsers record, @Param("example") NobleUsersExample example);

    int updateByPrimaryKeySelective(NobleUsers record);

    int updateByPrimaryKey(NobleUsers record);
}
