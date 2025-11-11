package com.erban.main.service.wx;

import com.erban.main.model.WxRecord;
import com.erban.main.service.base.CacheBaseService;
import com.erban.main.util.StringUtils;
import com.xchat.common.redis.RedisKey;
import org.springframework.stereotype.Service;

@Service
public class WxRecordService extends CacheBaseService<WxRecord, WxRecord> {

    @Override
    public WxRecord getOneByJedisId(String jedisId) {
        return getOne(RedisKey.wx_record.getKey(), jedisId, "select * from wx_record where record_id = ? ", jedisId);
    }

    @Override
    public WxRecord entityToCache(WxRecord entity) {
        return entity;
    }

    public String refreshListCache(String jedisCode, String jedisKey) {
        return refreshListCacheByKey(null, jedisCode, jedisKey, "getRecordId", "select * from wx_record where uid = ? order by record_id desc ", jedisKey);
    }

    public String getStrList(String jedisKey){
        String str = jedisService.hget(RedisKey.wx_record_list.getKey(), jedisKey);
        if(StringUtils.isEmpty(str)){
            str = refreshListCache(RedisKey.wx_record_list.getKey(), jedisKey);
            if(StringUtils.isEmpty(str)){
                return null;
            }
        }
        return str;
    }

}
