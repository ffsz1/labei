package com.erban.main.mybatismapper;

import com.erban.main.model.NobleRight;
import com.erban.main.model.NobleRightExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NobleRightMapper {
    int countByExample(NobleRightExample example);

    int deleteByExample(NobleRightExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(NobleRight record);

    int insertSelective(NobleRight record);

    List<NobleRight> selectByExample(NobleRightExample example);

    NobleRight selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") NobleRight record, @Param("example") NobleRightExample example);

    int updateByExample(@Param("record") NobleRight record, @Param("example") NobleRightExample example);

    int updateByPrimaryKeySelective(NobleRight record);

    int updateByPrimaryKey(NobleRight record);
}
