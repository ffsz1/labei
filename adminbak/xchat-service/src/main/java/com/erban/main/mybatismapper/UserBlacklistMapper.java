package com.erban.main.mybatismapper;

import com.erban.main.model.UserBlacklist;
import com.erban.main.model.UserBlacklistExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserBlacklistMapper {
    int countByExample(UserBlacklistExample example);

    int deleteByExample(UserBlacklistExample example);

    int deleteByPrimaryKey(Integer blackId);

    int insert(UserBlacklist record);

    int insertSelective(UserBlacklist record);

    List<UserBlacklist> selectByExample(UserBlacklistExample example);

    UserBlacklist selectByPrimaryKey(Integer blackId);

    int updateByExampleSelective(@Param("record") UserBlacklist record, @Param("example") UserBlacklistExample example);

    int updateByExample(@Param("record") UserBlacklist record, @Param("example") UserBlacklistExample example);

    int updateByPrimaryKeySelective(UserBlacklist record);

    int updateByPrimaryKey(UserBlacklist record);
}
