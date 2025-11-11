package com.erban.main.mybatismapper;

import com.erban.main.model.UserNoblePrettyNoApp;
import com.erban.main.model.UserNoblePrettyNoAppExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserNoblePrettyNoAppMapper {
    int countByExample(UserNoblePrettyNoAppExample example);

    int deleteByExample(UserNoblePrettyNoAppExample example);

    int deleteByPrimaryKey(Integer recordId);

    int insert(UserNoblePrettyNoApp record);

    int insertSelective(UserNoblePrettyNoApp record);

    List<UserNoblePrettyNoApp> selectByExample(UserNoblePrettyNoAppExample example);

    UserNoblePrettyNoApp selectByPrimaryKey(Integer recordId);

    int updateByExampleSelective(@Param("record") UserNoblePrettyNoApp record, @Param("example") UserNoblePrettyNoAppExample example);

    int updateByExample(@Param("record") UserNoblePrettyNoApp record, @Param("example") UserNoblePrettyNoAppExample example);

    int updateByPrimaryKeySelective(UserNoblePrettyNoApp record);

    int updateByPrimaryKey(UserNoblePrettyNoApp record);
}
