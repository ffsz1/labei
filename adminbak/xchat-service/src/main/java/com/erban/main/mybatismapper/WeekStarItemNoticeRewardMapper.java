package com.erban.main.mybatismapper;

import com.erban.main.dto.WeekStarItemRewardDTO;
import com.erban.main.model.WeekStarItemNoticeReward;
import com.erban.main.model.WeekStarItemNoticeRewardExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WeekStarItemNoticeRewardMapper {
    int countByExample(WeekStarItemNoticeRewardExample example);

    int deleteByExample(WeekStarItemNoticeRewardExample example);

    int deleteByPrimaryKey(Long id);

    int insert(WeekStarItemNoticeReward record);

    int insertSelective(WeekStarItemNoticeReward record);

    List<WeekStarItemNoticeReward> selectByExample(WeekStarItemNoticeRewardExample example);

    WeekStarItemNoticeReward selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") WeekStarItemNoticeReward record, @Param("example") WeekStarItemNoticeRewardExample example);

    int updateByExample(@Param("record") WeekStarItemNoticeReward record, @Param("example") WeekStarItemNoticeRewardExample example);

    int updateByPrimaryKeySelective(WeekStarItemNoticeReward record);

    int updateByPrimaryKey(WeekStarItemNoticeReward record);

    List<WeekStarItemRewardDTO> selectByList(@Param("searchText") String searchText);
}
