package com.erban.admin.main.base;

import com.google.gson.Gson;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public abstract class RedisSupportService<T, E> extends AbstractCoreService<T, E>{
    @Autowired
    private JedisService jedisService;

    private Gson gson = new Gson();

    @Override
    public int save(T entity, boolean isEdit, boolean isSelective) {
        int result = super.save(entity, isEdit, isSelective);
        if(result > 0) {
            jedisService.hwrite(getRedisKey(), getId(entity).toString(), gson.toJson(entity));
        }
        return result;
    }

    @Override
    public int delete(Object id) {
        int result = super.delete(id);
        if(result > 0) {
            jedisService.hdel(getRedisKey(), id.toString());
        }
        return result;
    }

    protected abstract String getRedisKey();
}
