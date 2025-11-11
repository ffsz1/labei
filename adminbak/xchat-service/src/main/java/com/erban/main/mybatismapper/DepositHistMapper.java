package com.erban.main.mybatismapper;

import com.erban.main.model.DepositHist;
import com.erban.main.model.DepositHistExample;
import java.util.List;

public interface DepositHistMapper {
    int deleteByPrimaryKey(String did);

    int insert(DepositHist record);

    int insertSelective(DepositHist record);

    List<DepositHist> selectByExample(DepositHistExample example);

    DepositHist selectByPrimaryKey(String did);

    int updateByPrimaryKeySelective(DepositHist record);

    int updateByPrimaryKey(DepositHist record);
}
