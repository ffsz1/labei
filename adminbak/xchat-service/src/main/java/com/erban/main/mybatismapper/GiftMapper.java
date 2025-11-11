package com.erban.main.mybatismapper;

import com.erban.main.base.BaseMapper;
import com.erban.main.model.Gift;
import com.erban.main.model.GiftExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface GiftMapper extends BaseMapper<Gift, GiftExample> {
    // int countByExample(GiftExample example);
    //
    // int deleteByExample(GiftExample example);
    //
    // int deleteByPrimaryKey(Integer giftId);
    //
    // int insert(Gift record);
    //
    // int insertSelective(Gift record);
    //
    // List<Gift> selectByExample(GiftExample example);
    //
    // Gift selectByPrimaryKey(Integer giftId);
    //
    // int updateByExampleSelective(@Param("record") Gift record, @Param("example") GiftExample example);
    //
    // int updateByExample(@Param("record") Gift record, @Param("example") GiftExample example);
    //
    // int updateByPrimaryKeySelective(Gift record);
    //
    // int updateByPrimaryKey(Gift record);

    /**
     * 获取520及以上金币的礼物
     *
     * @param giftType 礼物类型
     * @return
     */
    List<Gift> queryHighPriceGifts(@Param("giftType") byte giftType);
}
