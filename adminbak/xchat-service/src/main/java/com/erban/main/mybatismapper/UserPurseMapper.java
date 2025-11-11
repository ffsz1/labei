package com.erban.main.mybatismapper;

import com.erban.main.model.UserPurse;
import com.erban.main.model.UserPurseExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserPurseMapper {
    int countByExample(UserPurseExample example);

    int deleteByExample(UserPurseExample example);

    int deleteByPrimaryKey(Long uid);

    int insert(UserPurse record);

    int insertSelective(UserPurse record);

    List<UserPurse> selectByExample(UserPurseExample example);

    UserPurse selectByPrimaryKey(Long uid);

    int updateByExampleSelective(@Param("record") UserPurse record, @Param("example") UserPurseExample example);

    int updateByExample(@Param("record") UserPurse record, @Param("example") UserPurseExample example);

    int updateByPrimaryKeySelective(UserPurse record);

    int updateByPrimaryKey(UserPurse record);
}
