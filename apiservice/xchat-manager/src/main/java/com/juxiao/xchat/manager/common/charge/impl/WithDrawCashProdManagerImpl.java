package com.juxiao.xchat.manager.common.charge.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.charge.WithDrawCashProdDao;
import com.juxiao.xchat.dao.charge.dto.WithDrawCashProdDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.charge.WithDrawCashProdManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @class: WithDrawCashProdManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/25
 */
@Service
public class WithDrawCashProdManagerImpl implements WithDrawCashProdManager {
    @Autowired
    private WithDrawCashProdDao cashProdDao;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private Gson gson;

    /**
     * @see com.juxiao.xchat.manager.common.charge.WithDrawCashProdManager#getCashNum(String)
     */
    @Override
    public Long getCashNum(String pid) {
        String prodStr = redisManager.hget(RedisKey.withdraw_cash_list.getKey(), pid);
        if (!StringUtils.isBlank(prodStr)) {
            WithDrawCashProdDTO prodDto = gson.fromJson(prodStr, WithDrawCashProdDTO.class);
            if (prodDto != null) {
                return prodDto.getCashNum();
            }
        }

        WithDrawCashProdDTO prodDto = cashProdDao.getCashProd(pid);
        if (prodDto != null && StringUtils.isNotBlank(prodDto.getCashProdId())) {
            redisManager.hset(RedisKey.withdraw_cash_list.getKey(), pid, gson.toJson(prodDto));
        }
        return prodDto == null ? null : prodDto.getCashNum();
    }

    /**
     * @see com.juxiao.xchat.manager.common.charge.WithDrawCashProdManager#listAllCashProds()
     */
    @Override
    public List<WithDrawCashProdDTO> listAllCashProds() {
        List<WithDrawCashProdDTO> list = Lists.newArrayList();
        Map<String, String> map = redisManager.hgetAll(RedisKey.withdraw_cash_list.getKey());
        if (map != null && map.size() > 0) {
            Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                String value = entry.getValue();
                if (StringUtils.isNotEmpty(value)) {
                    WithDrawCashProdDTO prodDto = gson.fromJson(entry.getValue(), WithDrawCashProdDTO.class);
                    list.add(prodDto);
                }
            }
            Collections.sort(list, new Comparator<WithDrawCashProdDTO>() {
                @Override
                public int compare(WithDrawCashProdDTO o1, WithDrawCashProdDTO o2) {
                    return o1.getSeqNo() > o2.getSeqNo()?1:-1;
                }
            });
            return list;
        }

        list = cashProdDao.listAllCashProds();
        if (list != null && list.size() > 0) {
            for (WithDrawCashProdDTO prod : list) {
                redisManager.hset(RedisKey.withdraw_cash_list.getKey(), prod.getCashProdId(), gson.toJson(prod));
            }
        }
        return list;
    }

    /**
     * 根据提现产品ID查询
     *
     * @param pid pid
     * @return WithDrawCashProdDTO
     */
    @Override
    public WithDrawCashProdDTO getWithDrawCashProdByPid(String pid) {
        return cashProdDao.getCashProd(pid);
    }
}
