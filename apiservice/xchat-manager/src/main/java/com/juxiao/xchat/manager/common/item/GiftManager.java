package com.juxiao.xchat.manager.common.item;

import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.item.dto.GiftDTO;

import java.util.List;

/**
 * 礼物公共操作类
 *
 * @class: GiftManager.java
 * @author: chenjunsheng
 * @date 2018/6/8
 */
public interface GiftManager {

    /**
     * 根据ID获取有效礼物
     *
     * @param gifId
     * @return
     */
    GiftDTO getValidGiftById(Integer gifId);

    /**
     * 查询礼物
     * @return
     */
    List<GiftDTO> listGift(Integer type);

    /**
     * 根据ID查询(包含有效无效礼物)
     * @param giftId giftId
     * @return GiftDTO
     */
    GiftDTO getGiftById(Integer giftId);
}
