package com.erban.main.mybatismapper;

import com.erban.main.dto.WeekStarItemRewardDTO;
import com.erban.main.model.WeekStarItemReward;
import com.erban.main.model.WeekStarItemRewardExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WeekStarItemRewardMapper {
    int countByExample(WeekStarItemRewardExample example);

    int deleteByExample(WeekStarItemRewardExample example);

    int deleteByPrimaryKey(Long id);

    int insert(WeekStarItemReward record);

    int insertSelective(WeekStarItemReward record);

    List<WeekStarItemReward> selectByExample(WeekStarItemRewardExample example);

    WeekStarItemReward selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") WeekStarItemReward record, @Param("example") WeekStarItemRewardExample example);

    int updateByExample(@Param("record") WeekStarItemReward record, @Param("example") WeekStarItemRewardExample example);

    int updateByPrimaryKeySelective(WeekStarItemReward record);

    int updateByPrimaryKey(WeekStarItemReward record);

    List<WeekStarItemRewardDTO> selectByList(@Param("searchText") String searchText);

    int countEffective();
}
