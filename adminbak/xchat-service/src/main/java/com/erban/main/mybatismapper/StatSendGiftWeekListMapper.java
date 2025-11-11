package com.erban.main.mybatismapper;

import com.erban.main.model.StatSendGiftWeekList;
import com.erban.main.model.StatSendGiftWeekListExample;
import java.util.List;

public interface StatSendGiftWeekListMapper {
    int deleteByPrimaryKey(Long giftListId);

    int insert(StatSendGiftWeekList record);

    int insertSelective(StatSendGiftWeekList record);

    List<StatSendGiftWeekList> selectByExample(StatSendGiftWeekListExample example);

    StatSendGiftWeekList selectByPrimaryKey(Long giftListId);

    int updateByPrimaryKeySelective(StatSendGiftWeekList record);

    int updateByPrimaryKey(StatSendGiftWeekList record);
}
