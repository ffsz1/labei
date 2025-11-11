package com.juxiao.xchat.service.api.item;

import com.juxiao.xchat.service.api.item.vo.GiftVO;

import java.util.List;
import java.util.Map;

public interface GiftService {

    Map<String, Object> listGifts(Long uid);

    /**
     * 获取所有的礼物列表
     * @param uid
     * @param type 1 表白礼物
     * @return
     */
    Map<String, Object> listGifts(Long uid, Integer type);
}
