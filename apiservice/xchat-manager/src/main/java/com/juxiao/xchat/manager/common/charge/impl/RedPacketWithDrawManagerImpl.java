package com.juxiao.xchat.manager.common.charge.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.charge.WithDrawPacketCashProdDao;
import com.juxiao.xchat.dao.charge.dto.RedPacketCashProdDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.charge.RedPacketWithDrawManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class RedPacketWithDrawManagerImpl implements RedPacketWithDrawManager {
    @Autowired
    private WithDrawPacketCashProdDao packetCashProdDao;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private Gson gson;

    @Override
    public List<RedPacketCashProdDTO> listUsingPacketCashProd() {
        String listStr = redisManager.get(RedisKey.packet_withdraw_cash_list.getKey());
        if (StringUtils.isNotBlank(listStr)) {
            return gson.fromJson(listStr, new TypeToken<List<RedPacketCashProdDTO>>() {
            }.getType());
        }

        List<RedPacketCashProdDTO> list = packetCashProdDao.listUseingPacketCashProd();
        if (list != null && list.size() > 0) {
            Collections.sort(list);
            redisManager.set(RedisKey.packet_withdraw_cash_list.getKey(), gson.toJson(list));
        }
        return list;
    }

}
