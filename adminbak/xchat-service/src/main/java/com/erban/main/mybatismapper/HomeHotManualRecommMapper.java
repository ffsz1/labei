package com.erban.main.mybatismapper;

import com.erban.main.model.HomeHotManualRecomm;
import com.erban.main.model.HomeHotManualRecommExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface HomeHotManualRecommMapper {
    int countByExample(HomeHotManualRecommExample example);

    int deleteByExample(HomeHotManualRecommExample example);

    int deleteByPrimaryKey(Integer recommId);

    int insert(HomeHotManualRecomm record);

    int insertSelective(HomeHotManualRecomm record);

    List<HomeHotManualRecomm> selectByExample(HomeHotManualRecommExample example);

    HomeHotManualRecomm selectByPrimaryKey(Integer recommId);

    int updateByExampleSelective(@Param("record") HomeHotManualRecomm record, @Param("example") HomeHotManualRecommExample example);

    int updateByExample(@Param("record") HomeHotManualRecomm record, @Param("example") HomeHotManualRecommExample example);

    int updateByPrimaryKeySelective(HomeHotManualRecomm record);

    int updateByPrimaryKey(HomeHotManualRecomm record);
}
