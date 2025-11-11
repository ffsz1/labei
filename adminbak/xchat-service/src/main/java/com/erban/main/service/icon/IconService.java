package com.erban.main.service.icon;

import com.erban.main.model.Icon;
import com.erban.main.service.base.CacheBaseService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.IconVo;
import com.google.common.collect.Lists;
import com.xchat.common.redis.RedisKey;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IconService extends CacheBaseService<Icon, IconVo> {

    @Override
    public IconVo getOneByJedisId(String jedisId) {
        return getOne(RedisKey.icon.getKey(), jedisId, "select * from icon where icon_id = ? and status = 1 ", jedisId);
    }

    @Override
    public IconVo entityToCache(Icon entity) {
        IconVo cache = new IconVo();
        cache.setTitle(entity.getName());
        cache.setSubtitle(entity.getSubtitle());
        cache.setPic(entity.getPic());
        cache.setActivity(entity.getActivity());
        cache.setIosActivity(entity.getIosActivity());
        cache.setUrl(entity.getIconUrl());
        Map<String, Object> map = new HashMap<>();
        if(!StringUtils.isEmpty(entity.getKey1())){
            map.put(entity.getKey1(), entity.getValue1());
        }
        if(!StringUtils.isEmpty(entity.getKey2())){
            map.put(entity.getKey2(), entity.getValue2());
        }
        if(!StringUtils.isEmpty(entity.getKey3())){
            map.put(entity.getKey3(), entity.getValue3());
        }
        cache.setParams(gson.toJson(map));
        return cache;
    }

    /**
     * 刷新icon缓存
     */
    public String refreshIconListCache(String jedisCode, String types) {
        return refreshListCacheByCode(null, jedisCode, "getIconId", "select * from icon where status = 1 and type in (?) order by seq asc ", types);
    }

    /**
     * 获取首页icon
     */
    public List<IconVo> getIcon(boolean isCheck, String appVersion){
        Integer version = 104;
        if(StringUtils.isNotBlank(appVersion)){
            version = Integer.valueOf(appVersion.replaceAll("\\.", ""));
        }
        if(version < 104) {
            List<IconVo> iconVoList = Lists.newArrayList();
            IconVo iconVo = new IconVo();
            iconVo.setTitle("幸运大抽奖");
            iconVo.setSubtitle("充值赢大奖");
            iconVo.setPic("http://res.91fb.com/icon-zhuanpan.png");
            iconVo.setActivity("");
            iconVo.setIosActivity("");
            iconVo.setUrl("https://www.47huyu.cn/mm/luckdraw/index.html?activity");
            iconVo.setParams("");
            iconVoList.add(iconVo);
            iconVo = new IconVo();
            iconVo.setTitle("魅力财富榜");
            iconVo.setSubtitle("荣耀大比拼");
            iconVo.setPic("http://res.91fb.com/icon-paihangbang.png");
            iconVo.setActivity("");
            iconVo.setIosActivity("");
            iconVo.setUrl("https://www.47huyu.cn/mm/rank/index.html");
            iconVo.setParams("");
            iconVoList.add(iconVo);
            return iconVoList;
        }
        String str;
        if(isCheck){
            str = jedisService.get(RedisKey.icon_index_v.getKey());
        }else{
            str = jedisService.get(RedisKey.icon_index.getKey());
        }
        if(StringUtils.isEmpty(str)){
            if(isCheck){
                str = refreshIconListCache(RedisKey.icon_index_v.getKey(), "1");
            }else{
                str = refreshIconListCache(RedisKey.icon_index.getKey(), "0");
            }
            if(StringUtils.isEmpty(str)){
                return null;
            }
        }
        return getList(StringUtils.splitToList(str, ","));
    }

}
