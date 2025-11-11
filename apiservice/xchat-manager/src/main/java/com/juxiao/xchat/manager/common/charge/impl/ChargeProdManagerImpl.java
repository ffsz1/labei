package com.juxiao.xchat.manager.common.charge.impl;

import com.google.common.collect.Lists;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.Utils;
import com.juxiao.xchat.dao.charge.dto.ChargeChannelProdDTO;
import com.juxiao.xchat.dao.charge.dto.ChargeProdDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.base.CacheBaseManager;
import com.juxiao.xchat.manager.common.charge.ChargeProdManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @class: ChargeProdManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/11
 */
@Service
public class ChargeProdManagerImpl extends CacheBaseManager<ChargeProdDTO, ChargeProdDTO> implements ChargeProdManager {
    @Autowired
    private RedisManager redisManager;

    @Override
    public ChargeProdDTO getOneByJedisId(String jedisId) {
        return getOne(RedisKey.charge_prod.getKey(), jedisId, "select * from charge_prod where charge_prod_id = ? and prod_status = 1 ", jedisId);
    }

    @Override
    public ChargeProdDTO entityToCache(ChargeProdDTO entity) {
        return entity;
    }

    public String refreshProdListCache(String jedisCode) {
        return refreshListCacheByCode(null, jedisCode, "getChargeProdId", "select * from charge_prod where prod_status = 1 order by seq_no asc ");
    }

    /**
     * @see com.juxiao.xchat.manager.common.charge.ChargeProdManager#getChargeProd(String)
     */
    @Override
    public ChargeProdDTO getChargeProd(String chargeProdId) {
        return getOneByJedisId(chargeProdId);
    }

    @Override
    public List<ChargeChannelProdDTO> findChargeProd(int type, String appVersion) {
        String str = redisManager.get(RedisKey.charge_prod_list.getKey());
        if (StringUtils.isBlank(str)) {
            str = refreshProdListCache(RedisKey.charge_prod_list.getKey());
        }

        List<ChargeChannelProdDTO> chargeProdVoList = Lists.newArrayList();
        ChargeChannelProdDTO chargeChannelProdDTO;
        List<ChargeProdDTO> chargeProdList = getList(str);
        for (ChargeProdDTO chargeProd : chargeProdList) {
            if (chargeProd.getChannel() == type) {
                chargeChannelProdDTO = new ChargeChannelProdDTO();
                BeanUtils.copyProperties(chargeProd, chargeChannelProdDTO);
                chargeProdVoList.add(chargeChannelProdDTO);
            }
        }
        Collections.sort(chargeProdVoList);
        return chargeProdVoList;
    }
}
