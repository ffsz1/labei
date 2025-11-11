package com.juxiao.xchat.manager.common.base;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.ListUtil;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/***
 *  Created by eyre on 2018/2/1.
 * @param <T>：指定数据库的实体类
 * @param <V>：指定缓存的实体类
 *              必填，两个可以一样
 */
public abstract class CacheBaseManager<T, V> {
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private Gson gson;
    private Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    private Class<V> cacheClass = (Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];

    /***
     *  外部获取单个对象方法
     *  具体实现是实现调用getOne方法
     * @param jedisId：redis存储的key
     */
    public abstract V getOneByJedisId(String jedisId);

    /***
     *  内部获取单个对象方法
     *  外部一般不需直接调用这方法，实现外部getOneByJedisId方法即可
     * @param jedisCode：redis存储的code
     * @param jedisId：redis存储的key
     * @param sql：数据库查询的语句
     * @param arg：数据库查询的过滤条件
     */
    public V getOne(String jedisCode, String jedisId, String sql, Object... arg) {
        V cache = getOneCache(jedisCode, jedisId);
        if (cache == null) {
            T entity = findOne(sql, arg);
            if (entity != null) {
                cache = entityToCache(entity);
                saveOneCache(cache, jedisCode, jedisId);
            }
        }
        return cache;
    }

    /***
     *  内部获取单个对象缓存方法
     * @param jedisCode：redis存储的code
     * @param jedisId：redis存储的key
     */
    private V getOneCache(String jedisCode, String jedisId) {
        String str = redisManager.hget(jedisCode, jedisId);
        if (!StringUtils.isEmpty(str)) {
            return gson.fromJson(str, cacheClass);
        }
        return null;
    }

    /***
     *  内部查询单个对象方法
     * @param sql：数据库查询的语句
     * @param arg：数据库查询的过滤条件
     */
    private T findOne(String sql, Object... arg) {
        List<T> tList = jdbcTemplate.query(sql, arg, new BeanPropertyRowMapper<>(entityClass));
        if (tList != null && tList.size() != 0) {
            return tList.get(0);
        }
        return null;
    }

    /***
     *  内部保存单个对象缓存方法
     * @param cache：缓存对象
     * @param jedisCode：redis存储的code
     * @param jedisId：redis存储的key
     */
    protected void saveOneCache(V cache, String jedisCode, String jedisId) {
        String str = gson.toJson(cache);
        redisManager.hset(jedisCode, jedisId, str);
    }

    /***
     *  数据库实体类转缓存对象
     *  在外部实现，如果数据库和缓存一样的话，直接return entity
     * @param entity：数据库对象
     */
    public abstract V entityToCache(T entity);

    /***
     *  外部获取多个对象方法
     * @param jedisIdList：redis存储的key列表
     */
    public List<V> getList(List<String> jedisIdList) {
        V cache;
        List<V> vList = new ArrayList<>();
        for (String jedisId : jedisIdList) {
            cache = getOneByJedisId(jedisId);
            if (cache != null) {
                vList.add(getOneByJedisId(jedisId));
            }
        }
        return vList;
    }

    /***
     *  外部获取多个对象方法
     * @param jedisIdList：redis存储的key列表
     */
    public List<V> getList(String[] jedisIdList) {
        V cache;
        List<V> vList = new ArrayList<>();
        for (String jedisId : jedisIdList) {
            cache = getOneByJedisId(jedisId);
            if (cache != null) {
                vList.add(cache);
            }
        }
        return vList;
    }

    /***
     *  外部获取多个对象方法
     * @param jedisIds：redis存储的key列表
     */
    public List<V> getList(String jedisIds) {
        return getList(jedisIds.split(","));
    }

    /***
     *  刷新列表缓存，具体缓存外部实现
     * @param relationClass：指定class，不指定就拿定义T的class
     * @param jedisCode：缓存code
     * @param method：获取缓存key的方法名
     * @param sql：查询列表的语句
     * @param arg：过滤条件
     */
    protected <K> String refreshListCacheByCode(Class<K> relationClass, String jedisCode, String method, String sql, Object... arg){
        List tList;
        if(relationClass==null){
            tList = jdbcTemplate.query(sql, arg, new BeanPropertyRowMapper<>(entityClass));
        }else{
            tList = jdbcTemplate.query(sql, arg, new BeanPropertyRowMapper<>(relationClass));
        }
        if (tList == null || tList.size() == 0) {
            redisManager.set(jedisCode, "");
            return null;
        }
        String str = ListUtil.listEntityToString(tList, method);
        redisManager.set(jedisCode, str);
        return str;
    }

    /***
     *  刷新列表缓存，具体缓存外部实现
     * @param jedisCode：缓存code
     * @param jedisKey：缓存key
     * @param method：获取字段的方法名
     * @param sql：查询列表的语句
     * @param arg：过滤条件
     */
    protected <K> String refreshListCacheByKey(Class<K> relationClass, String jedisCode, String jedisKey, String method, String sql, Object... arg){
        List tList;
        if(relationClass==null){
            tList = jdbcTemplate.query(sql, arg, new BeanPropertyRowMapper<>(entityClass));
        }else{
            tList = jdbcTemplate.query(sql, arg, new BeanPropertyRowMapper<>(relationClass));
        }
        if (tList == null || tList.size() == 0) {
            redisManager.hset(jedisCode, jedisKey, "");
            return null;
        }
        String str = ListUtil.listEntityToString(tList, method);
        redisManager.hset(jedisCode, jedisKey, str);
        return str;
    }


    /***
     *  刷新列表缓存，具体缓存外部实现
     * @param jedisCode：缓存code
     * @param jedisKey：缓存key
     * @param method：获取字段的方法名
     * @param sql：查询列表的语句
     * @param arg：过滤条件
     */
    protected <K> String refreshBannerListCacheByKey(Class<K> relationClass, String jedisCode, String jedisKey, String method, String sql, Object... arg){
        List tList;
        if(relationClass==null){
            tList = jdbcTemplate.query(sql, arg, new BeanPropertyRowMapper<>(entityClass));
        }else{
            tList = jdbcTemplate.query(sql, arg, new BeanPropertyRowMapper<>(relationClass));
        }
        if (tList == null || tList.size() == 0) {
            redisManager.hset(jedisCode, jedisKey, "");
            return null;
        }
        String str = ListUtil.listEntityToString(tList, method);
        redisManager.hset(jedisCode, jedisKey, str);
        return str;
    }
    
    /***
     *  内部查询多个对象方法
     * @param sql：数据库查询的语句
     * @param arg：数据库查询的过滤条件
     */
    protected List<V> findList(String jediskey,Integer cashEffTime,String sql, Object... arg) {
    	String cacheStr=redisManager.get(jediskey);
    	if(StringUtils.isBlank(cacheStr)) {
    		List<V> vlist=new ArrayList<V>();
    		List<T> tList = jdbcTemplate.query(sql, arg, new BeanPropertyRowMapper<>(entityClass));
    		for(T entity:tList) {
    			V obj=entityToCache(entity);
    			vlist.add(obj);
    		}
    		if(cashEffTime!=null) {
    			redisManager.set(jediskey, gson.toJson(vlist), cashEffTime, TimeUnit.SECONDS);
    		}else {
    			redisManager.set(jediskey, gson.toJson(vlist));
    		} 
    		return vlist;
    	}else {
    		//List<V> vlist =gson.fromJson(cacheStr,new TypeToken<List<V>>() {}.getType());
    		List<V> vlist=new ArrayList<V>();
    		try {
    			Gson gson = new Gson();
    			JsonArray arry = new JsonParser().parse(cacheStr).getAsJsonArray();
    			for (JsonElement jsonElement : arry) {
    				vlist.add(gson.fromJson(jsonElement, cacheClass));
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		return vlist;
    	}
    }

}
