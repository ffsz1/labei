package com.erban.main.mybatismapper;

import com.erban.main.model.Icon;
import com.erban.main.model.IconExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface IconMapper {
    int countByExample(IconExample example);

    int deleteByExample(IconExample example);

    int deleteByPrimaryKey(Integer iconId);

    int insert(Icon record);

    int insertSelective(Icon record);

    List<Icon> selectByExample(IconExample example);

    Icon selectByPrimaryKey(Integer iconId);

    int updateByExampleSelective(@Param("record") Icon record, @Param("example") IconExample example);

    int updateByExample(@Param("record") Icon record, @Param("example") IconExample example);

    int updateByPrimaryKeySelective(Icon record);

    int updateByPrimaryKey(Icon record);
}
