package com.erban.main.mybatismapper;

import com.erban.main.dto.UsersWithdrawWhitelistDTO;
import com.erban.main.model.UsersWithdrawWhitelist;
import com.erban.main.model.UsersWithdrawWhitelistExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UsersWithdrawWhitelistMapper {
    int countByExample(UsersWithdrawWhitelistExample example);

    int deleteByExample(UsersWithdrawWhitelistExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UsersWithdrawWhitelist record);

    int insertSelective(UsersWithdrawWhitelist record);

    List<UsersWithdrawWhitelist> selectByExample(UsersWithdrawWhitelistExample example);

    UsersWithdrawWhitelist selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UsersWithdrawWhitelist record, @Param("example") UsersWithdrawWhitelistExample example);

    int updateByExample(@Param("record") UsersWithdrawWhitelist record, @Param("example") UsersWithdrawWhitelistExample example);

    int updateByPrimaryKeySelective(UsersWithdrawWhitelist record);

    int updateByPrimaryKey(UsersWithdrawWhitelist record);

    List<UsersWithdrawWhitelistDTO> selectByParam(Map<String, Object> map);
}
