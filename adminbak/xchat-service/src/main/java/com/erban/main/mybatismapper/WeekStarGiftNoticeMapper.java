package com.erban.main.mybatismapper;

import com.erban.main.dto.WeekStarGiftDTO;
import com.erban.main.model.WeekStarGiftNotice;
import com.erban.main.model.WeekStarGiftNoticeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WeekStarGiftNoticeMapper {
    int countByExample(WeekStarGiftNoticeExample example);

    int deleteByExample(WeekStarGiftNoticeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(WeekStarGiftNotice record);

    int insertSelective(WeekStarGiftNotice record);

    List<WeekStarGiftNotice> selectByExample(WeekStarGiftNoticeExample example);

    WeekStarGiftNotice selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") WeekStarGiftNotice record, @Param("example") WeekStarGiftNoticeExample example);

    int updateByExample(@Param("record") WeekStarGiftNotice record, @Param("example") WeekStarGiftNoticeExample example);

    int updateByPrimaryKeySelective(WeekStarGiftNotice record);

    int updateByPrimaryKey(WeekStarGiftNotice record);

    List<WeekStarGiftDTO> selectByList(@Param("searchText") String searchText);

    int countEffective();

    List<WeekStarGiftDTO> selectByEffectiveGifts();

    WeekStarGiftNotice selectByGiftId(@Param("giftId") Integer giftId);
}
