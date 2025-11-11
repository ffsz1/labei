package com.erban.main.mybatismapper;

import com.erban.main.model.Valentine;
import com.erban.main.model.ValentineExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ValentineMapper {
    int countByExample(ValentineExample example);

    int deleteByExample(ValentineExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Valentine record);

    int insertSelective(Valentine record);

    List<Valentine> selectByExample(ValentineExample example);

    Valentine selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Valentine record, @Param("example") ValentineExample example);

    int updateByExample(@Param("record") Valentine record, @Param("example") ValentineExample example);

    int updateByPrimaryKeySelective(Valentine record);

    int updateByPrimaryKey(Valentine record);
}
