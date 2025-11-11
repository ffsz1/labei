package com.erban.main.mybatismapper;

import com.erban.main.base.BaseMapper;
import com.erban.main.model.Headwear;
import com.erban.main.model.HeadwearExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface HeadwearMapper extends BaseMapper<Headwear, HeadwearExample> {
    int countByExample(HeadwearExample example);

    int deleteByExample(HeadwearExample example);

    int deleteByPrimaryKey(Integer headwearId);

    int insert(Headwear record);

    int insertSelective(Headwear record);

    List<Headwear> selectByExample(HeadwearExample example);

    Headwear selectByPrimaryKey(Integer headwearId);

    int updateByExampleSelective(@Param("record") Headwear record, @Param("example") HeadwearExample example);

    int updateByExample(@Param("record") Headwear record, @Param("example") HeadwearExample example);

    int updateByPrimaryKeySelective(Headwear record);

    int updateByPrimaryKey(Headwear record);
}
