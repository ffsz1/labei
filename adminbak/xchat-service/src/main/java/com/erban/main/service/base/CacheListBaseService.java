package com.erban.main.service.base;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/***
 *  Created by eyre on 2018/2/1.
 * @param <T>：指定数据库的实体类
 * @param <V>：指定缓存的实体类
 *              必填，两个可以一样
 */
public abstract class CacheListBaseService<T, V> extends BaseService {

    private Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    private Class<V> cacheClass = (Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];

    /***
     *  外部获取列表对象方法
     *  具体实现是实现调用getList方法
     * @param jedisId：redis存储的key
     */
    public abstract List<V> getListByJedisId(String jedisId);

    /***
     *  内部获取列表对象方法
     *  外部一般不需直接调用这方法，实现外部getListByJedisId方法即可
     * @param jedisCode：redis存储的code
     * @param jedisId：redis存储的key
     * @param sql：数据库查询的语句
     * @param arg：数据库查询的过滤条件
     */
    public List<V> getList(String jedisCode, String jedisId, String sql, Object... arg) {
        List<V> cacheList = getListCache(jedisCode, jedisId);
        if (cacheList == null||cacheList.size()==0) {
            List<T> entityList = findList(sql, arg);
            if (entityList != null&&entityList.size()>0) {
                cacheList = new ArrayList<>();
                for(T entity:entityList){
                    cacheList.add(entityToCache(entity));
                }
                saveListCache(cacheList, jedisCode, jedisId);
            }
        }
        return cacheList;
    }

    /***
     *  内部刷新缓存方法方法
     * @param jedisCode：redis存储的code
     * @param jedisId：redis存储的key
     * @param sql：数据库查询的语句
     * @param arg：数据库查询的过滤条件
     */
    public void refresh(String jedisCode, String jedisId, String sql, Object... arg) {
        List<T> entityList = findList(sql, arg);
        if (entityList != null&&entityList.size()>0) {
            List<V> cacheList = new ArrayList<>();
            for(T entity:entityList){
                cacheList.add(entityToCache(entity));
            }
            saveListCache(cacheList, jedisCode, jedisId);
        }
    }

    /***
     *  内部获取列表对象缓存方法
     * @param jedisCode：redis存储的code
     * @param jedisId：redis存储的key
     */
    private List<V> getListCache(String jedisCode, String jedisId) {
        String str = jedisService.hget(jedisCode, jedisId);
        if (!StringUtils.isEmpty(str)) {
            List<V> vList = new ArrayList<>();
            JsonParser parser = new JsonParser();
            JsonArray jsonArray = parser.parse(str).getAsJsonArray();
            for (JsonElement json : jsonArray) {
                vList.add(gson.fromJson(json, cacheClass));
            }
            return vList;
        }
        return null;
    }

    /***
     *  内部查询列表对象方法
     * @param sql：数据库查询的语句
     * @param arg：数据库查询的过滤条件
     */
    private List<T> findList(String sql, Object... arg) {
        return jdbcTemplate.query(sql, arg, new BeanPropertyRowMapper<>(entityClass));
    }

    /***
     *  内部保存列表对象缓存方法
     * @param cache：缓存对象
     * @param jedisCode：redis存储的code
     * @param jedisId：redis存储的key
     */
    private void saveListCache(List<V> cache, String jedisCode, String jedisId) {
        String str = gson.toJson(cache);
        jedisService.hwrite(jedisCode, jedisId, str);
    }

    /***
     *  数据库实体类转缓存对象
     *  在外部实现，如果数据库和缓存一样的话，直接return entity
     * @param entity：数据库对象
     */
    public abstract V entityToCache(T entity);

}
