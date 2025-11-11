package com.erban.admin.main.mapper;

import com.erban.admin.main.model.AccountBanned;
import com.erban.admin.main.model.AccountBannedExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccountBannedMapper {
    long countByExample(AccountBannedExample example);

    int deleteByExample(AccountBannedExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AccountBanned record);

    int insertSelective(AccountBanned record);

    List<AccountBanned> selectByExample(AccountBannedExample example);

    AccountBanned selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AccountBanned record, @Param("example") AccountBannedExample example);

    int updateByExample(@Param("record") AccountBanned record, @Param("example") AccountBannedExample example);

    int updateByPrimaryKeySelective(AccountBanned record);

    int updateByPrimaryKey(AccountBanned record);
}
