package com.juxiao.xchat.dao.item;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.item.dto.GiftDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 礼物数据库操作接口
 *
 * @class: GiftDao.java
 * @author: chenjunsheng
 * @date 2018/6/8
 */
public interface GiftDao {

    /**
     * 查询数据库中的礼物
     *
     * @param giftId
     * @return
     * @author: chenjunsheng
     * @date 2018/6/8
     */
    @TargetDataSource(name = "ds2")
    GiftDTO getGift(@Param("giftId") Integer giftId);

    /**
     * 根据礼物类型, 加载礼物
     *
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<GiftDTO> listGifts(@Param("giftType") Integer giftType);
}