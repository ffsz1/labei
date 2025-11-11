package com.erban.main.mybatismapper;

import com.erban.main.model.AccompanyManualRecomm;
import com.erban.main.model.AccompanyManualRecommExample;
import com.erban.main.vo.AccompayManualRecommVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AccompanyManualRecommMapper {
    int countByExample(AccompanyManualRecommExample example);

    int deleteByExample(AccompanyManualRecommExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AccompanyManualRecomm record);

    int insertSelective(AccompanyManualRecomm record);

    List<AccompanyManualRecomm> selectByExample(AccompanyManualRecommExample example);

    AccompanyManualRecomm selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AccompanyManualRecomm record, @Param("example") AccompanyManualRecommExample example);

    int updateByExample(@Param("record") AccompanyManualRecomm record, @Param("example") AccompanyManualRecommExample example);

    int updateByPrimaryKeySelective(AccompanyManualRecomm record);

    int updateByPrimaryKey(AccompanyManualRecomm record);


    List<AccompayManualRecommVO> selectByParam(Map<String, Object> map);
}
