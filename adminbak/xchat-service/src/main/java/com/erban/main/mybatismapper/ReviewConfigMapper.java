package com.erban.main.mybatismapper;

import com.erban.main.dto.ReviewConfigDTO;
import com.erban.main.model.ReviewConfig;
import com.erban.main.model.ReviewConfigExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReviewConfigMapper {
    int countByExample(ReviewConfigExample example);

    int deleteByExample(ReviewConfigExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ReviewConfig record);

    int insertSelective(ReviewConfig record);

    List<ReviewConfig> selectByExample(ReviewConfigExample example);

    ReviewConfig selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ReviewConfig record, @Param("example") ReviewConfigExample example);

    int updateByExample(@Param("record") ReviewConfig record, @Param("example") ReviewConfigExample example);

    int updateByPrimaryKeySelective(ReviewConfig record);

    int updateByPrimaryKey(ReviewConfig record);

    List<ReviewConfigDTO> selectByList(@Param("searchText") String searchText);
}
