package com.erban.main.mybatismapper;

import com.erban.main.model.UserRealName;
import com.erban.main.model.UserRealNameExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRealNameMapper {
    int countByExample(UserRealNameExample example);

    int deleteByExample(UserRealNameExample example);

    int deleteByPrimaryKey(Long uid);

    int insert(UserRealName record);

    int insertSelective(UserRealName record);

    List<UserRealName> selectByExample(UserRealNameExample example);

    UserRealName selectByPrimaryKey(Long uid);

    int updateByExampleSelective(@Param("record") UserRealName record, @Param("example") UserRealNameExample example);

    int updateByExample(@Param("record") UserRealName record, @Param("example") UserRealNameExample example);

    int updateByPrimaryKeySelective(UserRealName record);

    int updateByPrimaryKey(UserRealName record);
}
