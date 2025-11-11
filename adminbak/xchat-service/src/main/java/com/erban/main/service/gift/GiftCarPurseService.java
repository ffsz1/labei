package com.erban.main.service.gift;

import com.erban.main.model.GiftCarPurseRecord;
import com.erban.main.service.base.CacheBaseService;
import com.erban.main.util.StringUtils;
import com.xchat.common.redis.RedisKey;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GiftCarPurseService extends CacheBaseService<GiftCarPurseRecord, GiftCarPurseRecord> {

    @Override
    public GiftCarPurseRecord getOneByJedisId(String jedisId) {
        return null;
    }

    public GiftCarPurseRecord getOneByJedisId(String uid, String carId) {
        return getOne(RedisKey.gift_car_purse.getKey(), uid + "_" + carId, "select * from gift_car_purse_record where uid = ? and car_id = ? ", uid, carId);
    }

    public String refreshPurseCache(String jedisCode, String uid) {
        List<GiftCarPurseRecord> tList = jdbcTemplate.query("select * from gift_car_purse_record where uid = ? ", new BeanPropertyRowMapper<>(GiftCarPurseRecord.class), uid);
        if (tList == null || tList.size() == 0) {
            jedisService.hwrite(jedisCode, uid, "");
            return null;
        }
        StringBuffer str = new StringBuffer();
        GiftCarPurseRecord giftCarPurseRecord;
        for (int i = 0; i < tList.size(); i++) {
            giftCarPurseRecord = tList.get(i);
            if (i == 0) {
                str.append(giftCarPurseRecord.getCarId());
            } else {
                str.append(",").append(giftCarPurseRecord.getCarId());
            }
        }
        jedisService.hwrite(jedisCode, uid, str.toString());
        return str.toString();
    }

    public String getPurse(Long uid) {
        String str = jedisService.hget(RedisKey.gift_car_purse_list.getKey(), uid.toString());
        if (StringUtils.isEmpty(str)) {
            str = refreshPurseCache(RedisKey.gift_car_purse_list.getKey(), uid.toString());
            if (StringUtils.isEmpty(str)) {
                return null;
            }
        }
        return str;
    }

    @Override
    public GiftCarPurseRecord entityToCache(GiftCarPurseRecord entity) {
        return entity;
    }

}
