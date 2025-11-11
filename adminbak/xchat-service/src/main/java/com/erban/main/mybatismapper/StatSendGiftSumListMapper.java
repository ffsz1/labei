package com.erban.main.mybatismapper;

import com.erban.main.model.StatSendGiftSumList;
import com.erban.main.model.StatSendGiftSumListExample;
import java.util.List;

public interface StatSendGiftSumListMapper {
    int deleteByPrimaryKey(Long giftListId);

    int insert(StatSendGiftSumList record);

    int insertSelective(StatSendGiftSumList record);

    List<StatSendGiftSumList> selectByExample(StatSendGiftSumListExample example);

    StatSendGiftSumList selectByPrimaryKey(Long giftListId);

    int updateByPrimaryKeySelective(StatSendGiftSumList record);

    int updateByPrimaryKey(StatSendGiftSumList record);
}
