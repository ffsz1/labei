package com.juxiao.xchat.manager.common.item.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.item.GiftDao;
import com.juxiao.xchat.dao.item.dto.GiftDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.item.GiftManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 礼物公共操作类
 *
 * @class: GiftManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/8
 */
@Service
public class GiftManagerImpl implements GiftManager {
    @Autowired
    private GiftDao giftDao;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private Gson gson;

    @Override
    public GiftDTO getValidGiftById(Integer giftId) {
        // 校验缓存
        List<GiftDTO> giftDTOS = listGift(null);
        if (CollectionUtils.isEmpty(giftDTOS)) {
            return null;
        } else {
            return this.findGiftByGiftId(giftDTOS, giftId);
        }
    }

    @Override
    public List<GiftDTO> listGift(Integer type) {
        List<GiftDTO> list = new ArrayList<>();
        String giftStr = redisManager.get(RedisKey.gift_all.getKey());
        if (StringUtils.isNotBlank(giftStr)) {
            list = gson.fromJson(giftStr, new TypeToken<List<GiftDTO>>() {
            }.getType());
        } else {
            try {
                list = giftDao.listGifts(null);// 查询礼物
                redisManager.set(RedisKey.gift_all.getKey(), gson.toJson(list));
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        if (type == null) { // 全部礼物
            return list;
        } else {
            return filterGiftByType(list, type);
        }
    }

    private List<GiftDTO> filterGiftByType(List<GiftDTO> list, int type) {
        if (CollectionUtils.isEmpty(list)) return null;
        List<GiftDTO> giftDTOS = new ArrayList<>();
        for (GiftDTO giftDTO : list) {
            if (giftDTO.getGiftType() == type) {
                giftDTOS.add(giftDTO);
            }
        }
        return giftDTOS;
    }

    private GiftDTO findGiftByGiftId(List<GiftDTO> list, int giftId) {
        GiftDTO gift = null;
        if (CollectionUtils.isEmpty(list)) return null;
        for (GiftDTO giftDTO : list) {
            if (giftDTO.getGiftId() == giftId) {
                gift = giftDTO;
                break;
            }
        }
        return gift;
    }

    /**
     * 根据ID查询(包含有效无效礼物)
     *
     * @param giftId giftId
     * @return GiftDTO
     */
    @Override
    public GiftDTO getGiftById(Integer giftId) {
        // 校验缓存
        String giftStr = redisManager.hget(RedisKey.gift_all.getKey(), String.valueOf(giftId));
        if (StringUtils.isNotBlank(giftStr)) {
            GiftDTO giftDTO = gson.fromJson(giftStr, GiftDTO.class);
            if (giftDTO != null) {
                return giftDTO;
            }
        }
        GiftDTO giftDTO = giftDao.getGift(giftId);
        if (giftDTO == null) {
            return null;
        }
        // 添加到缓存
        redisManager.hset(RedisKey.gift_all.getKey(), giftDTO.getGiftId().toString(), gson.toJson(giftDTO));
        return giftDTO;
    }
}
