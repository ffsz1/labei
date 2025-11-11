package com.erban.main.mybatismapper;

import com.erban.main.model.UserBankCard;
import com.erban.main.model.UserBankCardExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserBankCardMapper {
    int countByExample(UserBankCardExample example);

    int deleteByExample(UserBankCardExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserBankCard record);

    int insertSelective(UserBankCard record);

    List<UserBankCard> selectByExample(UserBankCardExample example);

    UserBankCard selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserBankCard record, @Param("example") UserBankCardExample example);

    int updateByExample(@Param("record") UserBankCard record, @Param("example") UserBankCardExample example);

    int updateByPrimaryKeySelective(UserBankCard record);

    int updateByPrimaryKey(UserBankCard record);
}
