package com.erban.main.mybatismapper;

import com.erban.main.model.Deposit;
import com.erban.main.model.DepositExample;
import java.util.List;

public interface DepositMapper {
    int deleteByExample(DepositExample example);

    int deleteByPrimaryKey(String did);

    int insert(Deposit record);

    int insertSelective(Deposit record);

    List<Deposit> selectByExample(DepositExample example);

    Deposit selectByPrimaryKey(String did);

    int updateByPrimaryKeySelective(Deposit record);

    int updateByPrimaryKey(Deposit record);
}
