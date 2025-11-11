package com.erban.main.service.headwear;

import com.erban.main.model.HeadwearPurseRecord;
import com.erban.main.service.base.CacheBaseService;
import com.erban.main.util.StringUtils;
import com.xchat.common.redis.RedisKey;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HeadwearPurseService extends CacheBaseService<HeadwearPurseRecord, HeadwearPurseRecord> {

    @Override
    public HeadwearPurseRecord getOneByJedisId(String jedisId) {
        return null;
    }

    public HeadwearPurseRecord getOneByJedisId(String uid, String headwearId) {
        return getOne(RedisKey.headwear_purse.getKey(), uid + "_" + headwearId, "select * from headwear_purse_record where uid = ? and headwear_id = ? ", uid, headwearId);
    }

    public String refreshPurseCache(String jedisCode, String uid) {
        List<HeadwearPurseRecord> tList = jdbcTemplate.query("select * from headwear_purse_record where uid = ? ", new BeanPropertyRowMapper<>(HeadwearPurseRecord.class), uid);
        if (tList == null || tList.size() == 0) {
            jedisService.hwrite(jedisCode, uid, "");
            return null;
        }
        StringBuffer str = new StringBuffer();
        HeadwearPurseRecord headwearPurseRecord;
        for (int i = 0; i < tList.size(); i++) {
            headwearPurseRecord = tList.get(i);
            if (i == 0) {
                str.append(headwearPurseRecord.getHeadwearId());
            } else {
                str.append(",").append(headwearPurseRecord.getHeadwearId());
            }
        }
        jedisService.hwrite(jedisCode, uid, str.toString());
        return str.toString();
    }

    public String getPurse(Long uid) {
        String str = jedisService.hget(RedisKey.headwear_purse_list.getKey(), uid.toString());
        if (StringUtils.isEmpty(str)) {
            str = refreshPurseCache(RedisKey.headwear_purse_list.getKey(), uid.toString());
            if (StringUtils.isEmpty(str)) {
                return null;
            }
        }
        return str;
    }

    @Override
    public HeadwearPurseRecord entityToCache(HeadwearPurseRecord entity) {
        return entity;
    }

}
