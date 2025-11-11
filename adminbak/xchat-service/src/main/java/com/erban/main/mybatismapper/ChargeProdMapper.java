package com.erban.main.mybatismapper;

import com.erban.main.model.ChargeProd;
import com.erban.main.model.ChargeProdExample;
import java.util.List;

public interface ChargeProdMapper {
    int deleteByPrimaryKey(String chargeProdId);

    int insert(ChargeProd record);

    int insertSelective(ChargeProd record);

    List<ChargeProd> selectByExample(ChargeProdExample example);

    ChargeProd selectByPrimaryKey(String chargeProdId);

    int updateByPrimaryKeySelective(ChargeProd record);

    int updateByPrimaryKey(ChargeProd record);
}
