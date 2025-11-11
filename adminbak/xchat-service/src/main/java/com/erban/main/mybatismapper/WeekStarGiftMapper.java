package com.erban.main.mybatismapper;

import com.erban.main.dto.WeekStarGiftDTO;
import com.erban.main.model.WeekStarGift;
import com.erban.main.model.WeekStarGiftExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WeekStarGiftMapper {
    int countByExample(WeekStarGiftExample example);

    int deleteByExample(WeekStarGiftExample example);

    int deleteByPrimaryKey(Long id);

    int insert(WeekStarGift record);

    int insertSelective(WeekStarGift record);

    List<WeekStarGift> selectByExample(WeekStarGiftExample example);

    WeekStarGift selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") WeekStarGift record, @Param("example") WeekStarGiftExample example);

    int updateByExample(@Param("record") WeekStarGift record, @Param("example") WeekStarGiftExample example);

    int updateByPrimaryKeySelective(WeekStarGift record);

    int updateByPrimaryKey(WeekStarGift record);

    List<WeekStarGiftDTO> selectByList(@Param("searchText") String searchText);

    int countEffective();

    List<WeekStarGiftDTO> selectByEffectiveGifts();

    List<WeekStarGiftDTO> selectByNormalGifts();

    WeekStarGift selectByGiftId(@Param("giftId") Integer giftId);
}
