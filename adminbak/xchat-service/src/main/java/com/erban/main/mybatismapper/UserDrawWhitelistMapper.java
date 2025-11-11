package com.erban.main.mybatismapper;

import com.erban.main.model.UserDrawWhitelist;
import com.erban.main.model.UserDrawWhitelistExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDrawWhitelistMapper {
    int countByExample(UserDrawWhitelistExample example);

    int deleteByExample(UserDrawWhitelistExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserDrawWhitelist record);

    int insertSelective(UserDrawWhitelist record);

    List<UserDrawWhitelist> selectByExample(UserDrawWhitelistExample example);

    UserDrawWhitelist selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserDrawWhitelist record, @Param("example") UserDrawWhitelistExample example);

    int updateByExample(@Param("record") UserDrawWhitelist record, @Param("example") UserDrawWhitelistExample example);

    int updateByPrimaryKeySelective(UserDrawWhitelist record);

    int updateByPrimaryKey(UserDrawWhitelist record);

    List<Long> selectByUids(@Param("uid") Long uid);
}
