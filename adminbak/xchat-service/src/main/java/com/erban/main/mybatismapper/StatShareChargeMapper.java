package com.erban.main.mybatismapper;

import com.erban.main.model.StatShareCharge;
import com.erban.main.model.StatShareChargeExample;
import java.util.List;

public interface StatShareChargeMapper {
    int deleteByPrimaryKey(Integer shareChargeId);

    int insert(StatShareCharge record);

    int insertSelective(StatShareCharge record);

    List<StatShareCharge> selectByExample(StatShareChargeExample example);

    StatShareCharge selectByPrimaryKey(Integer shareChargeId);

    int updateByPrimaryKeySelective(StatShareCharge record);

    int updateByPrimaryKey(StatShareCharge record);
}
